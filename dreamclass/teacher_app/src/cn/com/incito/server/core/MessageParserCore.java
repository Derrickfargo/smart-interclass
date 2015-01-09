package cn.com.incito.server.core;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import cn.com.incito.server.message.MessagePacking;


public class MessageParserCore {

	private Logger log  =  Logger.getLogger(MessageParserCore.class.getName());
	private static MessageParserCore instance;
	private final boolean isRunning ;
	private Lock lock = new ReentrantLock();
	private Condition isEmpty	=	lock.newCondition();
	private	 Queue<MessagePacking> messageQueue = new LinkedList<MessagePacking>();
	private final long TIMEOUT = 5000;
	
	public MessageParserCore(){
		isRunning = true;
		initMessageParserLooper();
	}
	
	public static MessageParserCore getInstance(){
		if(instance==null){
			instance = new MessageParserCore();
		}
		return instance;
	}
	
	/**
	 * 加入消息队列
	 * @param msg
	 * @return
	 */
	public boolean addMsgQue(MessagePacking msg){
		boolean isSuccess=false;
		if(msg==null){
			log.error("警告：压入队列消息为空！");
			return isSuccess;
		}
		lock.lock();
		isSuccess = messageQueue.offer(msg);
		isEmpty.signal();
		log.info("压入队列是否成功："+isSuccess);
		lock.unlock();
		return isSuccess;
	}
	
	private void initMessageParserLooper() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(isRunning){
					lock.lock();
					try{
					MessagePacking msg = messageQueue.poll();
					if(msg==null){
						if(!isEmpty.await(TIMEOUT, TimeUnit.MILLISECONDS)){
							log.info("****正在等待新消息到达****");
						}
					} else{
						handleMessage(msg);
						}
					}catch(Exception e){
						log.fatal("消息队列中断：" + e.getMessage());
						continue;
					}finally{
						lock.unlock();
					}
				}
			}
		}).start();
	}
	
	/**
	 * 处理消息
	 * @param msg
	 */
	private void handleMessage(final MessagePacking msg){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				msg.getMessageHandler().handleMessage(msg);
			}
		}).start();
	}
	
}
