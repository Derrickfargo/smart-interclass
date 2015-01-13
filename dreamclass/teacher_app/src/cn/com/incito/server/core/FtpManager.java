package cn.com.incito.server.core;

import java.io.File;

import org.apache.ftpserver.ConnectionConfigFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.log4j.Logger;

public class FtpManager {
	private static FtpManager instance = null;
	public static final String FTP_HOME = "ftp_home";
	private Logger logger = Logger.getLogger(FtpManager.class.getName());
	private FtpServer server;

	public static FtpManager createFtpServer() {
		if (instance == null) {
			instance = new FtpManager();
		}
		return instance;
	}

	private FtpManager() {
		File ftpHome = new File(FTP_HOME);
		if(!ftpHome.exists()){
			if (!ftpHome.mkdirs()) {
				logger.info("ftp_home目录创建失败");
			}
		}
		FtpServerFactory serverFactory = new FtpServerFactory();
		
		ListenerFactory factory = new ListenerFactory();
		factory.setPort(21);// 监听端口
		serverFactory.addListener("default", factory.createListener());// 默认监听器
		PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
		userManagerFactory.setFile(new File("./config/users.properties"));
		serverFactory.setUserManager(userManagerFactory.createUserManager());
		ConnectionConfigFactory connectionConfigFactory = new ConnectionConfigFactory();
		connectionConfigFactory.setLoginFailureDelay(10);
		connectionConfigFactory.setMaxLogins(2000);
		connectionConfigFactory.setMaxThreads(200);
		connectionConfigFactory.setMaxLoginFailures(500);
		serverFactory.setConnectionConfig(connectionConfigFactory.createConnectionConfig());
		server = serverFactory.createServer();
		
		
//		ConnectionConfig connectionConfigFactory=serverFactory.getConnectionConfig();
		System.out.println("MaxLogins()"+connectionConfigFactory.getMaxLogins()  );
		System.out.println("MaxAnonymousLogins()"+connectionConfigFactory.getMaxAnonymousLogins());
		System.out.println("MaxLoginFailures()"+connectionConfigFactory.getMaxLoginFailures());
		System.out.println("MaxThreads()"+connectionConfigFactory.getMaxThreads());
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
