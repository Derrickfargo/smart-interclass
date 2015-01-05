package cn.com.incito.server.handler;


import java.util.List;

import org.apache.log4j.Logger;

import cn.com.incito.interclass.po.Student;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.utils.UIHelper;

public class ResponderHandler extends MessageHandler{
	private Logger log = Logger.getLogger(ResponderHandler.class.getName());
	
	@Override
	protected void handleMessage() {
		String imei = data.getString("imei");
		log.info("抢答学生imei："+imei);
		Application app  = Application.getInstance();
		List<Student>students = app.getStudentByImei(imei);
		if(students.size()==0){
		log.error("抢答获取学生列表失败，无效imei");
		Application.isOnResponder=false;
		UIHelper.sendResponderMessage(false);
		return;
		}
		else if(Application.isOnResponder){
			Application.isOnResponder=false;
			UIHelper.sendResponderMessage(false);			
			app.setResponderStudents(students);
			app.refreshResponder();
		}
		else{
			log.info("抢答已经结束，响应的学生小组为："+students.get(0).getName());
		}
	}

}
