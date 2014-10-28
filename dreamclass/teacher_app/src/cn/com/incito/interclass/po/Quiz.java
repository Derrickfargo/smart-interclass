package cn.com.incito.interclass.po;

import java.io.Serializable;

public class Quiz implements Serializable,Comparable<Quiz> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1469903255940391815L;
	private String id;
	private int groupId;
	private String imei;
	private String name;
	private String quizUrl;
	private String lessionid;
	private String thumbnail;
	private Group group;
	private float time;

	
	public float getTime() {
		return time;
	}

	
	public void setTime(float time) {
		this.time = time;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
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

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public String getLessionid() {
		return lessionid;
	}


	public void setLessionid(String lessionid) {
		this.lessionid = lessionid;
	}


	@Override
	public int compareTo(Quiz o) {
		return groupId - o.getGroupId();
	}

}
