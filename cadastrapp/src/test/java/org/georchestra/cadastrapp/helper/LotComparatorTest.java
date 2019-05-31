/**
 * 
 */
package org.georchestra.cadastrapp.helper;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.georchestra.cadastrapp.service.constants.CadastrappConstants;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author pierre.jego@jdev.fr
 *
 */
public class LotComparatorTest {
	
	static final Logger logger = LoggerFactory.getLogger(LotComparatorTest.class);
	
	public LotComparator lotComparator = new LotComparator();
	
	/**
	 * Test method for {@link org.georchestra.cadastrapp.helper.LotComparator#compare(java.util.Map, java.util.Map)}.
	 */
	@Test
	public void testEqualsNumbersCompare() {
		
		Map<String, Object> lotsInformation1 = new HashMap<String, Object>();
		lotsInformation1.put(CadastrappConstants.PB_LOT_ID, "001");
		
		Map<String, Object> lotsInformation2 = new HashMap<String, Object>();
		lotsInformation2.put(CadastrappConstants.PB_LOT_ID, "001");
		
		int result = lotComparator.compare(lotsInformation1, lotsInformation2);
		assertEquals(0, result);			
	}
	
	/**
	 * Test method for {@link org.georchestra.cadastrapp.helper.LotComparator#compare(java.util.Map, java.util.Map)}.
	 */
	@Test
	public void testEqualsNumberStringCompare() {
		
		Map<String, Object> lotsInformation1 = new HashMap<String, Object>();
		lotsInformation1.put(CadastrappConstants.PB_LOT_ID, "001");
		
		Map<String, Object> lotsInformation2 = new HashMap<String, Object>();
		lotsInformation2.put(CadastrappConstants.PB_LOT_ID, "001A");
		
		int result = lotComparator.compare(lotsInformation1, lotsInformation2);
		assertEquals(-1, result);		
	}
	
	
	/**
	 * Test method for {@link org.georchestra.cadastrapp.helper.LotComparator#compare(java.util.Map, java.util.Map)}.
	 */
	@Test
	public void testEqualsStringCompare() {
		
		Map<String, Object> lotsInformation1 = new HashMap<String, Object>();
		lotsInformation1.put(CadastrappConstants.PB_LOT_ID, "A");
		
		Map<String, Object> lotsInformation2 = new HashMap<String, Object>();
		lotsInformation2.put(CadastrappConstants.PB_LOT_ID, "A");
		
		int result = lotComparator.compare(lotsInformation1, lotsInformation2);
		assertEquals(0, result);		
	}
	
	/**
	 * Test method for {@link org.georchestra.cadastrapp.helper.LotComparator#compare(java.util.Map, java.util.Map)}.
	 */
	@Test
	public void testBiggerNbOne() {
		
		Map<String, Object> lotsInformation1 = new HashMap<String, Object>();
		lotsInformation1.put(CadastrappConstants.PB_LOT_ID, "002");
		
		Map<String, Object> lotsInformation2 = new HashMap<String, Object>();
		lotsInformation2.put(CadastrappConstants.PB_LOT_ID, "001");
		
		int result = lotComparator.compare(lotsInformation1, lotsInformation2);
		assertEquals(1, result);			
	}
	
	/**
	 * Test method for {@link org.georchestra.cadastrapp.helper.LotComparator#compare(java.util.Map, java.util.Map)}.
	 */
	@Test
	public void testBiggerNbTwo() {
		
		Map<String, Object> lotsInformation1 = new HashMap<String, Object>();
		lotsInformation1.put(CadastrappConstants.PB_LOT_ID, "011");
		
		Map<String, Object> lotsInformation2 = new HashMap<String, Object>();
		lotsInformation2.put(CadastrappConstants.PB_LOT_ID, "101");
		
		int result = lotComparator.compare(lotsInformation1, lotsInformation2);
		assertEquals(-1, result);			
	}

	
	/**
	 * Test method for {@link org.georchestra.cadastrapp.helper.LotComparator#compare(java.util.Map, java.util.Map)}.
	 */
	@Test
	public void testBiggerNumberStringCompare() {
		
		Map<String, Object> lotsInformation1 = new HashMap<String, Object>();
		lotsInformation1.put(CadastrappConstants.PB_LOT_ID, "01");
		
		Map<String, Object> lotsInformation2 = new HashMap<String, Object>();
		lotsInformation2.put(CadastrappConstants.PB_LOT_ID, "A");
		
		int result = lotComparator.compare(lotsInformation1, lotsInformation2);
		assertTrue(result < 0);	
	}
	
	/**
	 * Test method for {@link org.georchestra.cadastrapp.helper.LotComparator#compare(java.util.Map, java.util.Map)}.
	 */
	@Test
	public void testBiggerStringNumberCompare() {
		
		Map<String, Object> lotsInformation1 = new HashMap<String, Object>();
		lotsInformation1.put(CadastrappConstants.PB_LOT_ID, "A");
		
		Map<String, Object> lotsInformation2 = new HashMap<String, Object>();
		lotsInformation2.put(CadastrappConstants.PB_LOT_ID, "01");
		
		int result = lotComparator.compare(lotsInformation1, lotsInformation2);
		assertTrue(result > 0);				
	}
	
	/**
	 * Test method for {@link org.georchestra.cadastrapp.helper.LotComparator#compare(java.util.Map, java.util.Map)}.
	 */
	@Test
	public void testSort() {
		
		String[] dnulotValues = {"A", "10E", "01", "10", "10A", "10C", "00005", "10B", "B10" , "4"};
		String[] wantedSort = {"01", "4", "00005", "10", "10A", "10B", "10C", "10E","A", "B10" };
		
		List<Map<String, Object>> lots = new ArrayList<Map<String,Object>>();
		
		for(String dnulot: dnulotValues){
			Map<String, Object> lotsInformation = new HashMap<String, Object>();
			lotsInformation.put(CadastrappConstants.PB_LOT_ID, dnulot);
			lots.add(lotsInformation);
		}
			
		lots.sort(lotComparator);
		
		for (int i = 0; i < lots.size(); i++) {
			logger.debug("Dnulot : " + lots.get(i).get(CadastrappConstants.PB_LOT_ID));
			assertEquals(wantedSort[i], lots.get(i).get(CadastrappConstants.PB_LOT_ID));
		}		
	}

}
