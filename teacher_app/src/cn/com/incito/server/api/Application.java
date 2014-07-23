package cn.com.incito.server.api;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.incito.interclass.po.Course;
import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Room;
import cn.com.incito.interclass.po.Teacher;
import cn.com.incito.interclass.ui.Login;
import cn.com.incito.server.core.CoreSocket;

public class Application {

	private static Application instance;
	private Room room;//当前上课的教室
	private Teacher teacher;//当前登录的老师
	private Course course;//当前课程
	private Map<Group, List<SocketChannel>> clientChannel;
	private CoreSocket coreSocket;

	private Application() {
		clientChannel = new HashMap<Group, List<SocketChannel>>();
		new Login();
	}

	public static Application getInstance() {
		if (instance == null) {
			instance = new Application();
		}
		return instance;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

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

	public Map<Group, List<SocketChannel>> getClientChannel() {
		return clientChannel;
	}

	public void putClientChannel(Group group, List<SocketChannel> channels) {
		clientChannel.put(group, channels);
	}

	public CoreSocket getCoreSocket() {
		return coreSocket;
	}

	public void setCoreSocket(CoreSocket coreSocket) {
		this.coreSocket = coreSocket;
	}
}
