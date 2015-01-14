package cn.com.incito.server.handler;

import java.util.Properties;

import org.apache.log4j.Logger;

import cn.com.incito.interclass.po.Device;
import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Table;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.config.AppConfig;
import cn.com.incito.server.core.DeviceConnectionManager;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.core.SocketServiceCore;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;

/**
 * 设备登陆消息处理器
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
		logger.info("收到设备登陆消息:" + data.toJSONString()+ctx.channel().remoteAddress());
		service.deviceLogin(imei);
		DeviceConnectionManager.notificate(imei, ctx);
		Application app = Application.getInstance();
		app.addSocketChannel(imei, ctx);
		Device device = app.getImeiDevice().get(imei);
        if (device != null) {
            // 系统中无此设备
        	Table table = app.getDeviceTable().get(device.getId());
        	if (table != null) {
        		Group group = app.getTableGroup().get(table.getId());
				if (group != null) {
        			app.addSocketChannel(group.getId(), ctx);
        		}
        	}
        }
		
		//回复设备登陆消息
		Properties props = AppConfig.getProperties();
		String ip = props.get(AppConfig.CONF_SERVER_IP).toString();
		String port = props.get(AppConfig.CONF_SERVER_PORT).toString();
		data.put(AppConfig.CONF_SERVER_IP, ip);
		data.put(AppConfig.CONF_SERVER_PORT, port);
        logger.info("回复设备登陆消息:" + data.toJSONString());
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_HAND_SHAKE);
		messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(data.toJSONString()));
//		byte[] handShakResponse = messagePacking.pack().array();
//        ByteBuffer buffer = ByteBuffer.allocate(handShakResponse.length);
//        buffer.put(handShakResponse);
//        buffer.flip();
//        ctx.channel().writeAndFlush(buffer);
        SocketServiceCore.getInstance().sendMsg(messagePacking, ctx);
	}

}
