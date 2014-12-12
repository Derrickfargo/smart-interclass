package cn.com.incito.classroom.vo;

import android.graphics.drawable.Drawable;


public class EvaluateVo {
	public String id;
	public int score;
	public Drawable paperPic;
	
	
	public String getQuizId() {
		return id;
	}
	
	public void setQuizId(String quizId) {
		this.id = quizId;
	}
	
	
	public int getSelectNumber() {
		return score;
	}
	
	public void setSelectNumber(int selectNumber) {
		this.score = selectNumber;
	}
	
	public Drawable getPaperPic() {
		return paperPic;
	}
	
	public void setPaperPic(Drawable paperPic) {
		this.paperPic = paperPic;
	}
	
}
