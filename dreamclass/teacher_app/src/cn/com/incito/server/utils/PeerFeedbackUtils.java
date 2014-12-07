package cn.com.incito.server.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import cn.com.incito.interclass.po.Quiz;

public class PeerFeedbackUtils {
	private static int count = 3;// 每份作业出现次数
	
	public static Queue<List<Quiz>> getQuizQueue() {
		List<Quiz> quizList = getQuizList();
		List<String> padList = getPadList();
		Queue<List<Quiz>> quizQueue = initQuizQueue();

		if (padList.size() > quizList.size()) {
			++count;
		}
		
		for (Quiz quiz : quizList) {//所有作业
			for (int j = 0; j < count; j++) {//每个作业出现count次
				List<Quiz> quizzes = quizQueue.poll();
				quizzes.add(quiz);
				quizQueue.offer(quizzes);
			}
		}
		return quizQueue;
	}

	private static Queue<List<Quiz>> initQuizQueue() {
		Queue<List<Quiz>> quizQueue = new LinkedList<List<Quiz>>();
		List<String> padList = getPadList();
		for (int i = 0; i < padList.size(); i++) {
			quizQueue.offer(new ArrayList<Quiz>());
		}
		return quizQueue;
	}
	
	private static List<Quiz> getQuizList() {
		List<Quiz> quizList = new ArrayList<Quiz>();
		for (int i = 0; i < 32; i++) {
			Quiz quiz = new Quiz();
			quiz.setName("quiz" + (i + 1));
			quizList.add(quiz);
		}
		return quizList;
	}

	//Pad的IMEI
	private static List<String> getPadList() {
		List<String> padList = new ArrayList<String>();
		for (int i = 0; i < 32; i++) {
			padList.add(String.valueOf(i + 1));
		}
		return padList;
	}
}
