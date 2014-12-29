package cn.com.incito.server.utils;

import java.awt.Color;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Collection;

import org.apache.log4j.Logger;

import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.CoreSocket;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;

public class UIHelper {
	static Logger logger =  Logger.getLogger(UIHelper.class.getName());
	public static Color getDefaultFontColor() {
		return new Color(Integer.parseInt("454545", 16));
	}
	/**
	 * 
	 * @param isLock true 为锁屏，false为锁屏
	 */
	public static void sendLockScreenMessage(boolean isLock){
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_LOCK_SCREEN);
		if(isLock){
			Application.getInstance().setLockScreen(true);
			messagePacking.putBodyData(DataType.INT,BufferUtils.writeUTFString("true"));
		}else{
			Application.getInstance().setLockScreen(false);
			messagePacking.putBodyData(DataType.INT,BufferUtils.writeUTFString("false"));
		}
		CoreSocket.getInstance().sendMessage(messagePacking.pack().array());
		logger.info("锁屏信息发出");
	}
	/**
	 * 下课命令
	 */
	public static void sendClassOverMessage(){
		logger.info("下课信息发出");
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_LOCK_SCREEN);
		Application.getInstance().setLockScreen(false);
		messagePacking.putBodyData(DataType.INT,BufferUtils.writeUTFString("over"));
		final byte[] data = messagePacking.pack().array();
		Application app = Application.getInstance();
		final Collection<SocketChannel> channels = app.getClientChannel().values();
		if (channels == null || channels.size() == 0) {
			System.exit(0);
		}
		new Thread() {
			@Override
			public void run() {
				try {
					ByteBuffer buffer = ByteBuffer.allocate(data.length);
					for(SocketChannel channel: channels){
						if(channel != null && channel.isConnected()){
							// 输出到通道
							buffer.clear();
							buffer.put(data);
							buffer.flip();
							channel.write(buffer);
						}
						System.exit(0);
					}
				} catch (IOException e) {
					logger.fatal("发送消息异常:\n" + e.getMessage());
				}
			};
		}.start();
	}
}
