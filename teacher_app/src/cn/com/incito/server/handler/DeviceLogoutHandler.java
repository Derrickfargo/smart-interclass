package cn.com.incito.server.handler;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import org.apache.log4j.Logger;

import cn.com.incito.server.core.MessageHandler;

/**
 * 设备退出
 * 
 * @author 刘世平
 * 
 */
public class DeviceLogoutHandler extends MessageHandler {
	private Logger logger = Logger.getLogger(DeviceLogoutHandler.class.getName());
	private String imei;

	@Override
	public void handleMessage() {
		imei = data.getString("imei");
		logger.info("收到设备退出消息，IMEI:" + imei);
		service.deviceLogout(imei);
		SocketChannel channel = message.getChannel();
		if (channel != null) {
			try {
				channel.close();
			} catch (IOException e) {
				
			}
		}
		
	}

}
