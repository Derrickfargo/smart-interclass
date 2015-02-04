package cn.com.incito.socket.core;

import android.util.SparseArray;

/**
* @ClassName: MessageActionMap 
* @Description: 消息编号与对应的执行的操作
* @author hm
* @date 2015年1月31日 上午9:59:30 
* @version 1.0
 */
public final class MessageActionMap {
	
	private static SparseArray<String> sparseArray;
	static{
		sparseArray = new SparseArray<String>();
		init();
	}
	
	private static void init(){
		sparseArray.put(Message.MESSAGE_HAND_SHAKE, "设备登录");
		sparseArray.put(Message.MESSAGE_DEVICE_LOGOUT, "设备退出");
		sparseArray.put(Message.MESSAGE_DEVICE_BIND, "绑定课桌");
		sparseArray.put(Message.MESSAGE_DEVICE_HAS_BIND, "判定课桌是否绑定");
		sparseArray.put(Message.MESSAGE_GROUP_CONFIRM, "小组图标修改完成,等待投票");
		sparseArray.put(Message.MESSAGE_GROUP_EDIT, "小组改名与修改图标");
		sparseArray.put(Message.MESSAGE_GROUP_LIST, "获取小组成员");
		sparseArray.put(Message.MESSAGE_GROUP_VOTE, "小组改名同意");
		sparseArray.put(Message.MESSAGE_HEART_BEAT, "心跳");
		sparseArray.put(Message.MESSAGE_LOCK_SCREEN, "锁屏");
		sparseArray.put(Message.MESSAGE_RANDOM_GROUP, "随机分组");
		sparseArray.put(Message.MESSAGE_RECIVE_PAER, "做作业回执");
		sparseArray.put(Message.MESSAGE_RESPONDER_END, "抢答结束");
		sparseArray.put(Message.MESSAGE_RICIVE_SAVEPAPER, "提交作业回执");
		sparseArray.put(Message.MESSAGE_SEND_PAPER, "主动提交作业请求");
		sparseArray.put(Message.MESSAGE_SAVE_PAPER, "老师收取作业");
		sparseArray.put(Message.MESSAGE_SAVE_PAPER_RESULT, "老师收到作业");
		sparseArray.put(Message.MESSAGE_STUDENT_LOGIN, "学生登录,退出,注册");
		sparseArray.put(Message.MESSAGE_STUDENT_RESPONDER, "抢答");
		sparseArray.put(Message.MESSAGE_DISTRIBUTE_PAPER, "做作业");
		sparseArray.put(Message.MESSAGE_APK_UPTE, "询问是否有升级包");
	}
	
	public static String getActionByMsgId(byte msgId){
		return sparseArray.get(msgId) == null ? "没有对应的动作" : sparseArray.get(msgId);
	}
}
