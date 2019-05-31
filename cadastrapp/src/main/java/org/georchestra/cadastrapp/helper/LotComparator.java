package org.georchestra.cadastrapp.helper;

import java.util.Comparator;
import java.util.Map;

import org.georchestra.cadastrapp.service.constants.CadastrappConstants;

/**
 * 
 * @author pierre.jego@jdev.fr
 *
 */
public class LotComparator implements Comparator<Map<String, Object>>{
	
	private final String numberRegex = "^[0-9]*$";
	private final String numberStartRegex = "^[0-9]";

	/**
	 * Compare map using dnulot value
	 *  At we wanted a naturalOrder but we could not use 1.8 jdk version
	 *  This is a really simple implementation for our specific case
	 */
	public int compare(Map<String, Object> o1, Map<String, Object> o2) {
		
		int compareValue = 0;
		
		// Get dnulot information
		String dnulot1 = (String)o1.get(CadastrappConstants.PB_LOT_ID);
		String dnulot2 = (String)o2.get(CadastrappConstants.PB_LOT_ID);
		
		// If contains only number		
		if(dnulot1.matches(numberRegex) && dnulot2.matches(numberRegex)){
			compareValue = Integer.valueOf(dnulot1).compareTo(Integer.valueOf(dnulot2));
		}
		// If both start by number, but finish with char
		else if(dnulot1.matches(numberStartRegex) && dnulot2.matches(numberStartRegex)){	
			compareValue = Integer.valueOf(dnulot1.replaceAll("[^\\d]", "")).compareTo(Integer.valueOf(dnulot2.replaceAll("[^\\d]", "")));
			// If same number compare end string value
			if(compareValue == 0){
				compareValue = dnulot1.replaceAll("[0-9]", "").compareTo(dnulot2.replaceAll("[0-9]", ""));
			}
		}
		// If one start with number and the other is char
		else if(dnulot1.matches(numberStartRegex)){	
			compareValue = -1;
		}
		// If one is char and the other start by number
		else if(dnulot2.matches(numberStartRegex)){	
			compareValue = 1;
		}
		// All other case
		else{
			compareValue = dnulot1.compareTo(dnulot2);
		}
		
		return compareValue;
	}

}
