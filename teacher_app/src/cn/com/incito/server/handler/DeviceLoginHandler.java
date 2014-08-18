package cn.com.incito.server.handler;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import cn.com.incito.interclass.po.Device;
import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Table;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.JSONUtils;

/**
 * 握手消息处理器
 * 
 * @author 刘世平
 * 
 */
public class DeviceLoginHandler extends MessageHandler {
	private Logger logger = Logger.getLogger(DeviceLoginHandler.class.getName());
	private String imei;

	@Override
	public void handleMessage() {
		imei = data.getString("imei");
		logger.info("收到设备登陆消息，IMEI:" + imei);
		service.deviceLogin(imei);
		Application app = Application.getInstance();
		app.addSocketChannel(imei, message.getChannel());
		Device device = app.getImeiDevice().get(imei);
        if (device != null) {
            // 系统中无此设备
        	Table table = app.getDeviceTable().get(device.getId());
        	if (table != null) {
        		Group group = app.getTableGroup().get(table.getId());
				if (group != null) {
        			app.addSocketChannel(group.getId(), message.getChannel());
        		}
        	}
        }
        
		
		//回复握手消息
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_HAND_SHAKE);
		byte[] handShakResponse = messagePacking.pack().array();
        ByteBuffer buffer = ByteBuffer.allocate(handShakResponse.length);
        buffer.put(handShakResponse);
        buffer.flip();
        try {
			message.getChannel().write(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}