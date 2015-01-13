package cn.com.incito.server.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import cn.com.incito.interclass.po.Student;
import cn.com.incito.interclass.ui.MainFrame;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.SocketServiceCore;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;

import com.alibaba.fastjson.JSONObject;

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
		JSONObject json = new JSONObject();
		if(isLock){
			Application.getInstance().setLockScreen(true);
			json.put("isLock", "true");
			messagePacking.putBodyData(DataType.INT,json.toJSONString().getBytes());
		}else{
			Application.getInstance().setLockScreen(false);
			json.put("isLock", "false");
			messagePacking.putBodyData(DataType.INT,json.toJSONString().getBytes());
		}
		SocketServiceCore.getInstance().sendMsg(messagePacking);
		logger.info("锁屏信息发出");
	}
	
	public static void sendClassOverMessage(){
		logger.info("下课信息发出");
		final MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_LOCK_SCREEN);
		JSONObject json = new JSONObject();
		Application.getInstance().setLockScreen(false);
		json.put("isLock", "over");
		messagePacking.putBodyData(DataType.INT,json.toJSONString().getBytes());
		Application app = Application.getInstance();
		final Collection<ChannelHandlerContext> channels = app.getClientChannel().values();
		new Thread() {
			@Override
			public void run() {
				for (ChannelHandlerContext channel : channels) {
					if (channel != null && channel.channel().isActive()) {
						// 输出到通道
						JSONObject json = new JSONObject();
						json.put("messagePacking", messagePacking);
						ByteBuf buf = Unpooled.copiedBuffer((json.toString() + "$_").getBytes());
						if (channel != null && channel.channel().isActive()) {
							channel.writeAndFlush(buf);
						}
					}
				}
				System.exit(0);
			};
		}.start();
	}
	
	/**
	 * 
	 * 抢答命令
	 * @param doResponse true为发布抢答命令，false为抢答结束
	 */
	public static void sendResponderMessage(boolean doResponse) {
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_STUDENT_RESPONDER);
		if(doResponse){
//			sendLockScreenMessage(false)解锁屏
//			messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString("true"));//注意，如果使用请考虑else注释内容
//			CoreSocket.getInstance().sendMessage(messagePacking.pack().array());
			Application  app = Application.getInstance();
			Set<String> imeis = app.getOnlineDevice();
			List<ChannelHandlerContext> clientSocket = new ArrayList<ChannelHandlerContext>();
			for(String imei:imeis){
				List<Student> students = app.getStudentByImei(imei);
				for(Student stu:students){
					if(stu.isLogin()){
						clientSocket.add(app.getClientChannel().get(imei));
						break;
					}
				}
			}
			if(clientSocket.size()==0){
				JOptionPane.showInputDialog(MainFrame.getInstance(), "没有学生登录不能抢答！");
				return;
			}
			sendMsg(messagePacking, clientSocket);
			logger.info("抢答命令发出");
		}
		else{
//			messagePacking.putBodyData(DataType.INT,BufferUtils.writeUTFString("false")); //注意，如果使用请单独添加channel
//			sendLockScreenMessage(true);锁屏
			MessagePacking msg = new MessagePacking(Message.MESSAGE_RESPONDER_END);
			SocketServiceCore.getInstance().sendMsg(msg);
			logger.info("抢答结束命令发出");
		}
	}
	
	private static  void sendMsg(final MessagePacking msg,final List<ChannelHandlerContext > clientChannel){
		new Thread(){
			@Override
			public void run(){
				ByteBuffer buf =ByteBuffer.allocate(msg.pack().array().length);
				for(ChannelHandlerContext socketChannel:clientChannel){
					if(socketChannel!=null&&socketChannel.channel().isActive()){
						buf.clear();
						buf.put(msg.pack().array());
						buf.flip();
						socketChannel.writeAndFlush(buf);
						SocketServiceCore.getInstance().sendMsg(msg, socketChannel);
						logger.info("抢答信息发送成功\n");
					}
				}
			}
			
		}.start();;
	}
}
