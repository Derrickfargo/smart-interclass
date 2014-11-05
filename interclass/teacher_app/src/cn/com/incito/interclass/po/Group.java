package cn.com.incito.interclass.po;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Group implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6517861100227656945L;
	private String medals;
	private int score;
	private int id;
	private String name;
	private String logo;
	private String slogan;
	private int teacherId;
	private int classId;
	private Date ctime;
	private String captainid;
	private List<Device> devices;
	private List<Student> students;
	private List<Quiz> quizs;
	private String teacherName;

	public int getId() {
		return id;
	}

	public int getScore() {
		return score;
	}

	public String getCaptainid() {
		return captainid;
	}

	public void setCaptainid(String captainid) {
		this.captainid = captainid;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getMedals() {
		return medals;
	}

	public void setMedals(String medals) {
		this.medals = medals;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getSlogan() {
		return slogan;
	}

	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}

	public int getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}

	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public List<Device> getDevices() {
		return devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public List<Quiz> getQuizs() {
		return quizs;
	}

	public void setQuizs(List<Quiz> quizs) {
		this.quizs = quizs;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Group other = (Group) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
