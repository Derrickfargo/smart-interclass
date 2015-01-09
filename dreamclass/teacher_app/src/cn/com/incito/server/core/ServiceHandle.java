package cn.com.incito.server.core;

import org.apache.log4j.Logger;

import cn.com.incito.server.message.MessagePacking;

import com.alibaba.fastjson.JSONObject;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServiceHandle extends ChannelInboundHandlerAdapter{

	private Logger log =Logger.getLogger(ServiceHandle.class.getName());
	
	@Override
	public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception{
		String message = (String) msg;
		JSONObject json = JSONObject.parseObject(message);
		MessagePacking messagePacking = JSONObject.parseObject(json.getString("messagePacking"), MessagePacking.class);
		byte msgID	=	messagePacking.msgId;
		MessageHandler msgHandler	=	MessageHandlerResource.getHandlerResources().getMessageHandler(msgID);
		msgHandler.setChannelHandlerContext(ctx);
		messagePacking.setMessageHandler(msgHandler);
		MessageParserCore.getInstance().addMsgQue(messagePacking);//压入消息队列
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
		log.error("Message强制转换失败:	", cause);
	}
}
