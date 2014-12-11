package cn.com.incito.server.handler;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.com.incito.interclass.po.QuizFeedback;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.MessageHandler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class QuizEvaluateSubmitHandler extends MessageHandler {
	private Logger logger = Logger.getLogger(QuizEvaluateSubmitHandler.class.getName());
	
	@Override
	public void handleMessage() {
		logger.info("收到互评提交消息:" + data);
		Application app = Application.getInstance();
		
		String imei = data.getString("imei");//评论人的IMEI
		List<Student> students = app.getStudentByImei(imei);
		StringBuffer name = new StringBuffer();
		for(Student student : students){
			name.append(student.getName());
			name.append(" ");
		}
		
		Map<String, QuizFeedback> feedbackMap = app.getQuizFeedbackMap();
		JSONArray feedbacks = data.getJSONArray("feedback");
		for (int i = 0; i < feedbacks.size(); i++) {
			QuizFeedback feedback = new QuizFeedback();
			JSONObject json = (JSONObject) feedbacks.get(i);
			String id = json.getString("id");//被评作业的id
			int score = json.getIntValue("score");
			//TODO 
//			feedback.setName(name);
		}
	}
}
