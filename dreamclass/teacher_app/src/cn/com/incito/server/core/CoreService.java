package cn.com.incito.server.core;

import io.netty.channel.ChannelHandlerContext;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import cn.com.incito.interclass.po.Device;
import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Quiz;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.interclass.po.Table;
import cn.com.incito.interclass.ui.MainFrame;
import cn.com.incito.server.api.ApiClient;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.api.result.TeacherGroupResultData;
import cn.com.incito.server.config.Constants;
import cn.com.incito.server.exception.AppException;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;
import cn.com.incito.server.utils.ImageUtil;
import cn.com.incito.server.utils.JSONUtils;
import cn.com.incito.server.utils.QuizCollector;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class CoreService {
	private Application app = Application.getInstance();
	private Logger logger = Logger.getLogger(CoreService.class.getName());

	public void deviceLogin(String imei) {
		app.getOnlineDevice().add(imei);
		app.refresh();// 更新UI
	}

	public Group deviceLogout(String imei) {
		Device device = app.getImeiDevice().get(imei);
		if (device == null) {
			return null;
		}
		Table table = app.getDeviceTable().get(device.getId());
		if (table == null) {
			return null;
		}
		Group group = app.getTableGroup().get(table.getId());
		List<Student> students = app.getStudentByImei(imei);
		if (students != null) {
			for (Student student : students) {
				for (Student aStudent : group.getStudents()) {
					if (student.getName().equals(aStudent.getName())
							&& student.getNumber().equals(aStudent.getNumber())) {
						aStudent.setLogin(false);
					}
				}
			}
		}
		app.removeLoginStudent(imei);
		app.getOnlineDevice().remove(imei);
		Application.getInstance().getClientChannel().remove(imei);
		app.refresh();// 更新UI
		return group;
	}

	/**
	 * 判断设备是否已绑定
	 *
	 * @param imei
	 * @return
	 */
	public String isDeviceBind(String imei) {
		try {
			int roomId = app.getRoom().getId();
			final String result = ApiClient.isDeviceBind(imei, roomId);
			if (result != null && !result.equals("")) {
				return result;
			}
		} catch (Exception e) {
			if (e instanceof AppException) {
				return JSONUtils.renderJSONString(1);// 失败
			}
		}
		return JSONUtils.renderJSONString(2);// 失败
	}

	/**
	 * 绑定设备
	 *
	 * @param imei
	 * @param number
	 * @return
	 */
	public String deviceBind(String imei, int number) {
		try {
			final String result = ApiClient.deviceBind(imei, number);
			if (result != null && !result.equals("")) {
				return result;
			}
		} catch (Exception e) {
			if (e instanceof AppException) {
				return JSONUtils.renderJSONString(1);// 失败
			}
		}
		return JSONUtils.renderJSONString(2);// 失败
	}

	/**
	 * 刷新分组、课桌界面
	 */
	public void refreshGroupList() {
		int schoolId = app.getTeacher().getSchoolId();
		int roomId = app.getRoom().getId();
		int teacherId = app.getTeacher().getId();
		int courseId = app.getCourse().getId();
		int year = app.getClasses().getYear();// 学年
		int number = app.getClasses().getNumber();// 班级
		try {
			final String result = ApiClient.getGroupList(schoolId, roomId, teacherId, courseId, year, number);
			if (result != null && !result.equals("")) {
				JSONObject jsonObject = JSON.parseObject(result);
				if (jsonObject.getIntValue("code") != 0) {
					return;
				}
				String data = jsonObject.getString("data");
				TeacherGroupResultData resultData = JSON.parseObject(data, TeacherGroupResultData.class);

				// 第二步获得班级、课程、设备、课桌、分组数据
				Application.getInstance().initMapping(resultData.getDevices(), resultData.getTables(),
						resultData.getGroups());
				Application.getInstance().refresh();
			}
			logger.info(result);
		} catch (AppException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 学生登陆
	 *
	 * @param uname
	 * @param sex
	 * @param number
	 * @param imei
	 * @return
	 */
	public String login(String uname, int sex, String number, String imei) {
		Device device = app.getImeiDevice().get(imei);
		if (device == null) {
			// 系统中无此设备
			return JSONUtils.renderJSONString(1);// 失败
		}
		Table table = app.getDeviceTable().get(device.getId());
		if (table == null) {
			// 此设备未绑定课桌
			return JSONUtils.renderJSONString(2);// 失败
		}
		Group group = app.getTableGroup().get(table.getId());
		app.addGroup(group);
		for (Student student : group.getStudents()) {
			if (student.getUname().equals(uname) && student.getNumber().equals(number)) {
				removeReconnectStu(imei, uname, number);// 移除重连学生
				student.setLogin(true);
				app.getOnlineStudent().add(student);// 加入在线的学生
				app.addLoginStudent(imei, student);
				app.refresh();// 更新UI
				return JSONUtils.renderJSONString(0, group);
			}
		}
		return register(uname, sex, number, imei);// 学生未注册
	}

	/**
	 * 移除重连表中学生
	 * 
	 * @param imei
	 * @param uname
	 * @param number
	 */
	private void removeReconnectStu(String imei, String uname, String number) {

		List<Student> stus = app.getRecStudents().get(imei);
		if (stus != null)
			app.getRecStudents().remove(imei);// 允许为空或者size为零
		Device device = app.getImeiDevice().get(imei);
		if (device == null)
			return;
		Table table = app.getDeviceTable().get(device.getId());
		if (table == null)
			return;
		Group group = app.getTableGroup().get(table.getId());
		if (group == null)
			return;

		for (Device dvs : group.getDevices()) {
			String imeis = dvs.getImei();
			if (!imeis.equals(imei)) {
				List<Student> students = app.getRecStudents().get(imeis);
				if (students != null && students.size() != 0) {
					Iterator<Student> it = students.iterator();
					while (it.hasNext()) {
						Student student = it.next();
						if (student.getName().equals(uname) && 
								student.getNumber().equals(number)) {
							it.remove();
							return;
						}
					}
				}
			}
		}

	}

	/**
	 * 注销
	 *
	 * @param uname
	 * @param sex
	 * @param number
	 * @param imei
	 * @return
	 */
	public String logout(String uname, int sex, String number, String imei) {
		Device device = app.getImeiDevice().get(imei);
		if (device == null) {
			// 系统中无此设备
			return JSONUtils.renderJSONString(1);// 失败
		}
		Table table = app.getDeviceTable().get(device.getId());
		if (table == null) {
			// 此设备未绑定课桌
			return JSONUtils.renderJSONString(2);// 失败
		}
		Group group = app.getTableGroup().get(table.getId());
		for (Student student : group.getStudents()) {
			if (student.getUname().equals(uname) && student.getNumber().equals(number)) {
				student.setLogin(false);
				app.getOnlineStudent().remove(student);
				app.removeLoginStudent(imei, student);
				app.refresh();// 更新UI
				return JSONUtils.renderJSONString(0, group);
			}
		}
		return JSONUtils.renderJSONString(3);// 失败
	}

	/**
	 * 注册
	 *
	 * @param uname
	 * @param sex
	 * @param number
	 * @param imei
	 * @return
	 */
	public String register(String uname, int sex, String number, String imei) {
		try {
			final String result = ApiClient.loginForStudent(uname, sex, number, imei);
			if (result != null && !result.equals("")) {
				JSONObject jsonObject = JSON.parseObject(result);
				if (jsonObject.getIntValue("code") == 0) {
					String data = jsonObject.getString("data");
					Group group = JSON.parseObject(data, Group.class);
					for (Student student : group.getStudents()) {
						if ((student.getName() + student.getNumber()).equals(uname + number)) {
							student.setLogin(true);
							// 检查当前注册的学生是否之前在别的小组，存在则从之前的组中剔除
							sendOtherPadLogout(uname, number);
							// 注册学生必须在这里先移除，有可能是切换分组
							app.removeLoginStudent(student);
							app.getOnlineStudent().add(student);
							app.addLoginStudent(imei, student);
						}
						if (app.getOnlineStudent().contains(student)) {
							student.setLogin(true);
						}
					}
					app.addGroup(group);
					app.getTableGroup().put(group.getTableId(), group);
					app.refresh();// 更新UI
					return JSONUtils.renderJSONString(JSONUtils.SUCCESS, group);
				}
				return result;
			}
		} catch (Exception e) {
			if (e instanceof AppException) {
				return JSONUtils.renderJSONString(1);// 失败
			}
		}
		return JSONUtils.renderJSONString(2);// 失败
	}

	/**
	 * 将其他组的相同人员剔除
	 * 
	 * @param uname
	 * @param number
	 * @param groupId
	 *            新组id
	 */
	private void sendOtherPadLogout(String uname, String number) {
		Application app = Application.getInstance();
		List<Group> groupList = app.getGroupList();
		if (groupList == null || groupList.size() == 0) {
			return;
		}
		Group group = findGroup(groupList, uname, number);
		if (group != null) {
			Iterator<Student> it = group.getStudents().iterator();
			while (it.hasNext()) {
				Student temp = it.next();
				if ((temp.getName() + temp.getNumber()).equals(uname + number)) {
					it.remove();
					break;
				}
			}
			app.addGroup(group);
			app.getTableGroup().put(group.getTableId(), group);
			app.refresh();// 更新UI
			String result = JSONUtils.renderJSONString(0, group);// 更新消息发往pad端
			sendResponse(result, Application.getInstance().getClientChannelByGroup(group.getId()));
		}
	}

	private Group findGroup(List<Group> groupList, String uname, String number) {
		for (Group group : groupList) {
			List<Student> students = group.getStudents();
			if (students == null || students.size() == 0) {
				continue;
			}
			for (Student temp : students) {
				if ((temp.getName() + temp.getNumber()).equals(uname + number)) {
					return group;
				}
			}
		}
		return null;
	}

	public Student getStudentByNumber(String number,String imei) {
		Device device = app.getImeiDevice().get(imei);
		if(device == null)
			return null;
		Table table = app.getDeviceTable().get(device.getId());
		if(table == null)
			return null;
		Group group = app.getTableGroup().get(table.getId());
		if(group == null)
			return null;
			List<Student> students = group.getStudents();
			for (Student student : students) {
				if (student.getNumber().equals(number)) {
					return student;
				}
		}
		return null;
	}

	private void sendResponse(String json, List<ChannelHandlerContext> channels) {
		for (ChannelHandlerContext channel : channels) {
			MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_STUDENT_LOGIN);
			messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(json));
			byte[] messageData = messagePacking.pack().array();
			ByteBuffer buffer = ByteBuffer.allocate(messageData.length);
			buffer.put(messageData);
			buffer.flip();
			if (channel.channel().isActive()) {
				SocketServiceCore.getInstance().sendMsg(messagePacking, channel);
			}
		}
	}

	/**
	 * 根据IMEI获取所在组
	 *
	 * @param imei
	 * @return
	 */
	public String getGroupByIMEI(String imei) {
		Group testGroup = app.getGroupOnline(imei);
		if(testGroup==null){
			return JSONUtils.renderJSONString(1);
		}
		return JSONUtils.renderJSONString(0, testGroup);
	}

	public Group getGroupObjectByIMEI(String imei) {
		Device device = app.getImeiDevice().get(imei);
		if (device == null) {
			// 系统中无此设备
			return null;
		}
		Table table = app.getDeviceTable().get(device.getId());
		if (table == null) {
			// 此设备未绑定课桌
			return null;
		}
		return app.getTableGroup().get(table.getId());
	}

	public List<ChannelHandlerContext> getGroupSocketChannelByGroupId(int groupId) {
		return app.getClientChannelByGroup(groupId);
	}

	/**
	 * 保存学生作业
	 *
	 * @param imei
	 * @return
	 */
	// public void SavePaper(final String imei, final String quizid,
	// final String lessionid, final byte[] imageByte,
	// final SocketChannel channel) {
	// new Thread(){
	// public void run() {
	// File path = new File(Constants.PAPER_PATH + File.separator + lessionid
	// + File.separator + imei.replace(":", "-"));
	// path.mkdirs();
	//
	// File file = new File(path, quizid + ".jpg");
	// File thumbnail = new File(path, quizid + "_thumbnail.jpg");
	// try {
	// FileImageOutputStream imageOutput = new FileImageOutputStream(file);
	// imageOutput.write(imageByte, 0, imageByte.length);
	// imageOutput.close();
	// ImageUtil.resize(file, file, 865, 1f);
	// logger.error("大图生成：" + file.getAbsoluteFile());
	// ImageUtil.resize(file, thumbnail, 186, 1f);
	// logger.error("缩略图生成：" + thumbnail.getAbsoluteFile());
	// } catch (IOException e) {
	// logger.error("保存作业图片出现错误:", e);
	// }
	//
	// Quiz quiz = new Quiz();
	// quiz.setId(quizid);
	// quiz.setImei(imei);
	// quiz.setLessionid(lessionid);
	// StringBuffer name = new StringBuffer();
	// List<Student> students = app.getStudentByImei(imei);
	// if (students != null) {
	// for (Student student : students) {
	// name.append(student.getName());
	// name.append(",");
	// }
	// }
	// if (name.length() != 0) {
	// quiz.setName(name.deleteCharAt(name.length() - 1).toString());
	// }
	// quiz.setTime(System.currentTimeMillis());
	// Group group = getGroupObjectByIMEI(imei);
	// quiz.setGroupId(group.getId());
	// quiz.setGroup(group);
	// quiz.setQuizUrl(file.getAbsolutePath());
	// quiz.setThumbnail(thumbnail.getAbsolutePath());
	// app.getTempQuiz().put(imei, quiz);
	// app.getQuizMap().put(quizid, quiz);
	// app.getQuizList().add(quiz);
	// app.refresh();
	// if (app.getQuizList().size() == app.getClientChannel().size()) {
	// Application.getInstance().getFloatIcon().showNoQuiz();
	// MainFrame.getInstance().showNoQuiz();
	// Application.getInstance().setLockScreen(true);
	// } else {
	// String message = String.format(Constants.MESSAGE_QUIZ, app
	// .getQuizList().size(), app.getTempQuizIMEI().size());
	// Application.getInstance().getFloatIcon().showQuizMessage(message);
	// }
	// //当前作业处理完毕，处理下一作业
	// QuizCollector.getInstance().quizComplete(channel);
	// QuizCollector.getInstance().nextQuiz();
	// }
	// }.start();
	// }
	/**
	 * 保存学生作业
	 *
	 * @param imei
	 * @return
	 */
	public void SavePaper(String imei, String quizid, String path, ChannelHandlerContext channel) {
		File file = new File(path);
		logger.info("图片是否存在:" + file.exists());
		if (!file.exists()) {
			return;
		}
		File thumbnail = new File(file.getParent(), UUID.randomUUID() + ".jpg");
		try {
			thumbnail.delete();
			ImageUtil.resize(file, thumbnail, 186, 1f);
			logger.info("缩略图生成：" + thumbnail.getAbsoluteFile());
		} catch (IOException e) {
			logger.error("保存作业图片出现错误:", e);
			return;
		}

		Quiz quiz = new Quiz();
		quiz.setId(quizid);
		quiz.setImei(imei);
		// Student students = app.getStudentByImei(imei);
		// if (students.getName().length() != 0) {
		// quiz.setName(students.getName());
		// }

		List<Student> studentsList = app.getStudentByImei(imei);
		StringBuffer str = new StringBuffer();
		if (studentsList.size() > 0) {
			for (int i = 0; i < studentsList.size(); i++) {
				str.append(studentsList.get(i).getName() + ";");
			}
			quiz.setName(str.toString());
		}

		quiz.setTime(System.currentTimeMillis());
		quiz.setQuizUrl(file.getAbsolutePath());
		quiz.setThumbnail(thumbnail.getAbsolutePath());
		app.getTempQuiz().put(imei, quiz);
		app.getQuizList().add(quiz);
		app.refresh();
		if (app.getQuizList().size() == app.getClientChannel().size()) {
			Application.getInstance().getFloatIcon().showNoQuiz();
			MainFrame.getInstance().showNoQuiz();
			Application.getInstance().setLockScreen(true);
		} else {
			String message = String.format(Constants.MESSAGE_QUIZ, app.getQuizList().size(), app.getClientChannel()
					.size());
			Application.getInstance().getFloatIcon().showQuizMessage(message);
		}
		// 当前作业处理完毕，处理下一作业
		QuizCollector.getInstance().quizComplete(channel);
		QuizCollector.getInstance().nextQuiz();
	}

}
