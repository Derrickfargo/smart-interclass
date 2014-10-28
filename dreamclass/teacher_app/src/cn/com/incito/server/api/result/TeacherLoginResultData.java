package cn.com.incito.server.api.result;

import java.util.List;

import cn.com.incito.interclass.po.Course;
import cn.com.incito.interclass.po.Room;
import cn.com.incito.interclass.po.Teacher;

public class TeacherLoginResultData implements IApiResultData {
	private Teacher teacher;
	private Room room;
	private List<Course> courses;

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

}
