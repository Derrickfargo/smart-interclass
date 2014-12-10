package cn.com.incito.interclass.po;

import java.util.ArrayList;
import java.util.List;

public class QuizFeedback {
	private String quizId;
	private int score;
	private String name;
	private List<String> feedbackList = new ArrayList<String>();

	public String getQuizId() {
		return quizId;
	}

	public void setQuizId(String quizId) {
		this.quizId = quizId;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getFeedbackList() {
		return feedbackList;
	}

	public void setFeedbackList(List<String> feedbackList) {
		this.feedbackList = feedbackList;
	}

}
