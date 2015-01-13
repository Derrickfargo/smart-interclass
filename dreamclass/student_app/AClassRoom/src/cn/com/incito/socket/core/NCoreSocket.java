package cn.com.incito.socket.core;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import java.util.Timer;
import java.util.TimerTask;

import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.common.utils.AndroidUtil;
import cn.com.incito.socket.message.MessagePacking;

import com.alibaba.fastjson.JSONObject;

public class NCoreSocket implements ICoreSocket {
	
	private static NCoreSocket nCoreSocket;
	private NCoreSocket(){};
	private Channel channel = null;
	
	public Channel getChannel() {
		return channel;
	}

	/**
	 * 单例模式
	 * @return
	 */
	public static NCoreSocket getInstance(){
		if(nCoreSocket == null){
			nCoreSocket = new NCoreSocket();
		}
		return nCoreSocket;
	}

	@Override
	public void startConnection(final String ip, final int port) throws InterruptedException {

		EventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();

			bootstrap.group(workGroup);
			bootstrap.channel(NioSocketChannel.class);
			bootstrap.option(ChannelOption.TCP_NODELAY, true);
			
			bootstrap.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
					ChannelPipeline pipeline = ch.pipeline();
					pipeline.addLast(new IdleStateHandler(30, 0, 10));
					pipeline.addLast(new DelimiterBasedFrameDecoder(5 * 1024 * 1024, delimiter));
					pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
					pipeline.addLast(new NMainHandler());
				}
			});
			
			channel = bootstrap.connect(ip, port).sync().channel();
			channel.closeFuture().sync();
			
		} finally{
			//释放资源
			channel = null;
			workGroup.shutdownGracefully();
			connection();
		}
	}

	@Override
	public void stopConnection() {
		channel.close();
	}

	@Override
	public void sendMessage(MessagePacking messagePacking) {
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("messagePacking", messagePacking);

		if (channel != null) {
			
			ByteBuf buf = Unpooled.copiedBuffer((jsonObject.toJSONString() + "$_").getBytes());
			ChannelFuture channelFuture = channel.writeAndFlush(buf);
			channelFuture.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future)throws Exception {
					if (future.isSuccess()) {
						MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+ ":NCoreSocket:向服务器发送消息成,消息内容:"
								+ jsonObject.toJSONString());
					} else {
						// TODO 可以在断线重连后进行重发消息
						
					}
				}
			});
		} else {
			MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+ ":NCoreSocket:还没有连接至服务器,不能进行消息发送!");
		}
	}

	/**
	 * 连接操作里面包含重连
	 */
	@Override
	public void connection() {
		Timer timer = new Timer();
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				try {
					startConnection(Constants.IP, Constants.PORT);
				} catch (InterruptedException e) {
					MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + ":restartConnection:连接中断");
					e.printStackTrace();
				}
			}
		};
		
		if(MyApplication.getInstance().isFirstConnection()){
			timer.schedule(timerTask, 0);
		}else{
			timer.schedule(timerTask, 30000);
		}
	}
}
