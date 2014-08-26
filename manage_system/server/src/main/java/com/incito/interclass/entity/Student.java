package com.incito.interclass.entity;


public class Student extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8118085892876963773L;

	private int id;
	private String number;
	private String avatar;
	private int score;
	
	
	public int getScore() {
		return score;
	}

	
	public void setScore(int score) {
		this.score = score;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

}
