package cn.com.incito.socket.handler;

import com.alibaba.fastjson.JSONObject;

import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.common.utils.AndroidUtil;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.socket.core.NCoreSocket;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;

public class LockScreenHandler extends MessageHandler {

	private String isLock;

	@Override
	protected void handleMessage() {
		isLock = data.getString("isLock");
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + ":LockScreenHandler收到锁屏消息:" + isLock);
		String currentActivityName = AppManager.getAppManager().currentActivity().getClass().getSimpleName();
		if (isLock.equals("true")) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("imei", MyApplication.deviceId);
			MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_GROUP_LIST);
			messagePacking.putBodyData(DataType.INT,BufferUtils.writeUTFString(jsonObject.toJSONString()));
			NCoreSocket.getInstance().sendMessage(messagePacking);
				if("RandomGroupActivity".equals(currentActivityName)){
					AppManager.getAppManager().finishActivity();
				}
				MyApplication.getInstance().setOnClass(true);
				MyApplication.getInstance().lockScreen(true);
		} else if(isLock.equals("false")){
			MyApplication.getInstance().lockScreen(false);
		}else{
			MyApplication.getInstance().lockScreen(false);
			MyApplication.getInstance().setOnClass(false);
			NCoreSocket.getInstance().stopConnection();
			AppManager.getAppManager().AppExit(MyApplication.getInstance());
		}
	}
}
