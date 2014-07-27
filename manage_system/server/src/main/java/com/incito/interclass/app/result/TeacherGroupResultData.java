package com.incito.interclass.app.result;

import java.util.List;

import com.incito.interclass.entity.Classes;
import com.incito.interclass.entity.Course;
import com.incito.interclass.entity.Group;
import com.incito.interclass.entity.Table;

public class TeacherGroupResultData implements IApiResultData {
	private List<Group> groups;
	private List<Table> tables;
	private Classes classes;
	private Course course;

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public List<Table> getTables() {
		return tables;
	}

	public void setTables(List<Table> tables) {
		this.tables = tables;
	}

	public Classes getClasses() {
		return classes;
	}

	public void setClasses(Classes classes) {
		this.classes = classes;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

}
