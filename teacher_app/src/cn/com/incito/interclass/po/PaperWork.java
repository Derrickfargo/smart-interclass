package cn.com.incito.interclass.po;

import java.io.Serializable;
import java.util.Date;

public class PaperWork implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8829976037532682739L;
	/**
	 * 随堂测试作业
	 */
	private String type;
	private String student_id;
	private String author_name;
	private String quizid;
	private String path;
	private String filename;
	private String filesize;
	private Date ctime;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStudent_id() {
		return student_id;
	}
	public void setStudent_id(String student_id) {
		this.student_id = student_id;
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
	public Date getCtime() {
		return ctime;
	}
	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}


}
