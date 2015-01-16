package cn.com.incito.socket.core;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
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

	private volatile static NCoreSocket nCoreSocket;

	private NCoreSocket() {
		timer = new Timer("连接定时器");
		timerTask = new TimerTask() {
			@Override
			public void run() {
				try {
					startConnection();
				} catch (Exception e) {
					MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+ ":restartConnection:连接中断,异常消息:" + e.getMessage());
				}
			}
		};
	}

	private volatile Channel channel = null;
	private Timer timer;
	private TimerTask timerTask;

	public synchronized Channel getChannel() {
		return channel;
	}

	/**
	 * 单例模式
	 * 
	 * @return
	 */
	public static NCoreSocket getInstance() {
		if (nCoreSocket == null) {
			synchronized (NCoreSocket.class) {
				if (nCoreSocket == null) {
					nCoreSocket = new NCoreSocket();
				}
			}
		}
		return nCoreSocket;
	}

	@Override
	public void startConnection() throws InterruptedException {
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+ ":NCoreSocket:开始进行连接!");
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();

			bootstrap.group(workGroup);
			bootstrap.channel(NioSocketChannel.class);
			bootstrap.option(ChannelOption.TCP_NODELAY, true);

			bootstrap.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ByteBuf delimiter = Unpooled.copiedBuffer("\n".getBytes());
					ChannelPipeline pipeline = ch.pipeline();
					pipeline.addLast(new IdleStateHandler(30, 0, 10));
					pipeline.addLast(new DelimiterBasedFrameDecoder(5 * 1024 * 1024, delimiter));
					pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
					pipeline.addLast(new NMainHandler());
				}
			});
			channel = bootstrap.connect(Constants.IP, Constants.PORT).sync().channel();
			channel.closeFuture().sync();

		} finally {
			// 释放资源并且在时间任务调度下下一次连接时间是当前任务完成后30s执行
			channel = null;
			workGroup.shutdownGracefully();
			MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+ ":NCoreSocket:本次通讯任务执行的时间:"+ timerTask.scheduledExecutionTime()/60000 + "分");
		}
	}

	@Override
	public void stopConnection() {
		if (channel != null) {
			channel.close();
			channel = null;
		}
		if (timer != null) {
			try {
				timer.cancel();
			} catch (RuntimeException e) {
				MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+ ":NCoreSocket:timer:由于任务调度中有任务没有执行完成所造成的异常可以不同管!");
			}
		}
		if (timerTask != null) {
			try {
				timerTask.cancel();
			} catch (RuntimeException e) {
				MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+ ":NCoreSocket:timerTask:由于任务调度中有任务没有执行完成所造成的异常可以不同管!");
			}
		}

	}

	@Override
	public void sendMessage(final MessagePacking messagePacking) {
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("messagePacking", messagePacking);
		if (channel != null) {
			MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+ ":NCoreSocket:向服务器发送消息,消息ID:" + messagePacking.msgId);
			ByteBuf buf = Unpooled.copiedBuffer((jsonObject.toJSONString() + "\n").getBytes());
			ChannelFuture channelFuture = channel.writeAndFlush(buf);
			channelFuture.addListener(new SendMessageListener(messagePacking));
		} else {
			MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+ ":NCoreSocket:还没有连接至服务器,不能进行消息发送!");
		}
	}
	

	/**
	 * 连接操作里面包含重连
	 */
	@Override
	public void connection() {
		/**
		 * 采用固定延迟触发即下一次任务执行的时间是上一个任务完成后30s执行
		 */
		timer.schedule(timerTask, 0, 30000);
	}
}
