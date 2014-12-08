package cn.com.incito.server.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;

import cn.com.incito.interclass.po.Quiz;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.config.AppConfig;

public class PeerFeedbackUtils {
	public static Queue<List<Quiz>> getQuizQueue() {
		Application app = Application.getInstance();
		List<Quiz> quizList = app.getQuizList();
		Set<String> onlinePads = app.getOnlineDevice();
		Queue<List<Quiz>> quizQueue = initQuizQueue(onlinePads);

		int quizCount = getQuizRepeatCount();
		if (onlinePads.size() > quizList.size()) {
			++quizCount;
		}

		for (Quiz quiz : quizList) {// 所有作业
			for (int j = 0; j < quizCount; j++) {
				List<Quiz> quizzes = quizQueue.poll();
				quizzes.add(quiz);
				quizQueue.offer(quizzes);
			}
		}
		return quizQueue;
	}

	private static int getQuizRepeatCount() {
		Properties props = AppConfig.getProperties();
		return Integer.parseInt(props.getProperty("quiz_repeat_count"));
	}

	private static Queue<List<Quiz>> initQuizQueue(Set<String> onlinePads) {
		Queue<List<Quiz>> quizQueue = new LinkedList<List<Quiz>>();
		for (int i = 0; i < onlinePads.size(); i++) {
			quizQueue.offer(new ArrayList<Quiz>());
		}
		return quizQueue;
	}
}
