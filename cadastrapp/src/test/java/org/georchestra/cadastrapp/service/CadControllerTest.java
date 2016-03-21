/**
 * 
 */
package org.georchestra.cadastrapp.service;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.georchestra.cadastrapp.service.pdf.BordereauParcellaireHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author gfi
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-config.xml" })
public class CadControllerTest {
	
	@Autowired
	private BordereauParcellaireHelper cadController;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.georchestra.cadastrapp.service.CadController#isMandatoryParameterValid(java.lang.String)}.
	 */
	@Test
	public void testIsMandatoryParameterValid() {
		
		Assert.assertFalse(cadController.isMandatoryParameterValid(null));
		Assert.assertFalse(cadController.isMandatoryParameterValid(""));
		Assert.assertTrue(cadController.isMandatoryParameterValid("valid"));
		
	}

	/**
	 * Test method for {@link org.georchestra.cadastrapp.service.CadController#checkAreMandatoryParametersValid(java.util.List)}.
	 */
	@Test
	public void testCheckAreMandatoryParametersValid() {
		
		Assert.assertFalse(cadController.checkAreMandatoryParametersValid(null));
		
		ArrayList<String> testList = new ArrayList<String>();
		Assert.assertFalse(cadController.checkAreMandatoryParametersValid(testList));
		
		testList.add("test");
		Assert.assertTrue(cadController.checkAreMandatoryParametersValid(testList));
		
		testList.add(null);
		Assert.assertFalse(cadController.checkAreMandatoryParametersValid(testList));
	}


}
