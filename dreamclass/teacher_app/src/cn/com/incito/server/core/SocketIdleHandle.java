package cn.com.incito.server.core;

import java.io.IOException;
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
		JSONObject jsonObject = JSONObject.parseObject(message);
		
		MessagePacking messagePacking = JSONObject.parseObject(jsonObject.getString("messagePacking"), MessagePacking.class);
		byte msgId = messagePacking.msgId;
		
		//如果是心跳消息则也向pad端发送心跳消息
		if(Message.MESSAGE_HEART_BEAT == msgId){
			MessagePacking packing = new MessagePacking(Message.MESSAGE_HEART_BEAT);
			JSONObject json = new JSONObject();
			json.put("messagePacking", packing);
			logger.info("收到pad端心跳包：	"+msgId+ctx.channel().remoteAddress());

			//			ctx.writeAndFlush((json.toJSONString()+"$_").getBytes());不再向pad端回复心跳信息
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
            	DeviceConnectionManager.quit(ctx);//关闭通道并刷新UI
                ctx.close();
            } else if (event.state() == IdleState.WRITER_IDLE) {
            	logger.info("服务端写超时，不做处理");
            } else if (event.state() == IdleState.ALL_IDLE) {
            	//心跳
            	MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_HEART_BEAT);
            	JSONObject jsonObject = new JSONObject();
            	jsonObject.put("messagePacking", messagePacking);
            	ByteBuf buf = Unpooled.copiedBuffer((jsonObject.toJSONString()+"\n").getBytes());
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
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
		SocketAddress sa = ctx.channel().remoteAddress();
		logger.info(sa.toString() + "出现异常,异常信息为:"+cause.getMessage());
		if(cause instanceof IOException){//心跳写异常，可能是通道关闭，等待进一步测试抛异常种类调试
			logger.error("心跳解析出错,IOException", cause);
			DeviceConnectionManager.quit(ctx);
			ctx.close();
			return;
		}
		DeviceConnectionManager.quit(ctx);
		ctx.close();//未知异常，暂时关闭通道。等待测试结果再操作
		logger.error("心跳解析失败", cause);
		ctx.fireExceptionCaught(cause);
	}

}
