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

import org.geotools.data.ows.Layer;
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
import org.geotools.ows.ServiceException;
import org.opengis.filter.Filter;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.geometry.BoundingBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageParcelleController extends CadController {

	final static Logger logger = LoggerFactory.getLogger(ImageParcelleController.class);
	
	final private String URL_GET_CAPABILITIES="?REQUEST=GetCapabilities&version=1.0.0";
	
	final private String URL_GET_CAPABILITIES_WMS="?VERSION=1.1.1&Request=GetCapabilities&Service=WMS";
	
	final private String CONNECTION_PARAM = "WFSDataStoreFactory:GET_CAPABILITIES_URL";

	@GET
	@Path("/getImageBordereau")
	@Produces("image/png")
	public Response createImageBordereauParcellaire(@QueryParam("parcelle") String parcelle) throws IOException, ServiceException, CQLException {

		ResponseBuilder response = Response.noContent();

		if (parcelle != null && parcelle.length() > 14) {

			BufferedImage baseMapImage;
			BufferedImage parcelleImage;
			BoundingBox bounds;

			// Get parcelle geo information
			// get featureById
			logger.debug("Appel WFS avec la parcelle Id");
			String getCapabilities = wfsUrl + URL_GET_CAPABILITIES;
			
			Map<String, String> connectionParameters = new HashMap<String, String>();
			connectionParameters.put(CONNECTION_PARAM, getCapabilities);
			WFSDataStoreFactory dsf = new WFSDataStoreFactory();

			WFSDataStore dataStore = dsf.createDataStore(connectionParameters);
			if (logger.isDebugEnabled()){
				logger.debug("DataStore information : " + dataStore.getInfo());
			}
			
			SimpleFeatureSource source = dataStore.getFeatureSource(cadastreLayerName);
			
			if (source != null) {
				Filter filter = CQL.toFilter(cadastreLayerIdParcelle + " = '" + parcelle + "'");
				SimpleFeatureCollection collection = source.getFeatures(filter);

				SimpleFeatureIterator it = collection.features();
				if (it.hasNext()) {
					SimpleFeature parcelleFeature = it.next();

					bounds = parcelleFeature.getBounds();

					// Ajout de la parcelle
					logger.debug("Appel WMS pour la parcelle");
					// Get basemap image with good BBOX
					URL parcelleWMSUrl = new URL(wmsUrl + URL_GET_CAPABILITIES_WMS);
					WebMapServer wmsParcelle = new WebMapServer(parcelleWMSUrl);

					GetMapRequest requestParcelle = wmsParcelle.createGetMapRequest();
					requestParcelle.setFormat(cadastreFormat);

					// Add layer see to set this in configuration parameters
					// Or use getCapatibilities
					Layer layerParcelle = new Layer("Parcelle cadastrapp");
					layerParcelle.setName(cadastreLayerName);
					requestParcelle.addLayer(layerParcelle);

					// sets the dimensions check with PDF size available
					requestParcelle.setDimensions(pdfImageWidth, pdfImageHeight);
					requestParcelle.setSRS(cadastreSRS);
					requestParcelle.setTransparent(true);
					// setBBox from Feature information
					requestParcelle.setBBox(bounds);

					GetMapResponse parcelleResponse = (GetMapResponse) wmsParcelle.issueRequest(requestParcelle);
					parcelleImage = ImageIO.read(parcelleResponse.getInputStream());

					logger.debug("Appel WMS pour le fond de carte");
					// Get basemap image with good BBOX
					URL baseMapUrl = new URL(baseMapWMSUrl);
					WebMapServer wms = new WebMapServer(baseMapUrl);

					GetMapRequest request = wms.createGetMapRequest();
					request.setFormat(baseMapFormat);

					// Add layer see to set this in configuration parameters
					// Or use getCapatibilities
					Layer layer = new Layer("OpenStreetMap : carte style 'google'");
					layer.setName(baseMapLayerName);
					request.addLayer(layer);

					// sets the dimensions check with PDF size available
					request.setDimensions(pdfImageWidth, pdfImageHeight);
					request.setSRS(baseMapSRS);
					// setBBox from Feature information
					request.setBBox(bounds);

					GetMapResponse baseMapResponse = (GetMapResponse) wms.issueRequest(request);
					baseMapImage = ImageIO.read(baseMapResponse.getInputStream());

					logger.debug("Creation de l'image finale");
					BufferedImage finalImage = new BufferedImage(baseMapImage.getWidth(), baseMapImage.getHeight(), BufferedImage.TYPE_INT_ARGB);

					// 1. Dessin couches
					Graphics2D g2 = finalImage.createGraphics();

					g2.drawImage(baseMapImage, 0, 0, null);
					g2.drawImage(parcelleImage, 0, 0, null);

					drawCompass(g2, pdfImageHeight, pdfImageWidth);
					// drawScale(g2, )

					g2.dispose();
					// Add feature on basemap

					File file = new File(tempFolder + File.separator + "BP-" + parcelle + ".png");
					file.deleteOnExit();
					ImageIO.write(finalImage, "png", file);

					response = Response.ok((Object) file);
				} else {

					logger.info("Pas de parcelle correspondant à la demande");
				}
			} else {
				logger.error("Error when getting WFS feature source, please check configuration");
			}
		} else {
			logger.info("Paramètres d'appel incorrecte");
		}

		return response.build();
	}

	/**
	 * Add North panel in the Upper Right
	 * 
	 * @param g2
	 *            current Graphics2D
	 */
	private void drawCompass(Graphics2D g2, int imageHeight, int imageWidth) {

		logger.debug("Ajout de la boussole ");

		// Draw N in the Upper Right
		g2.setColor(Color.white);
		g2.setFont(new Font("Times New Roman", Font.BOLD, 18));
		// TODO Change value by parameter from method
		g2.drawString("N", imageHeight - 32, 22);

		// Draw an arrow in the Upper Right
		int xtr_left[] = { imageHeight - 43, imageHeight - 29, imageHeight - 29 };
		int ytr[] = { 53, 47, 26 };
		int xtr_right[] = { imageHeight - 15, imageHeight - 29, imageHeight - 29 };

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
	 *            current Graphics2D
	 */
	private void drawScale(Graphics2D g2, String scaleValue) {

		logger.debug("Ajout de l'echelle ");

		// Dessin de l'echelle
		String zoom_[] = scaleValue.split(",");
		String Zdistance = zoom_[0];
		Integer ZdivisionCount = Integer.parseInt(zoom_[1]);
		String ZuiSymbol = zoom_[2];
		Integer Zwidth = Integer.parseInt(zoom_[4]);

		g2.setColor(new Color(255, 255, 255, 127));
		g2.fill(new Rectangle2D.Double(125, 567, Zwidth + 10, 23));

		int Zbare = (int) Math.round(Zwidth / ZdivisionCount);
		for (int i = 0; i < ZdivisionCount; i++) {
			if (i % 2 == 0) {
				g2.setColor(new Color(83, 83, 83, 115));
			} else {
				g2.setColor(new Color(25, 25, 25, 175));
			}
			g2.fill(new Rectangle2D.Double(130 + Zbare * i, 580, Zbare, 5));
		}

		String Zmin = "0";
		String Zmax = Zdistance + " " + ZuiSymbol;
		Font fnt = new Font("Verdana", Font.PLAIN, 11);
		FontMetrics fm = g2.getFontMetrics(fnt);
		int fm_width = fm.stringWidth(Zmax);

		g2.setColor(Color.black);
		g2.setFont(fnt);
		g2.drawString(Zmin, 130, 578);
		g2.drawString(Zmax, ((130 + Zwidth) - fm_width), 578);
	}
}
