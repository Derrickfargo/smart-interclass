package cn.com.incito.classroom.vo;

import java.util.Date;
import java.util.List;


public class Group {
	
	/**
	 * 
	 */
	private String medals;
	private int score;
	private int id;
	private String name;
	private int logo ;
	private String slogan;
	private int teacherId;
	private int classId;
	private Date ctime;

	private List<Device> devices;
	private List<Student> students;
	private String teacherName;
	
	
	public String getMedals() {
		return medals;
	}
	public void setMedals(String medals) {
		this.medals = medals;
	}
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
	public int getLogo() {
		return logo;
	}
	public void setLogo(int logo) {
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
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

}
