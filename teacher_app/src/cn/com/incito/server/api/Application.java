package cn.com.incito.server.api;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;

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

public class Application {
	private static Application instance;
	private String quizId;   //考试流水号
	private Room room;// 当前上课的教室，教师登陆完后初始化
	private Teacher teacher;// 当前登录的老师，教师登陆完后初始化
	private Course course;// 当前上课的课程，教师登陆完后初始化
	private Classes classes;// 当前上课的班级，教师登陆完后初始化

	private List<Group> groupList = new ArrayList<Group>();// 本堂课的所有分组
	private List<Table> tableList = new ArrayList<Table>();// 本教室所有的桌子
	private List<Device> deviceList = new ArrayList<Device>();// 本教室所有的Device
	private Set<String> onlineDevice = new HashSet<String>();
	private Set<Student> onlineStudent = new HashSet<Student>();

	private Map<Integer, List<SocketChannel>> groupChannel;// 保存每组和已登录的socket
	private Map<String, SocketChannel> clientChannel;// 保存所有设备登陆的socket，imei和socket
	private Map<String, Student> loginedStudent;// 已登录的学生，key:name+number，value:Student
	private Map<Integer, Group> groupMap = new HashMap<Integer, Group>();
	private Map<Integer, JSONObject> tempGroup = new HashMap<Integer, JSONObject>();// 修改的分组信息
	private Map<Integer, List<Integer>> tempVote = new HashMap<Integer, List<Integer>>();// 小组的投票信息

	/**
	 * IMEI和设备的对应关系(key:imei,value:Device)，教师登陆完后初始化
	 */
	private Map<String, Device> imeiDevice;

	/**
	 * tableId和Table的对应关系(key:tableId,value:Table)，教师登陆完后初始化
	 */
	private Map<Integer, Table> tableMap;

	private Map<Integer, Table> tableNumberMap;

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
		tableNumberMap = new HashMap<Integer, Table>();
		deviceTable = new HashMap<Integer, Table>();
		tableDevice = new HashMap<Integer, List<Device>>();
		tableGroup = new HashMap<Integer, Group>();

		clientChannel = new HashMap<String, SocketChannel>();
		groupChannel = new HashMap<Integer, List<SocketChannel>>();
		new Login();
	}

	public Map<String, SocketChannel> getClientChannel() {
		return clientChannel;
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
		this.initTableNumberMap(tables);
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
		imeiDevice.clear();
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
		tableMap.clear();
		for (Table table : tables) {
			tableMap.put(table.getId(), table);
		}
	}

	/**
	 * 初始化课桌映射
	 * 
	 * @param tables
	 */
	private void initTableNumberMap(List<Table> tables) {
		tableList = tables;
		tableNumberMap.clear();
		for (int i = 0; i < 16; i++) {
			Table table = new Table();
			table.setNumber(i + 1);
			tableNumberMap.put(table.getNumber(), table);
		}
		for (Table table : tables) {
			tableNumberMap.put(table.getNumber(), table);
		}
	}

	/**
	 * 初始化课桌分组映射
	 * 
	 * @param groups
	 */
	private void initTableGroup(List<Group> groups) {
		groupList = groups;
		tableGroup.clear();
		for (Group group : groups) {
			tableGroup.put(group.getTableId(), group);
		}
	}

	/**
	 * 初始化设备课桌映射
	 */
	private void initDeviceTable() {
		deviceTable.clear();
		for (Device device : deviceList) {
			Table table = tableMap.get(device.getTableId());
			deviceTable.put(device.getId(), table);
		}
	}

	/**
	 * 初始化课桌设备映射
	 */
	private void initTableDevice() {
		tableDevice.clear();
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
		if (!deviceList.contains(device)) {
			deviceList.add(device);
		}
		tableDevice.put(id, deviceList);
	}

	public void addGroup(Group group) {
		// TODO 这里有修改过，以前写成了tableGroup.put(group.getId(), group);
		tableGroup.put(group.getTableId(), group);
		groupMap.put(group.getId(), group);

		Iterator<Group> it = groupList.iterator();
		while (it.hasNext()) {
			Group aGroup = it.next();
			if (aGroup.getId() == group.getId()) {
				it.remove();
				break;
			}
		}
		groupList.add(group);
	}

	public Group getGroupById(Integer id) {
		return groupMap.get(id);
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

	/**
	 * @param imei
	 * @param channel
	 */
	public void addSocketChannel(String imei, SocketChannel channel) {
		clientChannel.put(imei, channel);
	}

	public void addSocketChannel(Integer groupId, SocketChannel channel) {
		List<SocketChannel> channels = groupChannel.get(groupId);
		if (channels == null) {
			channels = new ArrayList<SocketChannel>();
		}
		if (channels.contains(channel)) {
			channels.remove(channel);
		}
		channels.add(channel);
		groupChannel.put(groupId, channels);
	}

	public List<SocketChannel> getClientChannelByGroup(Integer groupId) {
		return groupChannel.get(groupId);
	}

	public Map<String, Device> getImeiDevice() {
		return imeiDevice;
	}

	public Map<Integer, Table> getTableMap() {
		return tableMap;
	}

	public Map<Integer, Table> getTableNumberMap() {
		return tableNumberMap;
	}

	public void setTableNumberMap(Map<Integer, Table> tableNumberMap) {
		this.tableNumberMap = tableNumberMap;
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

	public Map<Integer, JSONObject> getTempGroup() {
		return tempGroup;
	}

	public Map<Integer, List<Integer>> getTempVote() {
		return tempVote;
	}

	public void refreshFrame() {
		MainFrame.getInstance().refreshUI();
	}

	public String getQuizId() {
		return quizId;
	}

	public void setQuizId(String quizId) {
		this.quizId = quizId;
	}

}
