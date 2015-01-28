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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.classroom.ui.activity.DrawBoxActivity;
import cn.com.incito.classroom.ui.activity.WaitingActivity;
import cn.com.incito.common.utils.LogUtil;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.message.MessagePacking;

import com.alibaba.fastjson.JSONObject;

/**
 * 客户端连接服务器核心类
 * @author hm
 */
public final class NCoreSocket{

	private volatile static NCoreSocket nCoreSocket;
	private ScheduledExecutorService executorService;
	private volatile Channel channel;

	private NCoreSocket() {
		executorService = Executors.newScheduledThreadPool(1);
	}
	public synchronized Channel getChannel() {
		return channel;
	}

	/**
	 * 单例模式保证内存中只有一个对象
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
	/**
	 * 连接初始化参数配置
	 */
	private void init() {
		
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(workGroup);
			bootstrap.channel(NioSocketChannel.class);
			bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);//tcp层面io超时时间
			bootstrap.option(ChannelOption.TCP_NODELAY, true);
			bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
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
			LogUtil.d("属性设置完毕,开始进行连接!");
			channel = bootstrap.connect(Constants.IP, Constants.PORT).sync().channel();
			channel.closeFuture().sync();
		}catch(Exception e){
			LogUtil.e("出现异常", e.getCause());
		}finally {
			// 释放资源并且在时间任务调度下下一次连接时间是当前任务完成后30s执行
			channel = null;
			WaitingActivity waitingActivity = UIHelper.getInstance().getWaitingActivity();
			if(waitingActivity != null){
				LogUtil.d("改变本pad的所有学生的状态");
				waitingActivity.notifyStudentOffline();
			}
			DrawBoxActivity boxActivity = UIHelper.getInstance().getDrawBoxActivity();
			if(boxActivity != null){
				LogUtil.d("清理画板!");
				boxActivity.closeProgressDialog();
				boxActivity.cancleTask();
				boxActivity.showToast();
			}
			workGroup.shutdownGracefully();
			LogUtil.d("本次连接资源清理完毕,等待下次连接!");
		}
	}

	/**
	 * 停止连接
	 */
	public void stopConnection() {
		LogUtil.d("关闭连接,停止线程任务");
		if (channel != null) {
			channel.close();
			channel = null;
		}
		if(executorService != null && !executorService.isShutdown()){
			executorService.shutdownNow();
		}
	}
	
	public void closeConnection(){
		if(channel != null){
			LogUtil.d("主动关闭通道,进行重连!");
			channel.close();
		}
	}

	/**
	 * 向服务器发送消息
	 * @param messagePacking
	 */
	public void sendMessage(final MessagePacking messagePacking) {
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("messagePacking", messagePacking);
		if (channel != null) {
			LogUtil.d("向服务器发送消息,消息ID:" + messagePacking.msgId);
			ByteBuf buf = Unpooled.copiedBuffer((jsonObject.toJSONString() + "\n").getBytes());
			ChannelFuture channelFuture = channel.writeAndFlush(buf);
			channelFuture.addListener(new SendMessageListener(messagePacking));
		} else {
			LogUtil.d("还没有连接至服务器,不能进行消息发送!" + messagePacking.msgId);
		}
	}
	

	/**
	 * 连接操作里面包含重连
	 */
	public void connection() {
		/**
		 * 采用固定延迟触发即下一次任务执行的时间是上一个任务完成后30s执行
		 */
		executorService.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				init();
			}
		}, 0, 30, TimeUnit.SECONDS);
	}
}
