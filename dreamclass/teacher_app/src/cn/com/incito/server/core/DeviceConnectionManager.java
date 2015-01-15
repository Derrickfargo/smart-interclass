package cn.com.incito.server.core;

import io.netty.channel.ChannelHandlerContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

public class DeviceConnectionManager {

	private static Map<String, ChannelHandlerContext> connections	 = Collections.synchronizedMap(new HashMap<String, ChannelHandlerContext>());
	private static IdleManager idleManager = IdleManager.getInstance();
	private static Logger log = Logger.getLogger(DeviceConnectionManager.class.getName());
	
	/**
	 * 设备登录登记
	 * @param imei
	 * @param cnctx
	 */
	public static void notificate(String imei, ChannelHandlerContext cnctx){
		if(imei!=null){
				connections.put(imei, cnctx);
				return;
		}
		log.info("設備登錄獲取imei失敗！");
	}
	
	/**
	 * 设备正常退出
	 * @param imei
	 */
	public static void quit(String imei){
		idleManager.close(imei);
	}
	
	/**
	 * 设备非正常退出
	 * @param imei
	 * @return
	 */
	public static void quit(ChannelHandlerContext ctx){
		String imei = null;
		for(Entry<String, ChannelHandlerContext> connection:connections.entrySet()){
			if(connection.getValue().channel().equals(ctx.channel())){
				imei	= connection.getKey();
			}
		}
		log.info("將要關閉通道的設備imei："+imei);
		if(imei==null){
			return;
		}
		connections.remove(imei);
		quit(imei);
	}
	
	public static  ChannelHandlerContext remove(String imei){
		return connections.remove(imei);
	}
}
