package cn.com.incito.server.core;

import java.net.SocketAddress;

import org.apache.log4j.Logger;

import cn.com.incito.server.message.MessagePacking;

import com.alibaba.fastjson.JSONObject;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class SocketIdleHandle extends ChannelInboundHandlerAdapter{
	
	private Logger logger  = Logger.getLogger(SocketIdleHandle.class.getName()); 
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		final SocketAddress sa = ctx.channel().remoteAddress();
		logger.info(sa.toString() + "上线了!");
		ctx.fireChannelActive();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)throws Exception {	
		String message = (String) msg;
		logger.info("收到客户端心跳："+message+ctx.channel().remoteAddress());
		JSONObject jsonObject = JSONObject.parseObject(message);
		
		MessagePacking messagePacking = JSONObject.parseObject(jsonObject.getString("messagePacking"), MessagePacking.class);
		byte msgId = messagePacking.msgId;
		
		//如果是心跳消息则也向pad端发送心跳消息
		if(Message.MESSAGE_HEART_BEAT == msgId){
			MessagePacking packing = new MessagePacking(Message.MESSAGE_HEART_BEAT);
			JSONObject json = new JSONObject();
			json.put("messagePacking", packing);
			logger.info("收到pad端心跳包：	"+msgId+ctx.channel().remoteAddress());
			ctx.writeAndFlush((json.toJSONString()+"$_").getBytes());
			return;
		}
		ctx.fireChannelRead(msg);
	}
	
	/**
	 * 心跳处理
	 */
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)throws Exception {
		/*心跳处理*/
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                /*读超时*/
            	logger.info("服务端读超时，关闭该通道：" + ctx.channel().remoteAddress().toString() );
            	DeviceConnectionManager.quit(ctx);
                ctx.disconnect();
            } else if (event.state() == IdleState.WRITER_IDLE) {
            	logger.info("服务端写超时，不做处理");
            } else if (event.state() == IdleState.ALL_IDLE) {
            	//心跳
            	MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_HEART_BEAT);
            	JSONObject jsonObject = new JSONObject();
            	jsonObject.put("messagePacking", messagePacking);
            	ByteBuf buf = Unpooled.copiedBuffer((jsonObject.toJSONString()+"$_").getBytes());
                ctx.writeAndFlush(buf);
                logger.info("心跳包发送："+ctx.channel().remoteAddress());
            }
        }
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		SocketAddress sa = ctx.channel().remoteAddress();
		logger.info(sa.toString() + "掉线了");
		DeviceConnectionManager.quit(ctx);
		ctx.close();
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)throws Exception {
		SocketAddress sa = ctx.channel().remoteAddress();
		logger.info(sa.toString() + "出现异常,异常信息为:"+cause.getMessage());
		ctx.fireExceptionCaught(cause);
	}

}
