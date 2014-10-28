package com.incito.interclass.app.result;

import java.util.List;

import com.incito.interclass.entity.Classes;
import com.incito.interclass.entity.Course;
import com.incito.interclass.entity.Group;
import com.incito.interclass.entity.Teacher;

public class TeacherLoginResultData implements IApiResultData {
	private Teacher teacher;
	private Course course;
	private List<Group> groupList;
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

	public List<Group> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<Group> groupList) {
		this.groupList = groupList;
	}

	public List<Classes> getClasses() {
		return classes;
	}

	public void setClasses(List<Classes> classes) {
		this.classes = classes;
	}

}
