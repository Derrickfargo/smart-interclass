package cn.com.incito.server.handler;


import java.util.List;

import org.apache.log4j.Logger;

import cn.com.incito.interclass.po.Student;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.MessageHandler;

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
		}
		else{
			app.setResponderStudents(students);
		}
	}

}
