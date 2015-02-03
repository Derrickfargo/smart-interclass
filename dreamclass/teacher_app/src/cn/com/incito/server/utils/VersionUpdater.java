package cn.com.incito.server.utils;

import io.netty.channel.ChannelHandlerContext;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.SocketServiceCore;
import cn.com.incito.server.message.MessagePacking;

public class VersionUpdater {

	private static VersionUpdater instance;
	private Logger log = Logger.getLogger(VersionUpdater.class.getName());
	private ExecutorService executor;
	private Application app = Application.getInstance();
	private Queue<String> deviceQueue;
	private Set<String> deviceSet;
	private Lock lock;
	private Condition idle;
	private int timeout,capacity,code;
	private String path;

	public static VersionUpdater getInstance(int code,String path) {
		if (instance == null)
			instance = new VersionUpdater(code,path);
		return instance;
	}

	public VersionUpdater() {
		this(0,null);
	}

	public VersionUpdater(int code,String path){
		deviceQueue = new LinkedList<String>();
		deviceSet = new HashSet<String>();
		executor = Executors.newCachedThreadPool();
		lock = new ReentrantLock();
		idle = lock.newCondition();
		timeout = 30000;
		capacity = 0;
		this.code = code;
		this.path = path;
		initVersionUpdater();
	}

	private void initVersionUpdater() {
		executor.execute(new Runnable() {

			@Override
			public void run() {
				while (Boolean.TRUE) {
					lock.lock();
					try {
						deviceQueue = getDeviceQueue();
						while (deviceQueue.size() == 0) {
							if (!idle.await(timeout, TimeUnit.MILLISECONDS))
								log.info("****正在等待新設備登錄****");
						}
						while (deviceQueue.size() != 0) {
							String imei = deviceQueue.poll();
							sendMsg(imei);
							capacity++;
							log.info("已发送设备总量："+capacity);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
						log.error("设备更新队列终端", e);
					} finally {
						lock.unlock();
					}
				}
			}
		});
	}

	private Queue<String> getDeviceQueue() {
		Set<String> onlineDevice =  app.getOnlineDevice();
		for(String imei : onlineDevice){
			if(!deviceSet.contains(imei)){
				deviceQueue.offer(imei);
				deviceSet.add(imei);
				idle.signal();
			}
		}
		return deviceQueue;
	}

	private void sendMsg(final String imei) {
		executor.execute(new Runnable() {
			
			@Override
			public void run() {
				ChannelHandlerContext ctx = app.getClientChannel().get(imei);
				JSONObject json = new JSONObject();
				json.put("serverVersionCode", code);
				json.put("fileName",path);
				MessagePacking msg = new MessagePacking(Message.MESSAGE_APK_UPDATE);
				msg.putBodyData(null, json.toJSONString().getBytes());
				SocketServiceCore.getInstance().sendMsg(msg, ctx);
				log.info("已发送设备升级命令："+imei);		
			}
		});
	}

}
