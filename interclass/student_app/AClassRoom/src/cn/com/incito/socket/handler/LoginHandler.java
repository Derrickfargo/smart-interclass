package cn.com.incito.socket.handler;

import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.vo.Student;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.ConnectionManager;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.wisdom.sdk.log.WLog;
/**
 * 登陆处理hanlder
 * Created by liushiping on 2014/7/28.
 */
public class LoginHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		WLog.i(LoginHandler.class, "收到登陆回复：" + data);
		WLog.i(LoginHandler.class, "连接建立成功,开始启动心跳!");
		String server_ip = data.getString("server_ip");
		String server_port = data.getString("server_port");
		//TODO 有可能这台pad没有绑定学生，
		Student student = data.getObject("student", Student.class);
		MyApplication.getInstance().setStudent(student);
		//TODO 需要添加server_ip，server_port到Application
		UIHelper.getInstance().showClassReadyActivity();
		//启动心跳检测
		ConnectionManager.getInstance(message.getChannel());
	}

}
