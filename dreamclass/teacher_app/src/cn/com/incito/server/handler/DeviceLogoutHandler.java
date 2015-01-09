package cn.com.incito.server.handler;

import org.apache.log4j.Logger;

import cn.com.incito.server.core.ConnectionManager;
import cn.com.incito.server.core.DeviceConnectionManager;
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
		logger.info("收到设备退出消息:" + data.toJSONString());
		
		//设备退出
		DeviceConnectionManager.quit(imei);
		
	}

}
