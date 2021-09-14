/**
 * 
 */
package org.georchestra.cadastrapp.service;


import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Pierre Jégo
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
		
		ResponseEntity<File> response = imageParcelleController.createImageBordereauParcellaire("2014630103000AO0351", 0, "#1446DE", (float) 0.50, "#10259E",2);


	}

}
