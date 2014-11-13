package cn.com.incito.socket.handler;

import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.classroom.vo.Student;
import cn.com.incito.common.utils.AndroidUtil;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.ConnectionManager;
import cn.com.incito.socket.core.MessageHandler;
/**
 * 登陆处理hanlder
 * Created by liushiping on 2014/7/28.
 */
public class LoginHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+"LoginHandler.class:收到登陆回复：" + data.toJSONString());
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+"连接建立成功,开始启动心跳!");
		String server_ip = data.getString("server_ip");
		String server_port = data.getString("server_port");
		Constants.setSERVER_IP(server_ip);
		Constants.setSERVER_PORT(server_port);
		//TODO 有可能这台pad没有绑定学生，
		Student student = data.getObject("student", Student.class);
		if(student==null){
			UIHelper.getInstance().getWifiSelectorActivity().showToast();
			return;
		}
		MyApplication.getInstance().setStudent(student);
		
		//判断学生状态跳转至不同的界面
		int state =data.getIntValue("state");
		
		if(state == 1 ){
			UIHelper.getInstance().showClassReadyActivity();
			AppManager.getAppManager().currentActivity().finish();
			return;
		}
		if(state == 2){
			UIHelper.getInstance().showGroupSelect(data.getString("group"));
			AppManager.getAppManager().currentActivity().finish();
			return;
		}
		if(state == 3){
			byte[] imageByte = data.getBytes("quiz");
			UIHelper.getInstance().showDrawBoxActivity(imageByte);
			AppManager.getAppManager().currentActivity().finish();
			return;
		}
		if(state == 4){
			UIHelper.getInstance().showClassingActivity();
			AppManager.getAppManager().currentActivity().finish();
			return;
		}
		
		
		
		//启动心跳检测
		ConnectionManager.getInstance(message.getChannel());
	}

}
