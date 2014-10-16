package cn.com.incito.socket.handler;

import android.content.SharedPreferences.Editor;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.socket.core.ConnectionManager;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.wisdom.sdk.log.WLog;
/**
 * 登陆处理hanlder
 * Created by liushiping on 2014/7/28.
 */
public class DeviceLoginHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		String ip = (String)data.get("server_ip");
		String port=(String) data.get("server_port");
		Editor editor = MyApplication.getInstance().getSharedPreferences().edit();
		editor.putString("server_ip", ip);
		editor.putString("server_port", port);
		editor.commit();
		editor.apply();
		
		WLog.i(DeviceLoginHandler.class, "收到登陆回复,连接建立成功,开始启动心跳!");
		//启动心跳检测
		ConnectionManager.getInstance(message.getChannel());
	}

}
