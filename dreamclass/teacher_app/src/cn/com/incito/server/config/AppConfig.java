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

	public final static String CONF_SERVER_IP = "server_ip";
	public final static String CONF_SERVER_PORT = "server_port";
	public final static String CONF_LOCAL_IP = "local_ip";
	
	public static Properties getProperties() {
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

	public static void setProperties(Properties p) {
		try {
			OutputStream in = new FileOutputStream(APP_CONFIG);
			p.store(in, null);
			in.flush();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
