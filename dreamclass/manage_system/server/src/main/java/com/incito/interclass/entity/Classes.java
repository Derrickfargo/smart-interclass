package com.incito.interclass.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Classes implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4858685768837161501L;

	private int id;
	private int number;
	private int schoolId;
	private int year;
	private int teacherId;
	private Date ctime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(int schoolId) {
		this.schoolId = schoolId;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public String getClassName() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		year = year - this.year;
		if (month >= 9) {
			year += 1;
		}
		String className = "%d年级%d班";
		return String.format(className, year, number);
	}
}
