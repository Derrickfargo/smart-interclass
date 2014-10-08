package cn.com.incito.server.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import cn.com.incito.server.config.AppConfig;

public class NetworkUtils {

	/**
	 * 获取当前正在工作的mac地址
	 * 
	 */
	public static String getLocalMac() {
		try {
			InetAddress ia = InetAddress.getLocalHost();
			byte[] mac = NetworkInterface.getByInetAddress(ia)
					.getHardwareAddress();
			StringBuffer sb = new StringBuffer("");
			for (int i = 0; i < mac.length; i++) {
				if (i != 0) {
					sb.append("-");
				}
				int temp = mac[i] & 0xff;
				String str = Integer.toHexString(temp);
				if (str.length() == 1) {
					sb.append("0" + str);
				} else {
					sb.append(str);
				}
			}
			return sb.toString().toUpperCase();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 获得当前正在工作的ip地址
	 * @return
	 */
	public static String getLocalIp() {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			String ip = addr.getHostAddress().toString();// 获得本机IP
			System.out.println("IP  : " + ip);
			return ip;
		} catch (Exception e) {
			return "";
		}
	}

	public static void main(String[] args) {
		Properties props = AppConfig.getProperties();
		Set<Entry<Object, Object>> sets = props.entrySet();
		for (Entry<Object, Object> entry : sets) {
			System.out.println(entry.getKey() + "->" + entry.getValue());
		}
		props.put(AppConfig.CONF_LOCAL_IP, "127.0.0.1");
		AppConfig.setProperties(props);
	}

}
