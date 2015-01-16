package cn.com.incito.socket.core;

import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.ui.activity.WaitingActivity;
import cn.com.incito.common.utils.AndroidUtil;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;

import com.alibaba.fastjson.JSONObject;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

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
//			boolean isReconnect = MyApplication.getInstance().isReconnect;
				
			/**
			 * 发送设备登录命令成功后
			 * 如果当前界面是启动界面则发送设备是否绑定消息
			 * 不是启动界面判断是否是重连
			 * 如果是重连则发送重连命令
			*/
			if(msgId == Message.MESSAGE_HAND_SHAKE){
				String activityName = AppManager.getAppManager().currentActivity().getClass().getSimpleName();
				if("SplashActivity".equals(activityName)){
					MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + ":SendMessageListener:当前界面是开始动画界面发送设备是否绑定消息");
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("imei", MyApplication.deviceId);
					MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_DEVICE_HAS_BIND);
					messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(jsonObject.toJSONString()));
					NCoreSocket.getInstance().sendMessage(messagePacking);
					return;
				}
				WaitingActivity waitingActivity = UIHelper.getInstance().getWaitingActivity();
				if(waitingActivity != null){
					MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + ":SendMessageListener:当前是进行重连,发送获取小组信息");
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("imei", MyApplication.deviceId);
					MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_GROUP_LIST);
					messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(jsonObject.toJSONString()));
					NCoreSocket.getInstance().sendMessage(messagePacking);
				}
//				if(isReconnect){
//					MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+ ":SendMessageListener:发送重连消息!");
//					JSONObject jsonObject = new JSONObject();
//					jsonObject.put("imei", MyApplication.deviceId);
//					MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_RECONNECT);
//					messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(jsonObject.toJSONString()));
//					NCoreSocket.getInstance().sendMessage(messagePacking);
//				}
			}
				
			/**
			 * 发送获取小组列表命令成功后
			 * 判断程序上一次是否是正常退出
			 * 不是正常退出则发送重连名
			 * 主要用于在上课中app因为有未捕获的异常导致程序退出后重启
			 * 保持状态同步
			*/
//			if(msgId == Message.MESSAGE_GROUP_LIST){
//				boolean isNormalExit = MyApplication.getInstance().isNormalExit();
//				if(!isNormalExit){
//					MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + ":SendMessageListener:上次设备非正常退出重新启动应用,进入等待界面发送获取小组列表成功后再次发送重连命令");
//					MyApplication.getInstance().setNormalExit(true);
//					JSONObject jsonObject = new JSONObject();
//					jsonObject.put("imei", MyApplication.deviceId);
//					MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_RECONNECT);
//					messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(jsonObject.toJSONString()));
//					NCoreSocket.getInstance().sendMessage(messagePacking);
//				}
//			}
		}else{
			MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + ":SendMessageListener:向服务器发送消息失败消息ID：" + msgId);
		}
	}
}
