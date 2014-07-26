package cn.com.incito.server.api;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.com.incito.interclass.po.Classes;
import cn.com.incito.interclass.po.Course;
import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Room;
import cn.com.incito.interclass.po.Table;
import cn.com.incito.interclass.po.Teacher;
import cn.com.incito.interclass.ui.Login;
import cn.com.incito.server.core.CoreSocket;

public class Application {

	private static Application instance;
	private Room room;// 当前上课的教室
	private Teacher teacher;// 当前登录的老师
	private Course course;// 当前上课的课程
	private Classes classes;// 当前上课的班级
	private Map<String, List<String>> tables;// 保存每个课桌和课桌上已登录的pad
	private Set<Group> groupSet = new HashSet<Group>();// 本堂课的分组情况
	private List<Table> tableList = new ArrayList<Table>();

	private List<Course> courseList;
	private Map<Group, List<SocketChannel>> clientChannel;// 保存每组和已登录的socket
	private CoreSocket coreSocket;

	private Application() {
		tables = new HashMap<String, List<String>>();
		clientChannel = new HashMap<Group, List<SocketChannel>>();
		new Login();
	}

	public static Application getInstance() {
		if (instance == null) {
			instance = new Application();
		}
		return instance;
	}

	public List<Table> getTableList() {
		return tableList;
	}

	public void setTableList(List<Table> tableList) {
		this.tableList = tableList;
	}

	public void addDevice(String tableNumber, String imei) {
		List<String> imeiList = tables.get(tableNumber);
		if (imeiList == null) {
			imeiList = new ArrayList<String>();
		}
		imeiList.add(imei);
		tables.put(tableNumber, imeiList);
	}

	public Set<Group> getGroupSet() {
		return groupSet;
	}

	public void setGroupSet(Set<Group> groupSet) {
		this.groupSet = groupSet;
	}

	public List<Course> getCourseList() {
		return courseList;
	}

	public void setCourseList(List<Course> courseList) {
		this.courseList = courseList;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Classes getClasses() {
		return classes;
	}

	public void setClasses(Classes classes) {
		this.classes = classes;
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
