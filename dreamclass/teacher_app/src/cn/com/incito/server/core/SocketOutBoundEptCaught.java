package cn.com.incito.server.core;

import org.apache.log4j.Logger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;

public class SocketOutBoundEptCaught extends ChannelOutboundHandlerAdapter{

	private Logger log =Logger.getLogger(SocketOutBoundEptCaught.class.getName());
	
		@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.info("写出现异常", cause);
		ctx.fireExceptionCaught(cause);
	}
}
