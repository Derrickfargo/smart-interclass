package cn.com.incito.socket.core;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.ui.activity.SplashActivity;
import cn.com.incito.classroom.ui.activity.WaitingActivity;
import cn.com.incito.common.utils.AndroidUtil;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 消息发送完成事件的监听
 * 
 * @author hm
 */
public class SendMessageListener implements ChannelFutureListener {

	private MessagePacking messagePacking;

	public SendMessageListener(MessagePacking messagePacking) {
		this.messagePacking = messagePacking;
	}

	@Override
	public void operationComplete(ChannelFuture future) throws Exception {
		byte msgId = messagePacking.msgId;
		if (future.isSuccess()) {
			MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+ ":SendMessageListener:向服务器发送消息成功,消息ID:" + msgId);
				
			/**
			 * 发送设备登录命令成功后
			 * 如果当前界面是启动界面则判断apk是否需要更新,否则发送设备是否绑定消息
			 * 不是启动界面则是断线后重连,重新获取分组消息
			*/
			if(msgId == Message.MESSAGE_HAND_SHAKE){
				String activityName = AppManager.getAppManager().currentActivity().getClass().getSimpleName();
				if("SplashActivity".equals(activityName)){
					MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + ":SendMessageListener:当前界面是开始动画界面检查apk更新后再发送设备是否绑定消息");
					SplashActivity splashActivity = (SplashActivity) AppManager.getAppManager().currentActivity();
					boolean isUpdateAp = splashActivity.isUpdateApk();
					if(!isUpdateAp){
						MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + ":SendMessageListener:当前界面是开始动画界面检查apk不需要更新,发送设备是否绑定消息");
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("imei", MyApplication.deviceId);
						MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_DEVICE_HAS_BIND);
						messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(jsonObject.toJSONString()));
						NCoreSocket.getInstance().sendMessage(messagePacking);
						return;
					}
				}
				WaitingActivity waitingActivity = UIHelper.getInstance().getWaitingActivity();
				if(waitingActivity != null){
					MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + ":SendMessageListener:当前界面不是动画界面,发送获取小组信息");
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("imei", MyApplication.deviceId);
					MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_GROUP_LIST);
					messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(jsonObject.toJSONString()));
					NCoreSocket.getInstance().sendMessage(messagePacking);
				}
			}
		}else{
			MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + ":SendMessageListener:向服务器发送消息失败消息ID：" + msgId);
		}
	}
}
