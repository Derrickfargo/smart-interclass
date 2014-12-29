package cn.com.incito.server.core;

import java.io.File;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;

public class FtpManager {
	private static FtpManager instance = null;
	public static final String FTP_HOME = "ftp_home";
	private FtpServer server;

	public static FtpManager createFtpServer() {
		if (instance == null) {
			instance = new FtpManager();
		}
		return instance;
	}

	private FtpManager() {
		FtpServerFactory serverFactory = new FtpServerFactory();
		ListenerFactory factory = new ListenerFactory();
		factory.setPort(21);// 监听端口
		serverFactory.addListener("default", factory.createListener());// 默认监听器
		PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
		userManagerFactory.setFile(new File("users.properties"));
		serverFactory.setUserManager(userManagerFactory.createUserManager());
		server = serverFactory.createServer();
	}

	public void start() {
		try {
			server.start();
		} catch (FtpException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		server.stop();
	}

	public void resume() {
		server.resume();
	}

	public void suspend() {
		server.suspend();
	}
}
