package cn.com.incito.server.utils;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import cn.com.incito.server.config.AppConfig;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.SocketServiceCore;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;

import com.alibaba.fastjson.JSONObject;

public class QuizCollector {
	private Logger logger = Logger.getLogger(QuizCollector.class.getName());
	private static final int TIMEOUT = 20000;//消息队列等待时长
	private static QuizCollector instance;
	private int capacity;
	private final Lock lock = new ReentrantLock();
	private final Condition isIdle = lock.newCondition();
	private Queue<ChannelHandlerContext> quizQueue = new LinkedList<ChannelHandlerContext>();
	private Set<ChannelHandlerContext> quizSet = new HashSet<ChannelHandlerContext>();
	private List<Channel> quizList = new ArrayList<Channel>();

	public static QuizCollector getInstance() {
		if (instance == null) {
			instance = new QuizCollector();
		}
		return instance;
	}

	private QuizCollector() {
		capacity = 0;
		initQuizCollector();
	}

	private int getQuizThreadThreshold(){
		Properties props = AppConfig.getProperties();
		String threshold = props.get("quiz_receive_threshold").toString();
		return Integer.parseInt(threshold);
	}
	
	/**
	 * 收取下一个作业
	 */
	public void nextQuiz() {
		if(quizQueue.size() == 0){
			return;
		}
		logger.info("当前处理的作业个数："+capacity);
//		if (capacity >= getQuizThreadThreshold()) {
//			return;
//		}
		lock.lock();
		isIdle.signal();
		lock.unlock();
	}
	/**
	 * 加入作业收取队列
	 * @param channel
	 */
	public void addQuizQueue(ChannelHandlerContext channel) {
		if (channel == null) {
			return;
		}
		quizList.add(channel.channel());
		quizSet.add(channel);
		quizQueue.offer(channel);
		logger.info("收作业命令加入队列："+channel.channel().remoteAddress());
		if (quizQueue.size() == 1) {
			lock.lock();
			isIdle.signal();
			lock.unlock();
		}
	}
	
	/**
	 * 初始化作业收取队列
	 */
	private synchronized void initQuizCollector() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (Boolean.TRUE) {
					lock.lock();
					try {
						while (capacity >= getQuizThreadThreshold()) {
							if (!isIdle.await(TIMEOUT, TimeUnit.MILLISECONDS)) {
								logger.info("*****正在等待作业提交请求*****");
							}
						}
						while (capacity < getQuizThreadThreshold() && quizQueue.size() != 0) {
							ChannelHandlerContext channel = quizQueue.poll();
							if(channel != null){
								capacity++;
								doCollect(channel);// 收集作业
							}
						}
					} catch (Exception e) {
						logger.fatal("作业队列中断：", e);
					} finally {
						lock.unlock();
					}
				}
			}
		}).start();
	}
	
	private void doCollect(final ChannelHandlerContext channel){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_SAVE_PAPER);
		        JSONObject json = new JSONObject();
		        json.put("uuid", UUID.randomUUID().toString());
		        messagePacking.putBodyData(DataType.INT,BufferUtils.writeUTFString(json.toString()));
				try {
					SocketServiceCore.getInstance().sendMsg(messagePacking, channel);
					logger.info("收作业命令已发送："+channel.channel().remoteAddress());
					new QuizCollectMonitor(channel).start();
				} catch (Exception e) {
					logger.error("作业收取失败,直接收取下一个作业", e);
					quizComplete(channel);
					nextQuiz();//收取下一个作业
					return;
				}
			}
			
		}).start();
	}
	
	public void quizComplete(ChannelHandlerContext channel) {
		capacity--;
		quizSet.remove(channel);
	}
	
	public void orderComplete(ChannelHandlerContext ctx){
		quizList.remove(ctx.channel());
	}

	/**
	 * 单独作业检测
	 * @author JOHN
	 *
	 */
	private class QuizCollectMonitor extends Thread {
		private ChannelHandlerContext channel;
		public QuizCollectMonitor(ChannelHandlerContext channel) {
			this.channel = channel;
		}

		public void run() {
			try {
				Thread.sleep(TIMEOUT);// 10秒后检测是否提交完成
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (quizSet.contains(channel)) {
				logger.info("作业在10秒内没有收取成功,自动收取下一个作业.");
				quizComplete(channel);
				nextQuiz();
			}
			if(quizList.contains(channel.channel())){
				quizList.remove(channel.channel());
				channel.close();
			}
		}
	}
}
