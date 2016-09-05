package io.jee.alaska.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;

public class PropertiesUtils {
	
	private Properties properties = null;
	private File file;

	public static PropertiesUtils loadProperties(String propertiesFile) {
		Properties properties = null;
		PropertiesUtils propertiesUtils = new PropertiesUtils();
		try {
			ClassPathResource cr = new ClassPathResource(propertiesFile);
			InputStream is = cr.getInputStream();
			properties = new Properties();
			properties.load(new InputStreamReader(is, "UTF-8"));
			propertiesUtils.setProperties(properties);
			propertiesUtils.setFile(cr.getFile());
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return propertiesUtils;
	}

	public Map<String, String> toMap() {
		if (properties == null || properties.isEmpty()) {
			return null;
		}
		Map<String, String> result = new HashMap<String, String>();
		for (String name : properties.stringPropertyNames()) {
			result.put(name, properties.getProperty(name));
		}
		return result;
	}

	public String getValue(String key) {
		return properties.getProperty(key);
	}

	public String getRequiredValue(String key) {
		String value = properties.getProperty(key);
		Assert.hasText(value, "'"+key+"' must not be empty");
		return value;
	}
	
	public void writeData(String key, String value) { 
		try {
            OutputStream fos = new FileOutputStream(file);  
            properties.setProperty(key, value);  
            //保存，并加入注释  
            properties.store(fos, FormatUtils.formatDateNow());
            fos.close();  
		} catch (Exception e) {
			throw new RuntimeException("Failed to get properties!");
		}
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
