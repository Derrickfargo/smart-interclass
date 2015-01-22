package cn.com.incito.socket.core;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.ui.activity.WaitingActivity;
import cn.com.incito.common.utils.AndroidUtil;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;

import com.alibaba.fastjson.JSONObject;

public class NMainHandler extends ChannelInboundHandlerAdapter {

	/**
	 * 读取数据
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)throws Exception {
		String message = (String) msg;
		JSONObject jsonObject = JSONObject.parseObject(message);
		MessagePacking messagePacking = JSONObject.parseObject(jsonObject.getString("messagePacking"), MessagePacking.class);

		byte msgId = messagePacking.msgId;
		
		if (msgId == Message.MESSAGE_HEART_BEAT) {
			MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+ "NMainHandler:收到服务器心跳消息" + ctx.channel().remoteAddress().toString().substring(1));
			MessagePacking packing = new MessagePacking(Message.MESSAGE_HEART_BEAT);
			NCoreSocket.getInstance().sendMessage(packing);
		} else {
			MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+ ":NMainHandler:收到服务器消息:msgId:" + msgId);
			if(messagePacking.getJsonObject() == null){
				MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+ ":NMainHandler:收到服务器消息:只有消息ID没有数据:msgId:" + msgId);
			}
			messagePacking.getHandler().handleMessage(messagePacking.getJsonObject());
		}
	}

	/**
	 * 此方法代表连接已经建立成功
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+ ":NMainHandler:与服务器连接建立成功,发送设备登录消息!");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("imei", MyApplication.deviceId);
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_HAND_SHAKE);
		messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(jsonObject.toJSONString()));
		NCoreSocket.getInstance().sendMessage(messagePacking);
	}
	
	/**
	 * 重写父类方法但是不抛出异常
	 * 将异常由自己进行处理
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+ ":NMainHandler:由于出现异常我将主动关闭通道,出现异常原因:" + cause.getMessage());
		ctx.close();
		WaitingActivity waitingActivity = UIHelper.getInstance().getWaitingActivity();
		if(waitingActivity != null){
			MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + ":NMainHandler:由于异常改变本pad的所有学生的状态!");
			waitingActivity.notifyStudentOffline();
		}
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt){
		/* 心跳处理 */
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.READER_IDLE) {
				/* 读超时 */
				MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+ ":NMainHandler:长时间没有收到服务器心跳,马上自动关闭连接!");
				throw new RuntimeException("心跳超时!");
			}
		}
	}
}
