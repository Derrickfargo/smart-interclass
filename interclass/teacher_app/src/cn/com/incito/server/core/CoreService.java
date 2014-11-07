package cn.com.incito.server.core;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.stream.FileImageOutputStream;

import org.apache.log4j.Logger;

import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Quiz;
import cn.com.incito.interclass.po.Student;
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
import cn.com.incito.server.utils.URLs;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class CoreService {
	private static Application app = Application.getInstance();
	private Logger logger = Logger.getLogger(CoreService.class.getName());

	public Group deviceLogout(String imei) {
//		Device device = app.getImeiDevice().get(imei);
//		if (device == null) {
//			return null;
//		}
//		Group group = app.getTableGroup().get(device.getId());
//		List<Student> students = app.getStudentByImei(imei);
//		if (students != null) {
//			for (Student student : students) {
//				for (Student aStudent : group.getStudents()) {
//					if (student.getName().equals(aStudent.getName())
//							&& student.getNumber().equals(aStudent.getNumber())) {
//						aStudent.setLogin(false);
//					}
//				}
//			}
//		}
//		app.removeLoginStudent(imei);
//		app.getOnlineDevice().remove(imei);
//		Application.getInstance().getClientChannel().remove(imei);
//		app.refresh();// 更新UI
		return new Group();
	}


	/**
	 * 刷新分组、课桌界面
	 */
	public void refreshGroupList() {
		int schoolId = app.getTeacher().getSchoolId();
		int teacherId = app.getTeacher().getId();
		int classId = app.getClasses().getId();
		try {
			final String result = ApiClient.getGroupList(schoolId, 
					teacherId, classId,"" );
			if (result != null && !result.equals("")) {
				JSONObject jsonObject = JSON.parseObject(result);
				if (jsonObject.getIntValue("code") != 0) {
					return;
				}
				String data = jsonObject.getString("data");
				TeacherGroupResultData resultData = JSON.parseObject(data,
						TeacherGroupResultData.class);

				// 第二步获得班级、课程、设备、课桌、分组数据
//				Application.getInstance().initMapping(resultData.getDevices(), app.getGroupList());
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
	public Student login(String imei) {
		List<Student> students = app.getStudentList();
		if (students == null || students.size() == 0) {
			return null;// 失败
		}
		for (Student student : students) {
			if (student.getImei().equals(imei)) {
				student.setLogin(true);
				app.getOnlineStudent().add(student);
				app.getOfflineStudent().remove(student);
				app.refresh();// 更新UI
				return student;
			}
		}
		return null;
	}



	public Student getStudentByNumber(String number){
		for (Group group : app.getGroupList()) {
			List<Student> students = group.getStudents();
			for (Student student : students) {
				if(student.getNumber().equals(number)){
					return student;
				}
			}
		}
		return null;
	}
	
	private void sendResponse(String json,List<SocketChannel> channels) {
		for (SocketChannel channel : channels) {
			MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_STUDENT_LOGIN);
	        messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(json));
	        byte[] messageData = messagePacking.pack().array();
	        ByteBuffer buffer = ByteBuffer.allocate(messageData.length);
	        buffer.put(messageData);
	        buffer.flip();
			try {
				if (channel.isConnected()) { 
					channel.write(buffer);
				}
			} catch (IOException e) {
				e.printStackTrace();
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
//		Device device = app.getImeiDevice().get(imei);
//		if (device == null) {
//			// 系统中无此设备
//			return JSONUtils.renderJSONString(1);// 失败
//		}
		Set<Group> groupList = app.getGroupList();
		return JSONUtils.renderJSONString(0, groupList);
	}

	public Group getGroupObjectByIMEI(String imei) {
		return new Group();
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
			logger.error("大图生成：" + file.getAbsoluteFile());
			ImageUtil.resize(file, thumbnail, 186, 1f);
			logger.error("缩略图生成：" + thumbnail.getAbsoluteFile());
		} catch (IOException e) {
			logger.error("保存作业图片出现错误:", e);
		}

		Quiz quiz = new Quiz();
		quiz.setId(quizid);
		quiz.setImei(imei);
		quiz.setLessionid(lessionid);
		StringBuffer name = new StringBuffer();
		
		Student students = app.getStudentByImei(imei);
		if (students.getName().length() != 0) {
			quiz.setName(students.getName());
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
		if (app.getQuizList().size() == app.getClientChannel().size()) {
			Application.getInstance().getFloatIcon().showNoQuiz();
			MainFrame.getInstance().showNoQuiz();
			Application.getInstance().setLockScreen(true);
		} else {
			String message = String.format(Constants.MESSAGE_QUIZ, app
					.getQuizList().size(), app.getClientChannel().size());
			Application.getInstance().getFloatIcon().showQuizMessage(message);
		}
		return JSONUtils.renderJSONString(0);
	}

	/**
	 * 保存小组信息
	 * 
	 * @param groupId
	 * @param studentId
	 * @return
	 * @throws AppException 
	 */
	public static String  saveGroup(Group group) throws AppException{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("group", group);
		try {
			return ApiClient._post(URLs.URL_GROUP_SAVE, params, null);
		} catch (Exception e) {
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}

}
