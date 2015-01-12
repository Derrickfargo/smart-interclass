package cn.com.incito.socket.core;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.common.utils.AndroidUtil;
import cn.com.incito.socket.message.MessagePacking;

import com.alibaba.fastjson.JSONObject;

public class NMainHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)throws Exception {
		String message = (String) msg;
		
		JSONObject jsonObject = JSONObject.parseObject(message);
		MessagePacking messagePacking = JSONObject.parseObject(jsonObject.getString("messagePacking"), MessagePacking.class);

		byte msgId = messagePacking.msgId;
		if (msgId == Message.MESSAGE_HEART_BEAT) {
			MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+ "NMainHandler:收到服务器心跳消息");
			MessagePacking packing = new MessagePacking(Message.MESSAGE_HEART_BEAT);
			NCoreSocket.getInstance().sendMessage(packing);
		} else {
			MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+ "NMainHandler:收到服务器消息");
			messagePacking.getHandler().handleMessage(messagePacking.getJsonObject());
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		MyApplication.getInstance().setChannelHandlerContext(ctx);
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + ":NMainHandler:连接上服务器");
		
		//停顿3s后进行重连连接
		Thread.sleep(3000);
		
		NCoreSocket.getInstance().startConnection(Constants.IP, Constants.PORT);
		
	}

	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		MyApplication.getInstance().setChannelHandlerContext(null);
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+ ":NMainHandler:断开连接");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+ ":NMainHandler:出现异常,原因:" + cause.getMessage());
		ctx.close();
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		/* 心跳处理 */
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.READER_IDLE) {
				/* 读超时 */
				MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+ ":NMainHandler:长时间没有收到心跳,退出");
				ctx.close();
			}
		}
	};
}
