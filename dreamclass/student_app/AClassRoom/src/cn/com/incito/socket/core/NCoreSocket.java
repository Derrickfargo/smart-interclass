package cn.com.incito.socket.core;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.common.utils.AndroidUtil;
import cn.com.incito.socket.message.MessagePacking;

import com.alibaba.fastjson.JSONObject;

public class NCoreSocket implements ICoreSocket {

	private static NCoreSocket ncoreSokcet;
	private EventLoopGroup workGroup;

	public static NCoreSocket getInstance() {
		if (ncoreSokcet == null) {
			ncoreSokcet = new NCoreSocket();
		}
		return ncoreSokcet;
	}

	@Override
	public void startConnection(final String ip, final int port) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				workGroup = new NioEventLoopGroup();
				Bootstrap bootstrap = new Bootstrap();
				ChannelFuture channelFuture;

				bootstrap.group(workGroup);
				bootstrap.channel(NioSocketChannel.class);
				 bootstrap.option(ChannelOption.TCP_NODELAY, true);
				 bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
				bootstrap.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
						ChannelPipeline pipeline = ch.pipeline();
						pipeline.addLast(new IdleStateHandler(30, 0, 10));
						pipeline.addLast(new DelimiterBasedFrameDecoder(5 * 1024 * 1024, delimiter));
						pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
						pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
						pipeline.addLast(new NMainHandler());
					}
				});
				try {
					channelFuture = bootstrap.connect(ip, port).sync();
					channelFuture.channel().closeFuture().sync();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					workGroup.shutdownGracefully();
				}
			}
		}).start();
	}

	@Override
	public void stopConnection() {
		if(workGroup != null){
			workGroup.shutdownGracefully();
		}
		
	}

	@Override
	public void sendMessage(MessagePacking messagePacking) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("messagePacking", messagePacking);

		ChannelHandlerContext channelHandlerContext = MyApplication
				.getInstance().getChannelHandlerContext();
		if (channelHandlerContext != null) {
			ByteBuf buf = Unpooled.copiedBuffer((jsonObject.toJSONString() + "$_").getBytes());
			ChannelFuture channelFuture = channelHandlerContext.writeAndFlush(buf);
			channelFuture.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture arg0) throws Exception {
					if(arg0.isSuccess()){
						MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + ":消息发送成功!");
					}
				}
			});
		}else{
			MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + "NCoreSocketM已经断开了连接");
		}
	}

}
