package cn.com.incito.socket.core.util;

import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.core.callback.DefaultSendMessageCallback;
import cn.com.incito.socket.message.MessagePacking;

/**
 * 消息发送的封装
 * @author hm
 *
 */
public final class SendMessageUtil extends AbstractSendMessageUitl{
	
	/**
	 * 发送心跳消息
	* @author hm
	* @date 2015年1月31日 下午1:03:48 
	* @Title: sendHeartBeat 
	* @Description:  回应服务器端的心跳
	* @return void    返回类型 
	* @throws
	 */
	public static void sendHeartBeat() {
		MessagePacking packing = new MessagePacking(Message.MESSAGE_HEART_BEAT);
		mCoreSocket.sendMessage(packing, new DefaultSendMessageCallback());
	}
	
	/**
	 * 发送设备登录命令
	 */
	public static void sendDeviceLogin(){
		sendMessagePackingWithImei(Message.MESSAGE_HAND_SHAKE);
	}
	
	/**
	 * 发送设备是否绑定
	 */
	public static void sendDeviceHasBind(){
		sendMessagePackingWithImei(Message.MESSAGE_DEVICE_HAS_BIND);
	}
	
	/**
	 * 绑定课桌
	 * @param jsonObject
	 */
	public static void sendBindDesk(String json){
		sendMessagePackingWithData(Message.MESSAGE_DEVICE_BIND, json);
	}
	
	/**
	 * 发送获取小组成员消息
	 */
	public static void sendGroupList(){
		sendMessagePackingWithImei(Message.MESSAGE_GROUP_LIST);
	}
	
	/**
	 * 发送学生登录 退出 注册信息
	 * @param loginReqVo 学生信息
	 */
	public static void sendStudentInfo(String json){
		sendMessagePackingWithData(Message.MESSAGE_STUDENT_LOGIN, json);
	}
	
	/**
	 * 发送抢答命令
	 */
	public static void sendStudentResponder(){
		sendMessagePackingWithImei(Message.MESSAGE_STUDENT_RESPONDER);
	}
	
	/**
	 * 发送编辑小组命令
	 * @param jsonObject
	 */
	public static void sendGroupEdit(String json){
		sendMessagePackingWithData(Message.MESSAGE_GROUP_EDIT, json);
	}
	
	/**
	 * 分组确认命令
	 * @param jsonObject
	 */
	public static void sendGroupConfirm(String json){
		sendMessagePackingWithData(Message.MESSAGE_GROUP_VOTE, json);
	}
	
	/**
	 * 收到作业回执
	 */
	public static void sendReplyRecivePaper(){
		sendMessagePackingWithImei(Message.MESSAGE_RECIVE_PAER);
	}
	
	/**
	 * 主动提交作业请求
	 */
	public static void sendPaperBySelf(){
		sendMessagePackingWithImei(Message.MESSAGE_SEND_PAPER);
	}
	
	/**
	 * 收到提交作业回执
	 */
	public static void sendReplyReciveSavePaper(){
		sendMessagePackingWithImei(Message.MESSAGE_RICIVE_SAVEPAPER);
	}
	
	/**
	 * 作业上传成功
	 * @param filePath 作业路径
	 * @param fileName 作业名称
	 */
	public static void sendPaperUploadSuccess(String json){
		sendMessagePackingWithData(Message.MESSAGE_SAVE_PAPER, json);
	}
	
	/**
	 * 判断apk是否需要更新,从教师端获取更新消息
	 * 有更新采用后台下载到本地,下次启动apk时进行自动后台安装
	 */
	public static void isUpdateApk(){
		sendMessagePackingWithImei(Message.MESSAGE_APK_UPTE);
	}
}
