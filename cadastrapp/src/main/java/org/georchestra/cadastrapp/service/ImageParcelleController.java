package org.georchestra.cadastrapp.service;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.georchestra.cadastrapp.configuration.CadastrappPlaceHolder;
import org.geotools.data.ows.HTTPClient;
import org.geotools.data.ows.Layer;
import org.geotools.data.ows.SimpleHttpClient;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.data.wms.WebMapServer;
import org.geotools.data.wms.request.GetMapRequest;
import org.geotools.data.wms.response.GetMapResponse;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.geometry.jts.JTS;
import org.geotools.ows.ServiceException;
import org.opengis.filter.Filter;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.awt.ShapeWriter;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Image Parcelle Controller
 * 
 * @author gfi
 *
 */
public class ImageParcelleController extends CadController {

	final static Logger logger = LoggerFactory.getLogger(ImageParcelleController.class);

	final private String URL_GET_CAPABILITIES = "?REQUEST=GetCapabilities&version=1.0.0";

	final private String URL_GET_CAPABILITIES_WMS = "?VERSION=1.1.1&Request=GetCapabilities&Service=WMS";

	final private String GET_CAPABILITIES_URL_PARAM = "WFSDataStoreFactory:GET_CAPABILITIES_URL";
	final private String USERNAME_PARAM = "WFSDataStoreFactory:USERNAME";
	final private String PASSWORD_PARAM = "WFSDataStoreFactory:PASSWORD";
	
	// buffer distance in CRS unit
	final private double BUFFER_DISTANCE = 10.0;

	/**
	 * Using a given parcelle id, this service will get feature from WFS
	 * service, get Basemap image using Boundingbox of the feature and add
	 * compass and scale on it
	 * 
	 * @param parcelle
	 *            parcelle ID
	 * 
	 * @return Response with noContent in case of error, png otherwise
	 * 
	 */
	@GET
	@Path("/getImageBordereau")
	@Produces("image/png")
	public Response createImageBordereauParcellaire(@QueryParam("parcelle") String parcelle) {

		// Create empty reponse for default value
		ResponseBuilder response = Response.noContent();

		final int parcelleIdLength = Integer.parseInt(CadastrappPlaceHolder.getProperty("parcelleId.length"));

		// Check parcelle value, at least
		if (parcelle != null && parcelle.length() > parcelleIdLength) {

			int visibleLength = 0;
			int visibleHeight = 0;
			BufferedImage baseMapImage;
			BufferedImage parcelleImage;
			BoundingBox bounds;

			// Get parcelle geo information
			// get featureById
			final String wfsUrl = CadastrappPlaceHolder.getProperty("cadastre.wfs.url");

			String getCapabilities = wfsUrl + URL_GET_CAPABILITIES;
			
			logger.debug("Call WFS with plot Id " + parcelle + " and WFS URL : " + getCapabilities);

			Map<String, String> connectionParameters = new HashMap<String, String>();
			connectionParameters.put(GET_CAPABILITIES_URL_PARAM, getCapabilities);
			
			// Add basic authent parameter if not empty
			final String cadastreWFSUsername = CadastrappPlaceHolder.getProperty("cadastre.wfs.username");
			final String cadastreWFSPassword = CadastrappPlaceHolder.getProperty("cadastre.wfs.password");
			if (cadastreWFSUsername != null && !cadastreWFSUsername.isEmpty()
					&& cadastreWFSUsername != null && !cadastreWFSUsername.isEmpty()){
				connectionParameters.put(USERNAME_PARAM, cadastreWFSUsername);
				connectionParameters.put(PASSWORD_PARAM, cadastreWFSPassword);
			}
			
			WFSDataStoreFactory dsf = new WFSDataStoreFactory();

			WFSDataStore dataStore;

			try {
				dataStore = dsf.createDataStore(connectionParameters);

				SimpleFeatureSource source;

				final String cadastreWFSLayerName = CadastrappPlaceHolder.getProperty("cadastre.wfs.layer.name");
				final String cadastreLayerIdParcelle = CadastrappPlaceHolder.getProperty("cadastre.layer.idParcelle");

				source = dataStore.getFeatureSource(cadastreWFSLayerName);

				// Make sure source have been found before making request filter
				if (source != null) {
					
					StringBuilder filterBuilder = new StringBuilder();
					filterBuilder.append(cadastreLayerIdParcelle);
					filterBuilder.append(" = '");
					filterBuilder.append(parcelle);
					filterBuilder.append("'");
					
					Filter filter = CQL.toFilter(filterBuilder.toString());
					
					if(logger.isDebugEnabled()){
						logger.debug("WFS call information");
						logger.debug("WFS layerName : " + cadastreWFSLayerName);
						logger.debug("Filter information : " + filterBuilder.toString());
					}
						
					SimpleFeatureCollection collection = source.getFeatures(filter);
					
					// Nullpointer exception in geotools features() when WFS is activate but not publish in geoserver
					// collection is not null, but features() method throw a java.lang.NullPointerException
					// org.geotools.data.wfs.v1_0_0.NonStrictWFSStrategy.correctFilterForServer(NonStrictWFSStrategy.java:221)
					SimpleFeatureIterator it = null;
					try{
						it = collection.features();
					} catch (NullPointerException e) {
						logger.error("Error when try to filter information from WFS service, check WFS service is activated and published", e);
					} 

					// Check if there is a leat one feature
					if (it != null && it.hasNext()) {

						logger.debug("Get feature");
						// Get only the first plot
						SimpleFeature parcelleFeature = it.next();
						
						bounds = parcelleFeature.getBounds();
						CoordinateReferenceSystem crs = bounds.getCoordinateReferenceSystem();
				
						Geometry targetGeometry = (Geometry) parcelleFeature.getDefaultGeometry();
						Geometry bufferGeometry = targetGeometry;
						
						float pdfImageWidth = Integer.parseInt(CadastrappPlaceHolder.getProperty("pdf.imageWidth"));
						float pdfImageHeight = Integer.parseInt(CadastrappPlaceHolder.getProperty("pdf.imageHeight"));
						
						// If CRS null
						if (crs == null || targetGeometry == null) {
							logger.error("CRS not known by geotools, no buffering can be made, scale won't be seeing on image");

						} else {
							if(logger.isDebugEnabled()){
								logger.debug("CRS : " + crs);
								logger.debug("Create buffer");
							}
							bufferGeometry = targetGeometry.buffer(BUFFER_DISTANCE);

							// transform JTS enveloppe to geotools enveloppe
							Envelope envelope = bufferGeometry.getEnvelopeInternal();
							bounds = JTS.getEnvelope2D(envelope, crs);

							// Get distance beetween two point here bounds is used
							Coordinate lowerLeftCorner = new Coordinate(bounds.getMinX(), bounds.getMinY());
							Coordinate lowerRightCorner = new Coordinate(bounds.getMaxX(), bounds.getMinY());
							Coordinate upperLeftCorner = new Coordinate(bounds.getMinX(), bounds.getMaxY());

							try {
								double bblength = JTS.orthodromicDistance(lowerLeftCorner, lowerRightCorner, crs);
								visibleLength = (int) bblength;						
								logger.debug("Bounding box length : " + visibleLength + " meters");
								
								double bbheight = JTS.orthodromicDistance(lowerLeftCorner, upperLeftCorner, crs);
								visibleHeight = (int) bbheight;
								logger.debug("Bounding box height : " + visibleHeight + " meters");
								
								// calculate current ratio
								float ratio =  (float) visibleLength / visibleHeight;
								logger.debug("Ratio L/W : " + ratio );
								
								// Wider than it is tall
								if(ratio < 1){
									pdfImageWidth = pdfImageWidth * ratio;
								}
								// Taller than it is wide
								else{
									pdfImageHeight = pdfImageWidth / ratio;	
								}
								
								logger.debug("Image size  : " + pdfImageHeight + " : " + pdfImageWidth);
								
							} catch (TransformException e) {
								logger.error("Could not calculate distance, no scale bar will be displayed on image", e);
							}
						}

						logger.debug("Call WMS for plot");
						// Get parcelle image with good BBOX
						final String wmsUrl = CadastrappPlaceHolder.getProperty("cadastre.wms.url");
						final String cadastreFormat = CadastrappPlaceHolder.getProperty("cadastre.format");
						final String cadastreWMSLayerName = CadastrappPlaceHolder.getProperty("cadastre.wms.layer.name");

						URL parcelleWMSUrl = new URL(wmsUrl + URL_GET_CAPABILITIES_WMS);
						WebMapServer wmsParcelle = null;

						logger.debug("WMS URL : " + parcelleWMSUrl);
						
						
						// Add basic authent parameter if not empty
						final String cadastreWMSUsername = CadastrappPlaceHolder.getProperty("cadastre.wms.username");
						final String cadastreWMSPassword = CadastrappPlaceHolder.getProperty("cadastre.wms.password");
						
						// if authentification is not null
						if (cadastreWFSUsername != null && !cadastreWFSUsername.isEmpty()
								&& cadastreWMSPassword != null && !cadastreWMSPassword.isEmpty()){
							
							HTTPClient httpClient = new SimpleHttpClient();
							httpClient.setUser(cadastreWMSUsername);
							httpClient.setPassword(cadastreWMSPassword);
							
							wmsParcelle = new WebMapServer(parcelleWMSUrl, httpClient);
						}
						// else without authentification
						else{
							wmsParcelle = new WebMapServer(parcelleWMSUrl);
						}
						
						GetMapRequest requestParcelle = wmsParcelle.createGetMapRequest();
						requestParcelle.setFormat(cadastreFormat);

						// Add layer see to set this in configuration parameters
						// Or use getCapatibilities
						Layer layerParcelle = new Layer("Parcelle cadastrapp");
						layerParcelle.setName(cadastreWMSLayerName);
						requestParcelle.addLayer(layerParcelle);

						// sets the dimensions check with PDF size available
						requestParcelle.setDimensions((int) pdfImageWidth, (int)pdfImageHeight);
						final String cadastreSRS = CadastrappPlaceHolder.getProperty("cadastre.SRS");
						requestParcelle.setSRS(cadastreSRS);
						requestParcelle.setTransparent(true);
						
						// setBBox from Feature information
						requestParcelle.setBBox(bounds);

						logger.debug("Create feature picture");
						GetMapResponse parcelleResponse = (GetMapResponse) wmsParcelle.issueRequest(requestParcelle);			
						parcelleImage = ImageIO.read(parcelleResponse.getInputStream());

						logger.debug("Create final picture");
						// createFinal buffer with pdf image size and not with result from parcellResponse
						BufferedImage finalImage = new BufferedImage((int)pdfImageWidth, (int)pdfImageHeight, BufferedImage.TYPE_INT_ARGB);

						Graphics2D g2 = finalImage.createGraphics();

						// Add basemap only if parameter is defined
						final String baseMapWMSUrl = CadastrappPlaceHolder.getProperty("baseMap.WMS.url");

						if (baseMapWMSUrl != null && baseMapWMSUrl.length() > 1) {
							// Get basemap image with good BBOX
							try {
								logger.debug("WMS call for basemap with URL : " + baseMapWMSUrl);
								URL baseMapUrl = new URL(baseMapWMSUrl);
								WebMapServer wms = new WebMapServer(baseMapUrl);

								GetMapRequest request = wms.createGetMapRequest();

								final String baseMapFormat = CadastrappPlaceHolder.getProperty("baseMap.format");

								request.setFormat(baseMapFormat);

								// Add layer see to set this in configuration
								// parameters
								// Or use getCapatibilities
								Layer layer = new Layer("OpenStreetMap : carte style 'google'");
								final String baseMapLayerName = CadastrappPlaceHolder.getProperty("baseMap.layer.name");

								layer.setName(baseMapLayerName);
								request.addLayer(layer);

								// sets the dimensions check with PDF size
								// available
								request.setDimensions((int) pdfImageWidth, (int) pdfImageHeight);
								final String baseMapSRS = CadastrappPlaceHolder.getProperty("baseMap.SRS");

								request.setSRS(baseMapSRS);

								// setBBox from Feature information
								request.setBBox(bounds);

								GetMapResponse baseMapResponse;

								baseMapResponse = (GetMapResponse) wms.issueRequest(request);
								logger.debug("Create basemap picture");
								baseMapImage = ImageIO.read(baseMapResponse.getInputStream());
								g2.drawImage(baseMapImage, 0, 0, null);
							} catch (ServiceException e) {
								logger.error("Error while getting basemap image, no basemap will be displayed on image", e);
							} catch (IOException e) {
								logger.error("Error while getting basemap image, no basemap will be displayed on image", e);
							}
						} else {
							logger.debug("No basemapurl given, non basemap will be add ");
						}

						drawPlot(g2, bufferGeometry, targetGeometry, (int) pdfImageHeight, (int) pdfImageWidth);
						logger.debug("Add Image to final picture");
						g2.drawImage(parcelleImage, 0, 0, null);
						drawCompass(g2, (int) pdfImageHeight, (int) pdfImageWidth);

						try {
							drawScale(g2, (int) pdfImageHeight, (int) pdfImageWidth, visibleLength);
						} catch (TransformException e) {
							logger.warn("Error while creating scale bar, no scale bar will be displayed on image", e);
						}

						g2.dispose();

						// Get temp folder from properties file
						final String tempFolder = CadastrappPlaceHolder.getProperty("tempFolder");

						File file = new File(tempFolder + File.separator + "BP-" + parcelle + ".png");
						//file.deleteOnExit();
						ImageIO.write(finalImage, "png", file);

						response = Response.ok((Object) file);
					} else {
						logger.info("No plots corresponding on WFS server");
					}
				} else {
					logger.error("Error when getting WFS feature source, please check configuration");
				}
			} catch (IOException e) {
				logger.error("Error while trying to init connection, please check configuration", e);
			}  catch (ServiceException e) {
				logger.error("Error while trying to connect to WMS server", e);
			} catch (CQLException e) {
				logger.error("Error while trying to create CQL filter", e);
			}
		} else {
			logger.info("No image can be generated with given input parameters");
		}

		return response.build();
	}

	/**
	 * Add North panel in the Upper Righ
	 * 
	 * @param g2
	 * @param imageHeight
	 * @param imageWidth
	 */
	private void drawCompass(Graphics2D g2, int imageHeight, int imageWidth) {

		logger.debug("Add compass ");

		// Draw N in the Upper Right
		g2.setColor(Color.white);
		g2.setFont(new Font("Times New Roman", Font.BOLD, 14));
		g2.drawString("N", imageWidth - 32, 22);

		// Draw an arrow in the Upper Right
		int xtr_left[] = { imageWidth - 32, imageWidth - 25, imageWidth - 25 };
		int ytr[] = { 44, 42, 27 };
		int xtr_right[] = { imageWidth - 19, imageWidth - 25, imageWidth - 25 };
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.fillPolygon(xtr_right, ytr, 3);
		g2.setStroke(new BasicStroke(1.0f));
		g2.drawPolygon(xtr_left, ytr, 3);
		g2.drawPolygon(xtr_right, ytr, 3);
	}

	/**
	 * Add a scale in bottom left of images
	 * 
	 * @param g2
	 * @param imageHeight
	 * @param imageWidth
	 * @param distanceVisible
	 * 
	 * @throws TransformException
	 */
	private void drawScale(Graphics2D g2, int imageHeight, double imageWidth, int distanceVisible) throws TransformException {

		logger.debug("Add scale ");

		if (distanceVisible > 0 && imageWidth > 0) {
			// define 1 pt size in meters
			final double pixelSize = distanceVisible / imageWidth;

			logger.debug("Pixels size :  " + pixelSize);
			logger.debug("Image width : " + imageWidth);
			logger.debug("Visible distance : " + distanceVisible);

			// Start x and y for scale bar
			final int scaleX = 50;
			final int scaleY = imageHeight - 10;

			// Width of the scalebar
			final int width = 100;
			final int divisionCount = 2;

			final double distance = width * pixelSize / divisionCount;
			final String unit = "mètres";
			final String Zmin = "0";
			final String Zmax = (int) distance + " " + unit;

			// Create grey global rectangle with label and scale bar in it
			g2.setColor(new Color(255, 255, 255, 127));
			g2.fill(new Rectangle2D.Double(scaleX - 5, scaleY - 23, width + 10, 23));

			final int scalebare = (int) Math.round(width / divisionCount);
			for (int i = 0; i < divisionCount; i++) {
				if (i % 2 == 0) {
					g2.setColor(new Color(83, 83, 83, 115));
				} else {
					g2.setColor(new Color(25, 25, 25, 175));
				}
				g2.setColor(new Color(83, 83, 83, 115));

				// Scalebar position
				g2.fill(new Rectangle2D.Double(scaleX + 5, scaleY - 10, scalebare, 5));
			}

			Font fnt = new Font("Verdana", Font.PLAIN, 11);
			FontMetrics fm = g2.getFontMetrics(fnt);
			final int fm_width = fm.stringWidth(Zmax);

			g2.setColor(Color.black);
			g2.setFont(fnt);

			g2.drawString(Zmin, scaleX, scaleY - 12);
			g2.drawString(Zmax, ((scaleX + width) - fm_width), scaleY - 12);
		} else {
			logger.warn("No scale bar can be create, wrong distance value given");
		}
	}

	/**
	 * Draw selected plot in blue
	 * 
	 * @param g2
	 * @param geometry
	 */
	private void drawPlot(Graphics2D g2d, Geometry bufferGeometry, Geometry originalGeometry, int imageHeight, int imageWidth) {
		
		logger.debug("Add selected feature ");
		if (bufferGeometry != null) {

			if (logger.isDebugEnabled()) {
				logger.debug("Geometry Type " + bufferGeometry.getGeometryType());
			}

			Envelope envelope = (Envelope) bufferGeometry.getEnvelopeInternal();
			
			AffineTransform translate= AffineTransform.getTranslateInstance(-envelope.getMinX(), -envelope.getMaxY());
			logger.debug("Translate to origin 0,0 :" + translate.toString());	
			
			AffineTransform scale= AffineTransform.getScaleInstance(imageWidth/(envelope.getMaxX()-envelope.getMinX()), imageHeight/ (envelope.getMinY()-envelope.getMaxY()));
			logger.debug("Scale to image size : " + scale.toString());	
			
		
			ShapeWriter sw = new ShapeWriter();
			
			// Geometry is can be a multipolygon and Java 1.7 awt does not display
			// GeometryCollectionShape, so we have to loop on each polygon
			for (int i = 0; i < originalGeometry.getNumGeometries(); i++) {
				Geometry g = (Geometry) originalGeometry.getGeometryN(i);
				
				if (logger.isDebugEnabled()) {
					logger.debug("Geometry n°: " + i + " of type " + g.getGeometryType());
				}

				Shape plot = sw.toShape(g);

				if (logger.isDebugEnabled()) {
					logger.debug("Before transalation");
					logger.debug("Shape width : " + plot.getBounds2D().getWidth());
					logger.debug("Shape heigh : " + plot.getBounds2D().getHeight());
					logger.debug("Shape MinX : " + plot.getBounds2D().getMinX());
					logger.debug("Shape MinY : " + plot.getBounds2D().getMinY());
					logger.debug("Shape MaxY : " + plot.getBounds2D().getMaxY());
					logger.debug("Shape MaxX : " + plot.getBounds2D().getMaxX());
				}
				plot = translate.createTransformedShape(plot);
		

				if (logger.isDebugEnabled()) {
					logger.debug("After transalation");
					logger.debug("Shape width : " + plot.getBounds2D().getWidth());
					logger.debug("Shape heigh : " + plot.getBounds2D().getHeight());
					logger.debug("Shape MinX : " + plot.getBounds2D().getMinX());
					logger.debug("Shape MinY : " + plot.getBounds2D().getMinY());
					logger.debug("Shape MaxY : " + plot.getBounds2D().getMaxY());
					logger.debug("Shape MaxX : " + plot.getBounds2D().getMaxX());
				}
				plot =scale.createTransformedShape(plot);
				
				if (logger.isDebugEnabled()) {
					logger.debug("After scale");
					logger.debug("Shape width : " + plot.getBounds2D().getWidth());
					logger.debug("Shape heigh : " + plot.getBounds2D().getHeight());
					logger.debug("Shape MinX : " + plot.getBounds2D().getMinX());
					logger.debug("Shape MinY : " + plot.getBounds2D().getMinY());
					logger.debug("Shape MaxY : " + plot.getBounds2D().getMaxY());
					logger.debug("Shape MaxX : " + plot.getBounds2D().getMaxX());
				}
				
				// draw in blue with transparence
				g2d.setColor(new Color(20, 255, 255, 128));
				g2d.draw(plot);
				g2d.setColor(new Color(20, 20, 255, 128));
				g2d.fill(plot);
			}

		} else {
			logger.error("No plot were given, cannot draw it on image");
		}

	}
}
