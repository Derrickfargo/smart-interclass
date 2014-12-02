package cn.com.incito.server.utils;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;

import com.alibaba.fastjson.JSONObject;

public class QuizCollector {
	private Logger logger = Logger.getLogger(QuizCollector.class.getName());
	private static final int TIMEOUT = 5000;//消息队列等待时长
	private static QuizCollector instance;
	private boolean isIdling = false;
	private final Lock lock = new ReentrantLock();
	private final Condition isIdle = lock.newCondition();
//	private final QuizCollectMonitor monitor = new QuizCollectMonitor();
	private Queue<SocketChannel> quizQueue = new LinkedList<SocketChannel>();
	private Set<SocketChannel> quizSet = new HashSet<SocketChannel>();

	public static QuizCollector getInstance() {
		if (instance == null) {
			instance = new QuizCollector();
		}
		return instance;
	}

	private QuizCollector() {
		initQuizCollector();
	}

	/**
	 * 收取下一个作业
	 */
	public void nextQuiz() {
		if(quizQueue.size() == 0){
			isIdling = false;
			return;
		}
		isIdling = true;
		lock.lock();
		isIdle.signal();
		lock.unlock();
	}
	/**
	 * 加入作业收取队列
	 * @param channel
	 */
	public void addQuizQueue(SocketChannel channel) {
		if (channel == null) {
			return;
		}
		quizSet.add(channel);
		quizQueue.offer(channel);
		if (quizQueue.size() == 1) {
			isIdling = true;
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
						while(!isIdling){
							if (!isIdle.await(TIMEOUT, TimeUnit.MILLISECONDS)) {
								logger.info("*****正在等待作业提交请求*****");
							}
						}
						isIdling = false;
						SocketChannel channel = quizQueue.poll();
						doCollect(channel);
					} catch (Exception e) {
						logger.fatal("作业队列中断：", e);
					} finally {
						lock.unlock();
					}
				}
			}
		}).start();
	}
	
	private void doCollect(final SocketChannel channel){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_SAVE_PAPER);
		        JSONObject json = new JSONObject();
		        json.put("id", Application.getInstance().getQuizId());
		        json.put("delay", 0);
		        messagePacking.putBodyData(DataType.INT,BufferUtils.writeUTFString(json.toString()));
		        byte[] data = messagePacking.pack().array();
				ByteBuffer buffer = ByteBuffer.allocate(data.length);
				buffer.clear();
				buffer.put(data);
				buffer.flip();
				try {
					channel.write(buffer);
					new QuizCollectMonitor(channel).start();
				} catch (Exception e) {
					logger.error("作业收取失败,直接收取下一个作业", e);
					quizSet.remove(channel);
					nextQuiz();//收取下一个作业
				}
			}
			
		}).start();
	}
	
	public void quizComplete(SocketChannel channel) {
		quizSet.remove(channel);
	}

	/**
	 * 单独作业检测
	 * @author JOHN
	 *
	 */
	private class QuizCollectMonitor extends Thread {
		private SocketChannel channel;
		public QuizCollectMonitor(SocketChannel channel) {
			this.channel = channel;
		}

		public void run() {
			try {
				Thread.sleep(TIMEOUT);// 5秒后检测是否提交完成
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (quizSet.contains(channel)) {
				logger.info("作业在5秒内没有收取成功,自动收取下一个作业.");
				quizSet.remove(channel);
				nextQuiz();
			}
		}
	}
}
