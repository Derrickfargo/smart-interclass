package com.incito.interclass.entity.cloud;

import java.io.Serializable;
import java.util.Date;

public class PaperWork implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8829976037532682739L;
	/**
	 * 随堂测试作业
	 */
	private String type;
	private String teacher_id;
	private String author_name;
	private String quizid;
	private String path;
	private String imei;
	private String filename;
	private String filesize;
	private Date class_start;
	private Date class_end;
	private String classes;
	private String course_id;
	private String course_name;
	private String lessionid;
	private String term;

	private Date ctime;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTeacher_id() {
		return teacher_id;
	}

	public void setTeacher_id(String teacher_id) {
		this.teacher_id = teacher_id;
	}

	public String getAuthor_name() {
		return author_name;
	}

	public void setAuthor_name(String author_name) {
		this.author_name = author_name;
	}

	public String getQuizid() {
		return quizid;
	}

	public void setQuizid(String quizid) {
		this.quizid = quizid;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilesize() {
		return filesize;
	}

	public void setFilesize(String filesize) {
		this.filesize = filesize;
	}

	public Date getClass_start() {
		return class_start;
	}

	public void setClass_start(Date class_start) {
		this.class_start = class_start;
	}

	public Date getClass_end() {
		return class_end;
	}

	public void setClass_end(Date class_end) {
		this.class_end = class_end;
	}

	public String getClasses() {
		return classes;
	}

	public void setClasses(String classes) {
		this.classes = classes;
	}

	public String getCourse_id() {
		return course_id;
	}

	public void setCourse_id(String course_id) {
		this.course_id = course_id;
	}

	public String getCourse_name() {
		return course_name;
	}

	public void setCourse_name(String course_name) {
		this.course_name = course_name;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public String getLessionid() {
		return lessionid;
	}

	public void setLessionid(String lessionid) {
		this.lessionid = lessionid;
	}



}
