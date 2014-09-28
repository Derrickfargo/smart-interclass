package cn.com.incito.server.config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * 应用程序配置类：用于保存用户相关信息及设置
 * 
 * @author 刘世平
 * @version 1.0
 * @created 2014-9-21
 */
public class AppConfig {

	private final static String APP_CONFIG = "config/application.properties";

	public final static String CONF_IP = "ip";
	public final static String CONF_PORT = "port";

	private AppConfig() {

	}

	/**
	 * 获取Properties设置,每次都读
	 */
	public static Properties getProperties() {
		return new AppConfig().get();
	}

	private Properties get() {
		Properties props = new Properties();
		try {
			InputStream in = new FileInputStream(APP_CONFIG);
			props.load(in);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return props;
	}

	private void setProps(Properties p) {
		try {
			OutputStream in = new FileOutputStream(APP_CONFIG);
			p.store(in, null);
			in.flush();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String get(String key) {
		Properties props = get();
		return (props != null) ? props.getProperty(key) : null;
	}

	public void set(Properties ps) {
		Properties props = get();
		props.putAll(ps);
		setProps(props);
	}

	public void set(String key, String value) {
		Properties props = get();
		props.setProperty(key, value);
		setProps(props);
	}

	public void remove(String... key) {
		Properties props = get();
		for (String k : key)
			props.remove(k);
		setProps(props);
	}

}
