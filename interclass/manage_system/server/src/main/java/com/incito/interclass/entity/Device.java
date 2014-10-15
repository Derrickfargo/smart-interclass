package com.incito.interclass.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Device implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5030861381666684624L;
	private int id;
	private String imei;
	private Date ctime;

	private int year;
	private int classNumber;

	private String schoolName;
	private String studentName;

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

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
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
		return String.format(className, year, classNumber);
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getClassNumber() {
		return classNumber;
	}

	public void setClassNumber(int classNumber) {
		this.classNumber = classNumber;
	}

}
