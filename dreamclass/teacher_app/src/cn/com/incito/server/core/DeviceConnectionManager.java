package cn.com.incito.server.core;

import io.netty.channel.ChannelHandlerContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class DeviceConnectionManager {

	private static Map<String, ChannelHandlerContext> connections	 = Collections.synchronizedMap(new HashMap<String, ChannelHandlerContext>());
	private static IdleManager idleManager = IdleManager.getInstance();
	
	/**
	 * 设备登录登记
	 * @param imei
	 * @param cnctx
	 */
	public static void notificate(String imei, ChannelHandlerContext cnctx){
		ChannelHandlerContext ctx = connections.get(imei);
		if(ctx==null){
			connections.put(imei, cnctx);
		}
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
		quit(imei);
	}
	
	public static  ChannelHandlerContext remove(String imei){
		return connections.remove(imei);
	}
}
