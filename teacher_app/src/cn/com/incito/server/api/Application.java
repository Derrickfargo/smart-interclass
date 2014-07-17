package cn.com.incito.server.api;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.incito.interclass.po.Student;
import cn.com.incito.interclass.ui.Login;
import cn.com.incito.server.core.CoreSocket;

public class Application {

	private static Application instance;
	private List<Student> studentList;
	private List<Student> onlineList;
	private Map<Student, SocketChannel> clientChannel;
	private CoreSocket coreSocket;

	private Application() {
		clientChannel = new HashMap<Student, SocketChannel>();
		new Login();
	}

	public static Application getInstance() {
		if (instance == null) {
			instance = new Application();
		}
		return instance;
	}

	public List<Student> getStudentList() {
		return studentList;
	}

	public void setStudentList(List<Student> studentList) {
		this.studentList = studentList;
	}

	public List<Student> getOnlineList() {
		return onlineList;
	}

	public void setOnlineList(List<Student> onlineList) {
		this.onlineList = onlineList;
	}

	public Map<Student, SocketChannel> getClientChannel() {
		return clientChannel;
	}

	public void putClientChannel(Student student, SocketChannel channel) {
		clientChannel.put(student, channel);
	}

	public CoreSocket getCoreSocket() {
		return coreSocket;
	}

	public void setCoreSocket(CoreSocket coreSocket) {
		this.coreSocket = coreSocket;
	}
}
