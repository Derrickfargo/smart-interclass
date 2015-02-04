package cn.com.incito.socket.core.callback;

import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.common.utils.LogUtil;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.core.MessageActionMap;

/**
* @ClassName: DefaultSendMessageUtil 
* @Description: 默认的消息发送回调实现  主要用于打印日志
* @author hm
* @date 2015年1月31日 上午9:57:09 
* @version 1.0
 */
public class DefaultSendMessageCallback implements ISendMessageCallback{

	@Override
	public void sendSuccess(byte msgId) {
		if(Message.MESSAGE_SAVE_PAPER == msgId){
			LogUtil.d("消息发送成功,消息ID:" + msgId + ":动作:作业上传成功,通知老师,并且等待回执");
		}else{
			LogUtil.d("消息发送成功,消息ID:" + msgId + ":动作:" + MessageActionMap.getActionByMsgId(msgId));
		}
	}

	@Override
	public void sendError(byte msgId, Throwable cause) {
		showToastInActivity();
		LogUtil.e("消息发送失败,消息ID:" + msgId + ":动作:" + MessageActionMap.getActionByMsgId(msgId),cause);
	}

	@Override
	public void channelIsNull(byte msgId) {
		showToastInActivity();
		LogUtil.d("不能发送消息,消息ID:" + msgId + ":动作:" + MessageActionMap.getActionByMsgId(msgId));
	}
	
	/**
	 * 在界面中弹出消息发送失败提示
	 */
	private void showToastInActivity(){
		BaseActivity activity = (BaseActivity) AppManager.getAppManager().currentActivity();
		activity.showError();
	}

}
