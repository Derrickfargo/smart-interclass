package cn.com.incito.interclass.po;

import java.util.ArrayList;
import java.util.List;

/**
 * 一个pad的互评反馈
 * @author 刘世平
 *
 */
public class QuizFeedback {
	private String quizId;
	private String name;
	private int score;
	private Quiz quiz;
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

	public Quiz getQuiz() {
		return quiz;
	}

	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}

	public List<String> getFeedbackList() {
		return feedbackList;
	}

	public void setFeedbackList(List<String> feedbackList) {
		this.feedbackList = feedbackList;
	}

}
