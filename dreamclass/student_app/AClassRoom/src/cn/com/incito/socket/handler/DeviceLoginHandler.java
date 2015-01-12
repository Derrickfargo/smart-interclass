package cn.com.incito.socket.handler;

import android.content.SharedPreferences.Editor;
import android.util.Log;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.utils.Utils;
import cn.com.incito.socket.core.MessageHandler;

import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
/**
 * 登陆处理hanlder
 * Created by liushiping on 2014/7/28.
 * 
 * 修改的地方 取消了调用心跳检测方法
 */
public class DeviceLoginHandler extends MessageHandler {
	private static final String TAG=DeviceLoginHandler.class.getSimpleName();
	public static final Logger Logger = LoggerFactory.getLogger();
	@Override
	protected void handleMessage() {
		String ip = (String)data.get("server_ip");
		String port=(String) data.get("server_port");
		Editor editor = MyApplication.getInstance().getSharedPreferences().edit();
		editor.putString("server_ip", ip);
		editor.putString("server_port", port);
		editor.commit();
		editor.apply();
		Logger.debug(Utils.getTime()+TAG+":收到登陆回复,连接建立成功,开始启动心跳!"+data.toJSONString());
		Log.i(TAG, "收到登陆回复,连接建立成功,开始启动心跳!"+data.toJSONString());
		//启动心跳检测
//		ConnectionManager.getInstance(message.getChannel());
	}

}
