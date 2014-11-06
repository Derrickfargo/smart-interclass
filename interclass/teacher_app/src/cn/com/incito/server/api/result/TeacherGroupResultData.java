package cn.com.incito.server.api.result;

import java.util.List;
import java.util.Set;

import cn.com.incito.interclass.po.Classes;
import cn.com.incito.interclass.po.Course;
import cn.com.incito.interclass.po.Device;
import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Student;

public class TeacherGroupResultData implements IApiResultData {
	private Course course;
	private Classes classes;
	private Set<Group> groups;
	private List<Device> devices;
	private List<Student> students;

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Classes getClasses() {
		return classes;
	}

	public void setClasses(Classes classes) {
		this.classes = classes;
	}

	public Set<Group> getGroups() {
		return groups;
	}

	public void setGroups(Set<Group> groups) {
		this.groups = groups;
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

}
