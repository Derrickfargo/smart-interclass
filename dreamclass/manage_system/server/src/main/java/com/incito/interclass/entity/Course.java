package com.incito.interclass.entity;

import java.io.Serializable;
import java.util.Date;

public class Course implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7731906102194126599L;

	private int id;
	private int sum;
	private String name;
	private String intro;
	private int ctime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSum() {
		return sum;
	}

	public void setSum(int sum) {
		this.sum = sum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public int getCtime() {
		return ctime;
	}

	public void setCtime(int ctime) {
		this.ctime = ctime;
	}

}
