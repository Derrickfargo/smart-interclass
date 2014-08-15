package cn.com.incito.interclass.po;

import java.io.Serializable;

public class Quiz implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1469903255940391815L;
	private String id;
	private String imei;
	private String name;
	private String quizUrl;
	private String thumbnail;

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}


}
