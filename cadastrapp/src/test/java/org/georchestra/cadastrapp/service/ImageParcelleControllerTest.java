/**
 * 
 */
package org.georchestra.cadastrapp.service;


import javax.ws.rs.core.Response;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author gfi
 *
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "classpath:test-config.xml" })
public class ImageParcelleControllerTest {
	



	/**
	 * Test method for {@link org.georchestra.cadastrapp.service.ImageParcelleController#createImageBordereauParcellaire(java.lang.String)}.
	 */
	// @Test This more an integration test than a unit test
	public void testCreateImageBordereauParcellaire() {
		// Test realized on integration server with specific data
		// If you want to test this, change cadastrapp-test.properties with good value for WMS and WFS serveur information
		// and change value of parcelle id

		ImageParcelleController imageParcelleController = new ImageParcelleController();
		
		Response response = imageParcelleController.createImageBordereauParcellaire("2014630103000AO0351");


	}

}
