package cn.com.incito.socket.core;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import cn.com.incito.common.utils.LogUtil;
import cn.com.incito.socket.core.util.SendMessageUtil;
import cn.com.incito.socket.message.MessagePacking;

import com.alibaba.fastjson.JSONObject;

public class NMainHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)throws Exception {
		String message = (String) msg;
		JSONObject jsonObject = JSONObject.parseObject(message);
		MessagePacking messagePacking = JSONObject.parseObject(jsonObject.getString("messagePacking"), MessagePacking.class);

		byte msgId = messagePacking.msgId;
		LogUtil.d("收到服务器消息:消息ID:" + msgId + ":动作:" + MessageActionMap.getActionByMsgId(msgId));
		if (msgId == Message.MESSAGE_HEART_BEAT) {
			SendMessageUtil.sendHeartBeat();
		} else {
			messagePacking.getHandler().handleMessage(messagePacking.getJsonObject());
		}
	}
	
	/**
	 * 重写父类方法但是不抛出异常
	 * 将异常由自己进行处理
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
		LogUtil.e("网络IO出现异常,原因:" ,cause);
		ctx.close();
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt){
		/* 心跳处理 */
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.READER_IDLE) {
				/* 读超时 */
				throw new RuntimeException("心跳超时!");
			}
		}
	}
}
