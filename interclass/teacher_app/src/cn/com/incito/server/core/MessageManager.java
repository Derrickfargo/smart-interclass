package cn.com.incito.server.core;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

/**
 * 消息管理器，采用队列方式管理解析后的消息
 * @author 刘世平
 *
 */
public class MessageManager {
	/**
	 * 消息队列等待时长
	 */
	private static final int TIMEOUT = 5000;
	private static MessageManager instance;

	private boolean isRunning = false;
	private final Lock lock = new ReentrantLock();
	private final Condition isEmpty = lock.newCondition();
	private Queue<Message> messageQueue = new LinkedList<Message>();
	private Logger logger = Logger.getLogger(MessageManager.class.getName());
	
	public static MessageManager getInstance() {
		if (instance == null) {
			instance = new MessageManager();
		}
		return instance;
	}
	
	private MessageManager(){
		isRunning = true;
		initMessageQueueLooper();
	}
	
	public boolean addQueue(Message message) {
		boolean isSuccess = false;
		if (message == null) {
			logger.info("警告：压入空消息到消息队列!");
			return isSuccess;
		}
		lock.lock();
		isSuccess = messageQueue.offer(message);
		logger.info("消息压入是否成功：" + isSuccess);
		isEmpty.signal();
		lock.unlock();
		return isSuccess;
	}

	private void initMessageQueueLooper() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (isRunning) {
					lock.lock();
					try {
						Message message = messageQueue.poll();
						if (message == null) {
							if (!isEmpty.await(TIMEOUT, TimeUnit.MILLISECONDS)) {
								logger.info("****正在等待新消息到达****");
							}
						} else {
							handleMessage(message);
						}
					} catch (Exception e) {
						logger.fatal("消息队列中断：" + e.getMessage());
						continue;
					}finally {
						lock.unlock();
					}
				}
			}
		}).start();
	}
	
	private void handleMessage(final Message message){
		new Thread(new Runnable() {

			@Override
			public void run() {
				message.handleMessage();
			}
			
		}).start();
	}
}
