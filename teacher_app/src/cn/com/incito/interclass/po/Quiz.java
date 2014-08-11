package cn.com.incito.interclass.po;

import java.io.Serializable;

public class Quiz implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1469903255940391815L;
	private int id;
	private String imei;
	private String quizUrl;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getQuizUrl() {
		return quizUrl;
	}

	public void setQuizUrl(String quizUrl) {
		this.quizUrl = quizUrl;
	}


}
