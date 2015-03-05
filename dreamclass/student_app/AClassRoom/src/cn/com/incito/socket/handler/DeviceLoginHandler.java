package cn.com.incito.socket.handler;

import android.content.SharedPreferences.Editor;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.socket.core.util.SendMessageUtil;

/**
 * 登陆处理hanlder Created by liushiping on 2014/7/28.
 * 
 * 修改的地方 取消了调用心跳检测方法
 */
public class DeviceLoginHandler extends MessageHandler {
	@Override
	protected void handleMessage() {
		String ip = (String) data.get("server_ip");
		String port = (String) data.get("server_port");
		Editor editor = MyApplication.getInstance().getSharedPreferences().edit();
		editor.putString("server_ip", ip);
		editor.putString("server_port", port);
		editor.commit();
		editor.apply();
		
		/**
		 * 发送判断apk是否需要更新
		 */
		SendMessageUtil.isUpdateApk();
		
//		oldUpdate();
	}
}
