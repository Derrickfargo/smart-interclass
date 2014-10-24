package com.incito.interclass.app.result;

import java.util.List;

import com.incito.interclass.entity.Classes;
import com.incito.interclass.entity.Device;
import com.incito.interclass.entity.Group;
import com.incito.interclass.entity.Student;
import com.incito.interclass.entity.Table;

public class TeacherGroupResultData implements IApiResultData {
	private List<Group> groups;
	private List<Table> tables;
	private List<Device> devices;
	private List<Student> students;
	private Classes classes;

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

	public List<Device> getDevices() {
		return devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}

	public Classes getClasses() {
		return classes;
	}

	public void setClasses(Classes classes) {
		this.classes = classes;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

}
