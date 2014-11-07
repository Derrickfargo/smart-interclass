package cn.com.incito.server.api;

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
import java.util.Set;

import javax.swing.JOptionPane;

import cn.com.incito.interclass.po.Classes;
import cn.com.incito.interclass.po.Course;
import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Quiz;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.interclass.po.Teacher;
import cn.com.incito.interclass.ui.FloatIcon;
import cn.com.incito.interclass.ui.Login;
import cn.com.incito.interclass.ui.MainFrame;
import cn.com.incito.server.config.Constants;
import cn.com.incito.server.utils.FileUtils;

public class Application {
	public boolean isLockScreen;

	public boolean isLockScreen() {
		return isLockScreen;
	}

	public void setLockScreen(boolean isLockScreen) {
		this.isLockScreen = isLockScreen;
	}

	private static Application instance;
	private boolean isGrouping = false;
	private FloatIcon floatIcon;
	private String quizId; // 考试流水号
	/**
	 * 课堂id
	 */
	private String lessionid;
	private String mac;// 当前登录的mac地址
	private Teacher teacher;// 当前登录的老师，教师登陆完后初始化
	private Course course;// 当前上课的课程，教师登陆完后初始化
	private Classes classes;// 当前上课的班级，教师登陆完后初始化

	public static boolean isOnClass;// 正在上课
	public static boolean hasQuiz;// 是否在作业
	private List<Student> studentList = new ArrayList<Student>();// 本班级的所有学生
	private Set<Student> onlineStudent = new HashSet<Student>();
	private Set<Student> offlineStudent = new HashSet<Student>();

	private Map<Integer, List<SocketChannel>> groupChannel;// 保存每组和已登录的socket
	private Map<String, Student> imeiStudent = new HashMap<String, Student>();//key是imei Student是
	private Map<String, SocketChannel> clientChannel;// 保存所有设备登陆的socket，imei和socket
	
	private Set<Group> groupList = new HashSet<Group>();// 本堂课的所有分组
	private Map<Integer, Group> groupMap = new HashMap<Integer, Group>(); //以保存的分组信息
	private Map<Integer, Group> tempGroup = new HashMap<Integer, Group>();// 未保存的分组信息,key是创建学生id
	
	private List<String> tempQuizIMEI = new ArrayList<String>();
	private Map<String, Quiz> tempQuiz = new HashMap<String, Quiz>();// 随堂联系
	private List<Quiz> quizList = new ArrayList<Quiz>();// 作业
	private FileLock lock;

	private Application() {
		lock();
		clear();
		new Login();
	}

	private void lock() {
		try {
			File locFile = new File(FileUtils.getProjectPath(),
					Constants.LOC_FILE);
			if (!locFile.exists()) {
				locFile.createNewFile();
			}
			RandomAccessFile raf = new RandomAccessFile(locFile, "rw");
			lock = raf.getChannel().tryLock();// 得到锁
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

	public void clear() {
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

	public List<String> getTempQuizIMEI() {
		return tempQuizIMEI;
	}

	public void addQuizIMEI(String imei) {
		tempQuizIMEI.add(imei);
		String message = String.format(Constants.MESSAGE_QUIZ, 0,
				tempQuizIMEI.size());
		getFloatIcon().showQuizMessage(message);
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

	public void addGroup(Group group) {
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

	public Set<Group> getGroupList() {
		return groupList;
	}

	public void setGroupList(Set<Group> groupList) {
		this.groupList = groupList;
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
		if (channels != null) {
			Iterator<SocketChannel> it = channels.iterator();
			while (it.hasNext()) {
				SocketChannel channel = it.next();
				if (channel.isOpen()) {
					retval.add(channel);
				}
			}
			groupChannel.put(groupId, retval);
		}
		return retval;
	}

	public List<Student> getStudentList() {
		return studentList;
	}

	public void setStudentList(List<Student> studentList) {
		this.studentList = studentList;
		if (studentList == null || studentList.size() == 0) {
			return;
		}
		for (Student student : studentList) {
			offlineStudent.add(student);
			imeiStudent.put(student.getImei(), student);
		}
	}

	public Map<Integer, List<SocketChannel>> getGroupChannel() {
		return groupChannel;
	}

	public Set<Student> getOnlineStudent() {
		return onlineStudent;
	}

	public void setOnlineStudent(Set<Student> onlineStudent) {
		this.onlineStudent = onlineStudent;
	}

	public Set<Student> getOfflineStudent() {
		return offlineStudent;
	}

	public void setOfflineStudent(Set<Student> offlineStudent) {
		this.offlineStudent = offlineStudent;
	}

	public Map<Integer, Group> getTempGroup() {
		return tempGroup;
	}

	public Map<String, Quiz> getTempQuiz() {
		return tempQuiz;
	}

	public List<Quiz> getQuizList() {
		return quizList;
	}

	public void refresh() {
		MainFrame.getInstance().refresh();
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

	public Student getStudentByImei(String imei) {
		return imeiStudent.get(imei);
	}

}
