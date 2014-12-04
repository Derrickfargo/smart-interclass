package cn.com.incito.server.api;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JOptionPane;

import cn.com.incito.interclass.po.Classes;
import cn.com.incito.interclass.po.Course;
import cn.com.incito.interclass.po.Device;
import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Quiz;
import cn.com.incito.interclass.po.Room;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.interclass.po.Table;
import cn.com.incito.interclass.po.Teacher;
import cn.com.incito.interclass.ui.FloatIcon;
import cn.com.incito.interclass.ui.Login;
import cn.com.incito.interclass.ui.MainFrame;
import cn.com.incito.server.config.Constants;
import cn.com.incito.server.utils.FileUtils;

import com.alibaba.fastjson.JSONObject;

public class Application {
	public boolean isLockScreen;
	private Font definedFont;
	
	public boolean isLockScreen() {
		return isLockScreen;
	}

	
	public void setLockScreen(boolean isLockScreen) {
		this.isLockScreen = isLockScreen;
	}

	private static Application instance;
	private boolean isGrouping = false;
    private FloatIcon floatIcon;
    private String quizId;   //考试流水号
    /**
	 * 课堂id
	 */
	private String lessionid;
	private String mac;//当前登录的mac地址
    private Room room;// 当前上课的教室，教师登陆完后初始化
    private Teacher teacher;// 当前登录的老师，教师登陆完后初始化
    private Course course;// 当前上课的课程，教师登陆完后初始化
    private Classes classes;// 当前上课的班级，教师登陆完后初始化

    public static boolean isOnClass;//正在上课
    public static boolean hasQuiz;//是否在作业
	public static boolean isOnResponder;//是否在抢答中
    private List<Group> groupList = new ArrayList<Group>();// 本堂课的所有分组
    private List<Table> tableList = new ArrayList<Table>();// 本教室所有的桌子
    private List<Device> deviceList = new ArrayList<Device>();// 本教室所有的Device
    private Set<String> onlineDevice = new HashSet<String>();
    private Set<Student> onlineStudent = new HashSet<Student>();
    private List<Course> courses = new ArrayList<Course>();//所有的课程
    private List<Room> rooms = new ArrayList<Room>();
    private Map<Integer, List<SocketChannel>> groupChannel;// 保存每组和已登录的socket
    private Map<String, List<Student>> imeiStudent = new HashMap<String, List<Student>>();
    private Map<String, SocketChannel> clientChannel;// 保存所有设备登陆的socket，imei和socket
    private Map<Integer, Group> groupMap = new HashMap<Integer, Group>();
    private Map<Integer, JSONObject> tempGroup = new HashMap<Integer, JSONObject>();// 修改的分组信息
    private Map<Integer, List<Integer>> tempVote = new HashMap<Integer, List<Integer>>();// 小组的投票信息
    private List<String> tempQuizIMEI = new ArrayList<String>();
    private Map<String, Quiz> tempQuiz = new HashMap<String, Quiz>();//随堂联系
    private List<Integer> tempGrouped = new ArrayList<Integer>();//已编辑完成的小组
    private List<Quiz> quizList = new ArrayList<Quiz>();//作业
    private FileLock lock;
    private List<Student> responderStudents = new ArrayList<Student>();//抢答的学生组
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
    	lock();
        clear();
    }
    
    private void lock(){
    	try {
            File locFile = new File(FileUtils.getProjectPath(), Constants.LOC_FILE);
            if (!locFile.exists()) {
                locFile.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(locFile, "rw");
            lock = raf.getChannel().tryLock();//得到锁
            if (lock == null) {
            	raf.close();
                JOptionPane.showMessageDialog(null, "程序已经在运行!");
                System.exit(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public void clear(){
    	imeiDevice = new HashMap<String, Device>();
        tableMap = new HashMap<Integer, Table>();
        tableNumberMap = new HashMap<Integer, Table>();
        deviceTable = new HashMap<Integer, Table>();
        tableDevice = new HashMap<Integer, List<Device>>();
        tableGroup = new HashMap<Integer, Group>();

        clientChannel = new HashMap<String, SocketChannel>();
        groupChannel = new HashMap<Integer, List<SocketChannel>>();
    }
    
	public FloatIcon getFloatIcon() {
		return floatIcon;
	}

	public void setFloatIcon(FloatIcon floatIcon) {
		this.floatIcon = floatIcon;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public boolean isGrouping() {
		return isGrouping;
	}

	public void setGrouping(boolean isGrouping) {
		this.isGrouping = isGrouping;
	}

	public List<Integer> getTempGrouped() {
		return tempGrouped;
	}

	public List<String> getTempQuizIMEI() {
		return tempQuizIMEI;
	}
	
	public void addQuizIMEI(String imei) {
		tempQuizIMEI.add(imei);
		String message = String.format(Constants.MESSAGE_QUIZ, 0, tempQuizIMEI.size());
		getFloatIcon().showQuizMessage(message);
	}

    public void addLoginStudent(String imei,Student student){
    	List<Student> studentList = imeiStudent.get(imei);
        if (studentList == null) {
        	studentList = new ArrayList<Student>();
        }
        boolean isLogin = false;
        for(Student aStudent : studentList){
			if (aStudent.getName().equals(student.getName())
					&& aStudent.getNumber().equals(student.getNumber())) {
        		isLogin = true;
        		break;
        	}
        }
		if (!isLogin) {
			studentList.add(student);
			imeiStudent.put(imei, studentList);
		}
    }
    //需要移除两个容器
    public void removeLoginStudent(String imei){
    	List<Student> studentList = imeiStudent.get(imei);
    	if (studentList == null) {
        	studentList = new ArrayList<Student>();
        }
    	
    	for(Student aStudent : studentList){
    		Iterator<Student> it = onlineStudent.iterator();
			while (it.hasNext()) {
				Student student = it.next();
				if (aStudent.getName().equals(student.getName())
						&& aStudent.getNumber().equals(student.getNumber())) {
					it.remove();
				}
			}
    	}
    	imeiStudent.remove(imei);
    }
    
    /**
     * TODO 界面有更改，现需要遍历所有的在线学生
     * @param imei
     * @param student
     */
    public void removeLoginStudent(String imei,Student student){
    	List<Student> studentList = imeiStudent.get(imei);
    	if (studentList == null) {
        	studentList = new ArrayList<Student>();
        }
    	//先在当前IMEI下找
    	boolean hasStudent = false;
    	Iterator<Student> it = studentList.iterator();
    	while (it.hasNext()) {
    		Student aStudent = it.next();
    		if (aStudent.getName().equals(student.getName())
					&& aStudent.getNumber().equals(student.getNumber())) {
				it.remove();
				hasStudent = true;
				break;
			}
    	}
		if (!hasStudent) {
			Set<Entry<String, List<Student>>> set = imeiStudent.entrySet();
			Iterator<Entry<String, List<Student>>> its = set.iterator();
			while (its.hasNext()) {
				Entry<String, List<Student>> entry = its.next();
				List<Student> students = entry.getValue();
				Iterator<Student> iterator = students.iterator();
				while (iterator.hasNext()) {
					Student s = iterator.next();
					if (s.getName().equals(student.getName())
							&& s.getNumber().equals(student.getNumber())) {
						iterator.remove();
						break;
					}
				}
			}
		}
    	it = onlineStudent.iterator();
    	while (it.hasNext()) {
			Student aStudent = it.next();
			if (aStudent.getName().equals(student.getName())
					&& aStudent.getNumber().equals(student.getNumber())) {
				it.remove();
				break;
			}
		}
    }
    
    /**
     * TODO 界面有更改，现需要遍历所有的在线学生
     * @param imei
     * @param student
     */
	public void removeLoginStudent(Student student) {
		Set<Entry<String, List<Student>>> set = imeiStudent.entrySet();
		Iterator<Entry<String, List<Student>>> its = set.iterator();
		while (its.hasNext()) {
			Entry<String, List<Student>> entry = its.next();
			List<Student> students = entry.getValue();
			Iterator<Student> iterator = students.iterator();
			while (iterator.hasNext()) {
				Student s = iterator.next();
				if (s.getName().equals(student.getName())
						&& s.getNumber().equals(student.getNumber())) {
					iterator.remove();
					break;
				}
			}
		}
		Iterator<Student> it = onlineStudent.iterator();
		while (it.hasNext()) {
			Student aStudent = it.next();
			if (aStudent.getName().equals(student.getName())
					&& aStudent.getNumber().equals(student.getNumber())) {
				it.remove();
				break;
			}
		}
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
            List<Device> deviceList = table.getDevices();
            if (deviceList == null) {
                deviceList = new ArrayList<Device>();
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
    	tableGroup.remove(group.getTableId());
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
    	List<SocketChannel> channels = groupChannel.get(groupId);
    	List<SocketChannel> retval = new ArrayList<SocketChannel>();
    	if(channels != null){
    		Iterator<SocketChannel> it = channels.iterator();
        	while(it.hasNext()){
        		SocketChannel channel = it.next();
    			if (channel.isOpen()) {
    				retval.add(channel);
    			}
        	}
        	groupChannel.put(groupId, retval);
    	}
        return retval;
    }

    public Map<Integer, List<SocketChannel>> getGroupChannel() {
		return groupChannel;
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

    public Map<String, Quiz> getTempQuiz() {
        return tempQuiz;
    }
    
	public List<Quiz> getQuizList() {
		return quizList;
	}

	public void refresh() {
        MainFrame.getInstance().refreshPrepare();
        MainFrame.getInstance().refreshQuiz();
    }

    public void refreshQuiz() {
        MainFrame.getInstance().refreshQuiz();
    }

    public String getQuizId() {
        return quizId;
    }

    public String getLessionid() {
		return lessionid;
	}


	public void setLessionid(String lessionid) {
		this.lessionid = lessionid;
	}


	public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

	public List<Student> getStudentByImei(String imei) {
		return imeiStudent.get(imei);
	}


	public List<Course> getCourses() {
		return courses;
	}


	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}


	public List<Room> getRooms() {
		return rooms;
	}


	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}


	public Font getDefinedFont() {
		return definedFont;
	}

	public void setDefinedFont(Font definedFont) {
		this.definedFont = definedFont;
	}


	public List<Student> getResponderStudents() {
		return responderStudents;
	}


	public void setResponderStudents(List<Student> responderStudents) {
		this.responderStudents = responderStudents;
	}

}
