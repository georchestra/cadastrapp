package org.georchestra.cadastrapp.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class CadastrappPlaceHolder extends PropertyPlaceholderConfigurer {

	private static Map<String, String> propertiesMap;
	
	// Default as in PropertyPlaceholderConfigurer
	private int springSystemPropertiesMode = SYSTEM_PROPERTIES_MODE_FALLBACK;

	@Override
	public void setSystemPropertiesMode(int systemPropertiesMode) {
		super.setSystemPropertiesMode(systemPropertiesMode);
		springSystemPropertiesMode = systemPropertiesMode;
	}

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) throws BeansException {
		super.processProperties(beanFactory, props);

		propertiesMap = new HashMap<String, String>();
		for (Object key : props.keySet()) {
			String keyStr = key.toString();
			String valueStr = resolvePlaceholder(keyStr, props, springSystemPropertiesMode);
			propertiesMap.put(keyStr, valueStr);
		}
	}

	/**
	 * Get String Value from property keys
	 * 
	 * @param name propertie key name
	 * @return String value of given key in properties files
	 */
	public static String getProperty(String name) {
		return propertiesMap.get(name);
	}
	
	/**
	 *  Get Map of values where key like given regex
	 *  
	 * @param regexKey regex use to get all keys
	 * @return all value where key like regexKey, null if no key matches
	 */
	public static List<Object> getPropertiesLike(String regexKey){
		
		List<Object> result = new ArrayList<Object>();
		for (Entry<String, String> entry : propertiesMap.entrySet()) {
			Map<String, Object> information = new HashMap<String, Object>();
			
			if (entry.getKey().matches(regexKey)) {
				information.put("key", entry.getKey());
				information.put("value", entry.getValue());
				result.add(information);
			}
		}
		return result;
	}
	
}
