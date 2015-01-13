package cn.com.incito.server.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.log4j.Logger;

import cn.com.incito.server.api.Application;
import cn.com.incito.server.config.AppConfig;
import cn.com.incito.server.message.MessagePacking;

import com.alibaba.fastjson.JSONObject;


public class SocketServiceCore {

	private static SocketServiceCore socketServiceCore;
	
	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	
	Logger logger = Logger.getLogger(SocketServiceCore.class.getName());
	private final ServerBootstrap serverBootstrap = new ServerBootstrap();
	private final EventLoopGroup bossGroup = new NioEventLoopGroup();
	private final EventLoopGroup workerGroup = new NioEventLoopGroup();
	private final Properties properties = AppConfig.getProperties();
	private SocketServiceCore() {

	}

	public static SocketServiceCore getInstance() {
		if (socketServiceCore == null) {
			socketServiceCore = new SocketServiceCore();
		}
		return socketServiceCore;
	}
	
	
	/**
	 * 开始接收服务器的连接
	 */
	public void startAccept() {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				serverBootstrap.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel channel)throws Exception {
						ByteBuf delimiter = Unpooled.copiedBuffer("\n".getBytes());
						/**
						 * 读取配置文件 确定心跳时间
						 */
						int readIdle = 0;
						int idle = 0;
						
						readIdle = Integer.parseInt(properties.getProperty("readidle"));
						idle = Integer.parseInt(properties.getProperty("idle"));
						ChannelPipeline pipeline = channel.pipeline();
						pipeline.addLast(new IdleStateHandler(readIdle,0,idle));
						pipeline.addLast(new DelimiterBasedFrameDecoder(5*1024*1024, delimiter));
					//TODO
						pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
						pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
						pipeline.addLast(new SocketIdleHandle());
						pipeline.addLast(new ServiceHandle());
					}
				})
				.option(ChannelOption.SO_KEEPALIVE, true)
				.option(ChannelOption.TCP_NODELAY, true)
				.childOption(ChannelOption.SO_KEEPALIVE, true)
				.childOption(ChannelOption.TCP_NODELAY, true);
				try {
					ChannelFuture f = serverBootstrap.bind(9001).sync();
					logger.info("服务端通讯线程启动");
					f.channel().closeFuture().sync();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}finally {
					bossGroup.shutdownGracefully();
					workerGroup.shutdownGracefully();
				}
			}
		});	
	}
	/**
	 * 发送信息至指定通道
	 * @param messagePacking
	 * @param ctx
	 * @return
	 */
	public boolean sendMsg(MessagePacking messagePacking,ChannelHandlerContext ctx){
		boolean flag = false;
		JSONObject json = new JSONObject();
		json.put("messagePacking", messagePacking);
		ByteBuf buf = Unpooled.copiedBuffer((json.toString()+"\n").getBytes());
		if(ctx!=null&&ctx.channel().isActive()){
			ctx.writeAndFlush(buf);
			flag = true;
		}
		return flag;
	}

	/**
	 * 启动线程发往所有客户端
	 * @param messagePacking
	 */
	public void sendMsg(final MessagePacking messagePacking){
		Application app = Application.getInstance();
		final Collection<ChannelHandlerContext> channels = app.getClientChannel().values();
		new Thread() {
			@Override
			public void run() {
					for(ChannelHandlerContext channel: channels){
						if(channel != null && channel.channel().isActive()){
							// 输出到通道
							sendMsg(messagePacking, channel);
						}
					}
			};
		}.start();
		
	}
}
