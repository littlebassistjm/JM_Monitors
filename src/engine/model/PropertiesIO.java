package engine.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesIO {

	private static PropertiesIO instance = null;
	
	private PropertiesIO(){}
	
	public static PropertiesIO getInstance(){ return instance == null ? (instance = new PropertiesIO()) : instance; }
	
	public Properties read(String file) throws IOException {
		Properties prop = new Properties();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();           
		InputStream stream = loader.getResourceAsStream(file);
		prop.load(stream);
		return prop;
	}
	
	public String read(String file, String property) throws IOException {
		Properties prop = new Properties();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();           
		InputStream stream = loader.getResourceAsStream(file);
		prop.load(stream);
		return prop.getProperty(property);
	}
}
