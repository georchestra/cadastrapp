package org.georchestra.cadastrapp.service;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.xml.transform.TransformerException;

import org.georchestra.cadastrapp.configuration.CadastrappPlaceHolder;
import org.geotools.data.ows.HTTPClient;
import org.geotools.data.ows.Layer;
import org.geotools.data.ows.SimpleHttpClient;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.wfs.impl.WFSContentDataStore;
import org.geotools.data.wfs.impl.WFSDataStoreFactory;
import org.geotools.data.wms.WebMapServer;
import org.geotools.data.wms.request.GetMapRequest;
import org.geotools.data.wms.response.GetMapResponse;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.geometry.jts.JTS;
import org.geotools.ows.ServiceException;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.SLDTransformer;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayerDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

/**
 * Image Parcelle Controller
 * 
 * @author gfi
 *
 */
public class ImageParcelleController extends CadController {

	static final Logger logger = LoggerFactory.getLogger(ImageParcelleController.class);

	private final String URL_GET_CAPABILITIES = "?REQUEST=GetCapabilities&version=1.0.0";
	private final String URL_GET_CAPABILITIES_WMS = "?VERSION=1.1.1&Request=GetCapabilities&Service=WMS";
	private final String GET_CAPABILITIES_URL_PARAM = "WFSDataStoreFactory:WFS_GET_CAPABILITIES_URL";
	private final String USERNAME_PARAM = "WFSDataStoreFactory:USERNAME";
	private final String PASSWORD_PARAM = "WFSDataStoreFactory:PASSWORD";
	
	// buffer ratio
	final private double MAX_PERIMETER = 2000;

	/**
	 * Using a given parcelle id, this service will get feature from WFS
	 * service, get Basemap image using Boundingbox of the feature and add
	 * compass and scale on it
	 * 
	 * @param parcelle
	 *            parcelle ID
	 * 
	 * @return Response with noContent in case of error, png otherwise
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
			BufferedImage baseMapImage;
			BufferedImage parcelleImage;
			BufferedImage backGroundParcelleImage;
			BoundingBox bounds;

			// Prepare WFS call
			final String wfsUrl = CadastrappPlaceHolder.getProperty("cadastre.wfs.url");

			String getCapabilities = wfsUrl + URL_GET_CAPABILITIES;
			
			logger.debug("Call WFS with plot Id " + parcelle + " and WFS URL : " + getCapabilities);

			Map<String, Serializable> connectionParameters = new HashMap<String, Serializable>();
			connectionParameters.put(GET_CAPABILITIES_URL_PARAM, getCapabilities);
			
			// Add basic authent parameter if not empty
			final String cadastreWFSUsername = CadastrappPlaceHolder.getProperty("cadastre.wfs.username");
			final String cadastreWFSPassword = CadastrappPlaceHolder.getProperty("cadastre.wfs.password");
			if (cadastreWFSUsername != null && !cadastreWFSUsername.isEmpty()){
				connectionParameters.put(USERNAME_PARAM, cadastreWFSUsername);
				connectionParameters.put(PASSWORD_PARAM, cadastreWFSPassword);
			}
			
			WFSDataStoreFactory dsf = new WFSDataStoreFactory();
			WFSContentDataStore dataStore;

			try {
				dataStore = dsf.createDataStore(connectionParameters);

				SimpleFeatureSource source;

				String cadastreWFSLayerNameOri = CadastrappPlaceHolder.getProperty("cadastre.wfs.layer.name");
				
				// remove this if not using gt-wfs-ng anymore
				// using ng extension : need to be changed by_
				String cadastreWFSLayerName = cadastreWFSLayerNameOri.replaceFirst(":", "_");
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

						// In our case we want a square
						int dpi=Integer.parseInt(CadastrappPlaceHolder.getProperty("pdf.dpi"));
						// Wanted image size in mm
						int pdfImageMMSize = Integer.parseInt(CadastrappPlaceHolder.getProperty("pdf.imageSize"));				
						double pdfImagePixelSize = (pdfImageMMSize*dpi)/25.4;
						double nbPixelFor1Cm=(10*dpi)/25.4;
						
						// Get only the first plot
						SimpleFeature parcelleFeature = it.next();
						
						bounds = parcelleFeature.getBounds();
						CoordinateReferenceSystem crs = bounds.getCoordinateReferenceSystem();
				
						Geometry targetGeometry = (Geometry) parcelleFeature.getDefaultGeometry();
						// In case of buffering Error, set default value
						Geometry bufferGeometry = targetGeometry;
						
						// If CRS null
						if (crs == null || targetGeometry == null) {
							logger.error("CRS not known by geotools, no buffering can be made, scale won't be seeing on image");

						} else {
							if(logger.isDebugEnabled()){
								logger.debug("CRS : " + crs);
								logger.debug("Create buffer");
								logger.debug("Perimeter : " + targetGeometry.getLength());
								logger.debug("Max visible distance in meter before buffer : " + getMaxDistanceVisible(bounds));								
							}
								
							// Use the centroid enable us to have a square bbbox, so no deformation
							Point pt = targetGeometry.getCentroid();
							
							// Calculate optimal buffer distance using AREA
							// see #320 and add  1/2000 as minimum
							double perimeterRatio;
							final int minScale = Integer.parseInt(CadastrappPlaceHolder.getProperty("pdf.min.scale"));											
							final int maxDistanceVisible = getMaxDistanceVisible(bounds);
							final double meterByPixel = (maxDistanceVisible) / pdfImagePixelSize;
							
							// check current scale information
							// If for 1 centimeter it's less than given scale in property file
							if((nbPixelFor1Cm*meterByPixel)<minScale){
								logger.debug("Current Scale : " + nbPixelFor1Cm*meterByPixel);
								logger.debug("Min Scale : " + minScale);
								bufferGeometry = pt.buffer(maxDistanceVisible + (maxDistanceVisible * (minScale / (nbPixelFor1Cm*meterByPixel))/2));
							}else if(targetGeometry.getLength() < MAX_PERIMETER){
								perimeterRatio = Double.parseDouble(CadastrappPlaceHolder.getProperty("pdf.ratio.mediumScale"));
								bufferGeometry = pt.buffer(maxDistanceVisible + perimeterRatio * targetGeometry.getLength());
							}else{
								perimeterRatio = Double.parseDouble(CadastrappPlaceHolder.getProperty("pdf.ratio.bigScale"));;
								bufferGeometry = pt.buffer(maxDistanceVisible + perimeterRatio * targetGeometry.getLength());
							}
			
							// transform JTS enveloppe to geotools enveloppe
							Envelope envelope = bufferGeometry.getEnvelopeInternal();
							bounds = JTS.getEnvelope2D(envelope, crs);

							// Get distance beetween two point here bounds is used
							Coordinate lowerLeftCorner = new Coordinate(bounds.getMinX(), bounds.getMinY());
							Coordinate lowerRightCorner = new Coordinate(bounds.getMaxX(), bounds.getMinY());

							try {
								double bblength = JTS.orthodromicDistance(lowerLeftCorner, lowerRightCorner, crs);
								visibleLength = (int) bblength;						
								logger.debug("Bounding box length : " + visibleLength + " meters");							
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
						if (cadastreWMSUsername != null && !cadastreWMSUsername.isEmpty()
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
						requestParcelle.setDimensions((int) pdfImagePixelSize, (int)pdfImagePixelSize);
						final String cadastreSRS = CadastrappPlaceHolder.getProperty("cadastre.SRS");
						requestParcelle.setSRS(cadastreSRS);
						requestParcelle.setTransparent(true);
						
						// setBBox from Feature information
						requestParcelle.setBBox(bounds);
												
						logger.debug("Create background plots image");
						GetMapResponse parcelleResponse = (GetMapResponse) wmsParcelle.issueRequest(requestParcelle);			
						backGroundParcelleImage = ImageIO.read(parcelleResponse.getInputStream());
						
						logger.debug("Create feature image from WMS");
						
						// generate SLD with parcelle id, fill and stroke properties
						org.geotools.styling.StyleFactory sf = CommonFactoryFinder.getStyleFactory();
						FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
						StyledLayerDescriptor sld = sf.createStyledLayerDescriptor();				
						NamedLayer namedLayer = sf.createNamedLayer();
						namedLayer.setName(cadastreWFSLayerNameOri);

					    /// create a "user defined" style
						// TODO get Value from configuration file
						Stroke stroke = sf.stroke(ff.literal("#10259E"), ff.literal(1), ff.literal(2), null, null, null, null);
					    Fill fill = sf.fill(null, ff.literal("#1446DE"), ff.literal(0.50));
						PolygonSymbolizer sym = sf.createPolygonSymbolizer(stroke, fill, null);
				        
					    Rule rule1 = sf.createRule();
					    rule1.setName("BP");
					    rule1.getDescription().setTitle("Borderau Parcellaire rule");
					    // To fit SLD generated by mapfishapp change default value for like
					    rule1.setFilter(ff.like(ff.property(cadastreLayerIdParcelle), "*"+parcelle+"*", "*", ".", "!"));
					    rule1.symbolizers().add(sym);
					    
					    FeatureTypeStyle fts = sf.createFeatureTypeStyle(new Rule[]{rule1});
				        Style style = sf.createStyle();
				        style.featureTypeStyles().add(fts);		    
					    namedLayer.addStyle(style);					    
					    sld.layers().add(namedLayer);

					    SLDTransformer styleTransform = new SLDTransformer();
				        styleTransform.setEncoding(Charset.forName("UTF-8"));
				        styleTransform.setIndentation(4);

						try {
							String gxml = styleTransform.transform(sld);
							logger.debug("Generated SLD : " + gxml);
							requestParcelle.setProperty(GetMapRequest.SLD_BODY, URLEncoder.encode(gxml, "UTF-8"));
							
						} catch (TransformerException e1) {
							logger.error("Error while generate SLD, No selection will be displayed on plot", e1);
						}	
					   
						GetMapResponse parcelleResponse2 = (GetMapResponse) wmsParcelle.issueRequest(requestParcelle);			
						parcelleImage = ImageIO.read(parcelleResponse2.getInputStream());

						logger.debug("Create final picture");
						// createFinal buffer with pdf image size and not with result from parcellResponse
						BufferedImage finalImage = new BufferedImage((int)pdfImagePixelSize, (int)pdfImagePixelSize, BufferedImage.TYPE_INT_ARGB);

						Graphics2D g2 = finalImage.createGraphics();

						// Add basemap only if parameter is defined
						final String baseMapWMSUrl = CadastrappPlaceHolder.getProperty("baseMap.wms.url");

						if (baseMapWMSUrl != null && baseMapWMSUrl.length() > 1) {
							// Get basemap image with good BBOX
							try {
								logger.debug("WMS call for basemap with URL : " + baseMapWMSUrl);
								URL baseMapUrl = new URL(baseMapWMSUrl);
								WebMapServer wms = null;
								
								// Add basic authent parameter if not empty
								final String baseMapWMSUsername = CadastrappPlaceHolder.getProperty("baseMap.wms.username");
								final String baseMapWMSPassword = CadastrappPlaceHolder.getProperty("baseMap.wms.password");
								
								// if authentification is not null
								if (baseMapWMSUsername != null && !baseMapWMSUsername.isEmpty()
										&& baseMapWMSPassword != null && !baseMapWMSPassword.isEmpty()){
									
									HTTPClient httpClient = new SimpleHttpClient();
									httpClient.setUser(baseMapWMSUsername);
									httpClient.setPassword(baseMapWMSPassword);
									
									wms = new WebMapServer(baseMapUrl, httpClient);
								}else{
									wms = new WebMapServer(baseMapUrl);
								}

								GetMapRequest request = wms.createGetMapRequest();
								final String baseMapFormat = CadastrappPlaceHolder.getProperty("baseMap.format");
								request.setFormat(baseMapFormat);

								// Add layer see to set this in configuration
								// parameters
								// Or use getCapatibilities
								Layer layer = new Layer("BaseMap module cadastrapp");
								final String baseMapLayerName = CadastrappPlaceHolder.getProperty("baseMap.layer.name");

								layer.setName(baseMapLayerName);
								request.addLayer(layer);

								// sets the dimensions check with PDF size
								// available
								request.setDimensions((int) pdfImagePixelSize, (int) pdfImagePixelSize);
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

						
						logger.debug("Add Image to final picture");
						g2.drawImage(backGroundParcelleImage, 0, 0, null);
						g2.drawImage(parcelleImage, 0, 0, null);
						drawCompass(g2, (int) pdfImagePixelSize);

						try {
							drawScale(g2, (int) pdfImagePixelSize, (int) pdfImagePixelSize, visibleLength);
						} catch (TransformException e) {
							logger.warn("Error while creating scale bar, no scale bar will be displayed on image", e);
						}

						g2.dispose();

						// Get temp folder from properties file
						final String tempFolder = CadastrappPlaceHolder.getProperty("tempFolder");

						File file = new File(tempFolder + File.separator + "BP-" + parcelle + ".png");
						file.deleteOnExit();
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
	 * Get the longest visible size on the given boundingbox in meter.
	 * 
	 * @param bounds BoundingBox the geometry bounding
	 * 
	 * @return int the  max size in meter depending bb
	 */
	private int getMaxDistanceVisible(BoundingBox bounds){
		
		CoordinateReferenceSystem crs = bounds.getCoordinateReferenceSystem();
		Coordinate lowerLeftCorner = new Coordinate(bounds.getMinX(), bounds.getMinY());
		Coordinate lowerRightCorner = new Coordinate(bounds.getMaxX(), bounds.getMinY());
		Coordinate upperLeftCorner = new Coordinate(bounds.getMinX(), bounds.getMaxY());

		double bblength, bbheight;
		int maxVisibleSize = 0;
		try {
			bblength = JTS.orthodromicDistance(lowerLeftCorner, lowerRightCorner, crs);
			bbheight = JTS.orthodromicDistance(lowerLeftCorner, upperLeftCorner, crs);

			maxVisibleSize = Math.max((int) bblength, (int)bbheight);	
						
		} catch (TransformException e) {
			logger.error("Error during plots size measurement", e);
		}
		return maxVisibleSize;
	};


	/**
	 * Add North panel in the upper right
	 * 
	 * @param g2
	 * @param imageWidth
	 */
	private void drawCompass(Graphics2D g2, int imageWidth) {

		logger.debug("Add compass ");

		// Draw N in the Upper Right
		g2.setColor(Color.black);
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

}
