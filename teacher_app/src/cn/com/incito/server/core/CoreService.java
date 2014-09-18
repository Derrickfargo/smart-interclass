package cn.com.incito.server.core;

import java.io.File;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.List;

import javax.imageio.stream.FileImageOutputStream;

import org.apache.log4j.Logger;

import cn.com.incito.interclass.constant.Constants;
import cn.com.incito.interclass.po.Device;
import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Quiz;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.interclass.po.Table;
import cn.com.incito.interclass.ui.MainFrame;
import cn.com.incito.server.api.ApiClient;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.api.result.TeacherGroupResultData;
import cn.com.incito.server.exception.AppException;
import cn.com.incito.server.utils.ImageUtil;
import cn.com.incito.server.utils.JSONUtils;

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
		int classId = app.getClasses().getId();
		try {
			final String result = ApiClient.getGroupList(schoolId, roomId,
					teacherId, courseId, classId, "");
			if (result != null && !result.equals("")) {
				JSONObject jsonObject = JSON.parseObject(result);
				if (jsonObject.getIntValue("code") != 0) {
					return;
				}
				String data = jsonObject.getString("data");
				TeacherGroupResultData resultData = JSON.parseObject(data,
						TeacherGroupResultData.class);

				// 第二步获得班级、课程、设备、课桌、分组数据
				Application.getInstance().initMapping(resultData.getDevices(),
						resultData.getTables(), app.getGroupList());
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
			if (student.getUname().equals(uname)
					&& student.getNumber().equals(number)) {
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
			if (student.getUname().equals(uname)
					&& student.getNumber().equals(number)) {
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
			final String result = ApiClient.loginForStudent(uname, sex, number,
					imei);
			if (result != null && !result.equals("")) {
				JSONObject jsonObject = JSON.parseObject(result);
				if (jsonObject.getIntValue("code") == 0) {
					String data = jsonObject.getString("data");
					Group group = JSON.parseObject(data, Group.class);
					for (Student student : group.getStudents()) {
						if ((student.getName() + student.getNumber())
								.equals(uname + number)) {
							student.setLogin(true);
							app.getOnlineStudent().add(student);
							app.addLoginStudent(imei, student);
						}
						if (app.getOnlineStudent().contains(student)) {
							student.setLogin(true);
						}
					}
					/***
					 * 当学生出现跨分组注册时，同步分组状态 2014.9-10 popoy
					 */
					if (Application.getInstance().getGroupList() != null
							&& Application.getInstance().getGroupList().size() > 1) {
						for (Group gp : Application.getInstance()
								.getGroupList()) {
							if (gp.getStudents() == null
									|| gp.getStudents().size() < 1)
								continue;
							for (int j = 0; j < gp.getStudents().size(); j++) {
								if ((gp.getStudents().get(j).getName() + gp
										.getStudents().get(j).getNumber())
										.equals(uname + number)) {
									gp.getStudents().remove(j);
								}
							}
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
	 * 根据IMEI获取所在组
	 *
	 * @param imei
	 * @return
	 */
	public String getGroupByIMEI(String imei) {
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
		return JSONUtils.renderJSONString(0, group);
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

	public List<SocketChannel> getGroupSocketChannelByGroupId(int groupId) {
		return app.getClientChannelByGroup(groupId);
	}

	/**
	 * 保存学生作业
	 *
	 * @param imei
	 * @return
	 */
	public String SavePaper(String imei, String quizid, String lessionid,
			byte[] imageByte) {
		
		File path = new File(Constants.PAPER_PATH + File.separator + lessionid
				+ File.separator + imei.replace(":", "-"));
		path.mkdirs();

		File file = new File(path, quizid + ".jpg");
		File thumbnail = new File(path, quizid + "_thumbnail.jpg");
		try {
			FileImageOutputStream imageOutput = new FileImageOutputStream(file);
			imageOutput.write(imageByte, 0, imageByte.length);
			imageOutput.close();
			ImageUtil.resize(file, file, 865, 1f);
			ImageUtil.resize(file, thumbnail, 186, 1f);
		} catch (IOException e) {
			logger.error("保存作业图片出现错误:", e);
		}

		Quiz quiz = new Quiz();
		quiz.setId(quizid);
		quiz.setImei(imei);
		quiz.setLessionid(lessionid);
		StringBuffer name = new StringBuffer();
		List<Student> students = app.getStudentByImei(imei);
		if (students != null) {
			for (Student student : students) {
				name.append(student.getName());
				name.append(",");
			}
		}
		if (name.length() != 0) {
			quiz.setName(name.deleteCharAt(name.length() - 1).toString());
		}
		quiz.setTime(System.currentTimeMillis());
		Group group = getGroupObjectByIMEI(imei);
		quiz.setGroupId(group.getId());
		quiz.setGroup(group);
		quiz.setQuizUrl(file.getAbsolutePath());
		quiz.setThumbnail(thumbnail.getAbsolutePath());
		app.getTempQuiz().put(imei, quiz);
		app.getQuizList().add(quiz);
		app.refresh();
		if (app.getQuizList().size() == app.getTempQuizIMEI().size()) {
			Application.getInstance().getFloatIcon().showNoQuiz();
			MainFrame.getInstance().showNoQuiz();
		} else {
			String message = String.format(Constants.MESSAGE_QUIZ, app
					.getQuizList().size(), app.getTempQuizIMEI().size());
			Application.getInstance().getFloatIcon().showQuizMessage(message);
		}
		return JSONUtils.renderJSONString(0);
	}

}
