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
import cn.com.incito.interclass.po.Device;
import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Room;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.interclass.po.Table;
import cn.com.incito.interclass.po.Teacher;
import cn.com.incito.interclass.ui.Login;
import cn.com.incito.interclass.ui.MainFrame;
import cn.com.incito.server.core.CoreSocket;

public class Application {
	private static Application instance;
	private Room room;// 当前上课的教室，教师登陆完后初始化
	private Teacher teacher;// 当前登录的老师，教师登陆完后初始化
	private Course course;// 当前上课的课程，教师登陆完后初始化
	private Classes classes;// 当前上课的班级，教师登陆完后初始化

	private List<Group> groupList = new ArrayList<Group>();// 本堂课的所有分组
	private List<Table> tableList = new ArrayList<Table>();// 本教室所有的桌子
	private List<Device> deviceList = new ArrayList<Device>();// 本教室所以的Device
	private Set<String> onlineDevice = new HashSet<String>();
	private Set<Student> onlineStudent = new HashSet<Student>();

	private Map<Group, List<SocketChannel>> clientChannel;// 保存每组和已登录的socket
	private CoreSocket coreSocket;
	private Map<String, Student> loginedStudent;// 已登录的学生，key:name+number，value:Student

	/**
	 * IMEI和设备的对应关系(key:imei,value:Device)，教师登陆完后初始化
	 */
	private Map<String, Device> imeiDevice;

	/**
	 * tableId和Table的对应关系(key:tableId,value:Table)，教师登陆完后初始化
	 */
	private Map<Integer, Table> tableMap;

	/**
	 * Device id和Table的对应关系(key:deviceId,value:Table)，教师登陆完后初始化
	 */
	private Map<Integer, Table> deviceTable;

	/**
	 * Table和Device的对应关系 (key:tableId,value:List<Device>)，教师登陆完后初始化
	 */
	private Map<Integer, List<Device>> tableDevice;

	/**
	 * Table和Group的对应关系 (key:tableId,value:Group)，教师登陆完后初始化
	 */
	private Map<Integer, Group> tableGroup;

	private Application() {
		loginedStudent = new HashMap<String, Student>();
		imeiDevice = new HashMap<String, Device>();
		tableMap = new HashMap<Integer, Table>();
		deviceTable = new HashMap<Integer, Table>();
		tableDevice = new HashMap<Integer, List<Device>>();
		tableGroup = new HashMap<Integer, Group>();

		clientChannel = new HashMap<Group, List<SocketChannel>>();
		new Login();
	}

	public static Application getInstance() {
		if (instance == null) {
			instance = new Application();
		}
		return instance;
	}

	/**
	 * 初始化映射关系
	 * 
	 * @param devices
	 * @param tables
	 */
	public void initMapping(List<Device> devices, List<Table> tables,
			List<Group> groups) {
		this.initImeiDevice(devices);
		this.initTableMap(tables);
		this.initTableGroup(groups);
		this.initDeviceTable();
		this.initTableDevice();
	}

	/**
	 * 初始化IMEI设备映射
	 * 
	 * @param devices
	 */
	private void initImeiDevice(List<Device> devices) {
		deviceList = devices;
		for (Device device : devices) {
			imeiDevice.put(device.getImei(), device);
		}
	}

	/**
	 * 初始化课桌映射
	 * 
	 * @param tables
	 */
	private void initTableMap(List<Table> tables) {
		tableList = tables;
		for (Table table : tables) {
			tableMap.put(table.getId(), table);
		}
	}

	/**
	 * 初始化课桌分组映射
	 * 
	 * @param groups
	 */
	private void initTableGroup(List<Group> groups) {
		groupList = groups;
		for (Group group : groups) {
			tableGroup.put(group.getTableId(), group);
		}
	}

	/**
	 * 初始化设备课桌映射
	 */
	private void initDeviceTable() {
		for (Device device : deviceList) {
			Table table = tableMap.get(device.getTableId());
			deviceTable.put(device.getId(), table);
		}
	}

	/**
	 * 初始化课桌设备映射
	 */
	private void initTableDevice() {
		for (Table table : tableList) {
			List<Device> deviceList = tableDevice.get(table.getId());
			if (deviceList == null) {
				deviceList = new ArrayList<Device>();
			}
			for (Device device : deviceList) {
				if (device.getTableId() == table.getId()) {
					deviceList.add(device);
				}
			}
			tableDevice.put(table.getId(), deviceList);
		}
	}

	public List<Table> getTableList() {
		return tableList;
	}

	public void addDevice(Integer id, Device device) {
		List<Device> deviceList = tableDevice.get(id);
		if (deviceList == null) {
			deviceList = new ArrayList<Device>();
		}
		deviceList.add(device);
		tableDevice.put(id, deviceList);
	}

	public void addGroup(Group group) {
		tableGroup.put(group.getId(), group);
		for (Group aGroup : groupList) {
			if (aGroup.getId() == group.getId()) {
				groupList.remove(aGroup);
				break;
			}
		}
		groupList.add(group);
	}

	public Map<String, Student> getLoginedStudent() {
		return loginedStudent;
	}

	public void setLoginedStudent(Map<String, Student> loginedStudent) {
		this.loginedStudent = loginedStudent;
	}

	public List<Group> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<Group> groupList) {
		this.groupList = groupList;
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

	public List<Device> getDeviceList() {
		return deviceList;
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

	public Map<String, Device> getImeiDevice() {
		return imeiDevice;
	}

	public Map<Integer, Table> getTableMap() {
		return tableMap;
	}

	public Map<Integer, Table> getDeviceTable() {
		return deviceTable;
	}

	public Map<Integer, List<Device>> getTableDevice() {
		return tableDevice;
	}

	public Map<Integer, Group> getTableGroup() {
		return tableGroup;
	}

	public Set<String> getOnlineDevice() {
		return onlineDevice;
	}

	public void setOnlineDevice(Set<String> onlineDevice) {
		this.onlineDevice = onlineDevice;
	}

	public Set<Student> getOnlineStudent() {
		return onlineStudent;
	}

	public void setOnlineStudent(Set<Student> onlineStudent) {
		this.onlineStudent = onlineStudent;
	}

	public void refreshMainFrame() {
		new Thread() {
			public void run() {
				MainFrame.getInstance().refresh();
			}
		}.start();
	}
}
