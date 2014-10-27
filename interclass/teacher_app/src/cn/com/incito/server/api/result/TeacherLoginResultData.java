package cn.com.incito.server.api.result;

import java.util.List;

import cn.com.incito.interclass.po.Classes;
import cn.com.incito.interclass.po.Course;
import cn.com.incito.interclass.po.Teacher;

public class TeacherLoginResultData implements IApiResultData {
	private Teacher teacher;
	private Course course;
	private List<Course> courses;
	private List<Classes> classes;

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	public List<Classes> getClasses() {
		return classes;
	}

	public void setClasses(List<Classes> classes) {
		this.classes = classes;
	}

}
