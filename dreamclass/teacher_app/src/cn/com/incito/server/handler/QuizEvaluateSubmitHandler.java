package cn.com.incito.server.handler;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.com.incito.interclass.po.Quiz;
import cn.com.incito.interclass.po.QuizFeedback;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.MessageHandler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class QuizEvaluateSubmitHandler extends MessageHandler {
	private Logger logger = Logger.getLogger(QuizEvaluateSubmitHandler.class
			.getName());

	@Override
	public void handleMessage() {
		logger.info("收到互评提交消息:" + data);
		Application app = Application.getInstance();

		String imei = data.getString("imei");// 评论人的IMEI
		List<Student> students = app.getStudentByImei(imei);
		StringBuffer name = new StringBuffer();
		for (Student student : students) {
			name.append(student.getName());
			name.append(" ");
		}

		Map<String, QuizFeedback> feedbackMap = app.getQuizFeedbackMap();
		JSONArray feedbacks = data.getJSONArray("feedback");
		for (int i = 0; i < feedbacks.size(); i++) {
			JSONObject json = (JSONObject) feedbacks.get(i);
			String quizId = json.getString("id");// 被评作业的id
			int level = json.getIntValue("score");// 作业名次
			QuizFeedback feedback = feedbackMap.get(quizId);
			if (feedback == null) {
				feedback = new QuizFeedback();
				feedback.setQuizId(quizId);
				Quiz quiz = app.getQuizMap().get(quizId);
				feedback.setName(quiz.getName());
				feedback.setQuiz(quiz);
				feedbackMap.put(quizId, feedback);
			}
			feedback.setScore(getScore(level));
			feedback.getFeedbackList().add(name.toString() + "给予了 " + getLevel(level));
		}
	}
	
	private int getScore(int level) {
		switch (level) {
		case 1:
			return 5;
		case 2:
			return 4;
		case 3:
			return 3;
		case 4:
			return 2;
		case 5:
			return 1;
		}
		return 0;
	}
	
	private String getLevel(int level) {
		switch (level) {
		case 1:
			return "第一名";
		case 2:
			return "第二名";
		case 3:
			return "第三名";
		case 4:
			return "第四名";
		default:
			return "第五名";
		}
	}
}
