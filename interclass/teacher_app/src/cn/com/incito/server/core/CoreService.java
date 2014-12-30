package cn.com.incito.server.core;

import java.io.File;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.stream.FileImageOutputStream;

import org.apache.log4j.Logger;

import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Quiz;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.interclass.po.TempStudent;
import cn.com.incito.interclass.ui.MainFrame;
import cn.com.incito.server.api.ApiClient;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.api.result.TeacherGroupResultData;
import cn.com.incito.server.config.Constants;
import cn.com.incito.server.exception.AppException;
import cn.com.incito.server.utils.ImageUtil;
import cn.com.incito.server.utils.JSONUtils;
import cn.com.incito.server.utils.QuizCollector;
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
				Set<Group> groupSet = app.getGroupList();
				Iterator<Group> it = groupSet.iterator();
				while(it.hasNext()){
					Group group = it.next();
					for (Student aStudent : group.getStudents()) {
						if (student.getId() == aStudent.getId()) {
							aStudent.setLogin(true);
							break;
						}
					}
				}
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
	
	public Group getGroupObjectByIMEI(String imei) {
		Set<Group> groups = app.getGroupList();
		Iterator<Group> it = groups.iterator();
		while (it.hasNext()) {
			Group group = it.next();
			
		}
		return new Group();
	}

	public List<SocketChannel> getGroupSocketChannelByGroupId(int groupId) {
		return app.getClientChannelByGroup(groupId);
	}

//	/**
//	 * 保存学生作业
//	 *
//	 * @param imei
//	 * @return
//	 */
//	public void SavePaper(final String imei, final String quizid, final String lessionid,
//			final byte[] imageByte, final SocketChannel channel) {
//		new Thread(){
//			public void run() {
//				File path = new File(Constants.PAPER_PATH + File.separator + lessionid
//						+ File.separator + imei.replace(":", "-"));
//				path.mkdirs();
//
//				File file = new File(path, quizid + ".jpg");
//				File thumbnail = new File(path, quizid + "_thumbnail.jpg");
//				try {
//					FileImageOutputStream imageOutput = new FileImageOutputStream(file);
//					imageOutput.write(imageByte, 0, imageByte.length);
//					imageOutput.close();
//					ImageUtil.resize(file, file, 865, 1f);
//					logger.info("大图生成：" + file.getAbsoluteFile());
//					ImageUtil.resize(file, thumbnail, 186, 1f);
//					logger.info("缩略图生成：" + thumbnail.getAbsoluteFile());
//				} catch (IOException e) {
//					logger.error("保存作业图片出现错误:", e);
//				}
//
//				Quiz quiz = new Quiz();
//				quiz.setId(quizid);
//				quiz.setImei(imei);
//				quiz.setLessionid(lessionid);
//				Student students = app.getStudentByImei(imei);
//				if (students.getName().length() != 0) {
//					quiz.setName(students.getName());
//				}
//				quiz.setTime(System.currentTimeMillis());
//				quiz.setQuizUrl(file.getAbsolutePath());
//				quiz.setThumbnail(thumbnail.getAbsolutePath());
//				app.getTempQuiz().put(imei, quiz);
//				app.getQuizList().add(quiz);
//				app.refresh();
//				if (app.getQuizList().size() == app.getClientChannel().size()) {
//					Application.getInstance().getFloatIcon().showNoQuiz();
//					MainFrame.getInstance().showNoQuiz();
//					Application.getInstance().setLockScreen(true);
//				} else {
//					String message = String.format(Constants.MESSAGE_QUIZ, app
//							.getQuizList().size(), app.getClientChannel().size());
//					Application.getInstance().getFloatIcon().showQuizMessage(message);
//				}
//				//当前作业处理完毕，处理下一作业
//				QuizCollector.getInstance().quizComplete(channel);
//				QuizCollector.getInstance().nextQuiz();
//			}
//		}.start();
//	}
	/**
	 * 保存学生作业
	 *
	 * @param imei
	 * @return
	 */
	public void SavePaper(String imei, String quizid, String path, SocketChannel channel) {
		File file = new File(path);
		logger.info("图片是否存在:" + file.exists());
		if(!file.exists()){
			return;
		}
		File thumbnail = new File(file.getParent(), "thumbnail.jpg");
		try {
			ImageUtil.resize(file, thumbnail, 186, 1f);
			logger.info("缩略图生成：" + thumbnail.getAbsoluteFile());
		} catch (IOException e) {
			logger.error("保存作业图片出现错误:", e);
			return;
		}

		Quiz quiz = new Quiz();
		quiz.setId(quizid);
		quiz.setImei(imei);
		Student students = app.getStudentByImei(imei);
		if (students.getName().length() != 0) {
			quiz.setName(students.getName());
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
			String message = String.format(Constants.MESSAGE_QUIZ, app
					.getQuizList().size(), app.getClientChannel().size());
			Application.getInstance().getFloatIcon().showQuizMessage(message);
		}
		//当前作业处理完毕，处理下一作业
		QuizCollector.getInstance().quizComplete(channel);
		QuizCollector.getInstance().nextQuiz();
	}

	/**
	 * 保存小组信息
	 * 
	 * @param groupId
	 * @param studentId
	 * @return
	 * @throws AppException 
	 */
	public String  saveGroup(Group group) throws AppException{
		Map<String, Object> params = new HashMap<String, Object>();
		JSONObject result = new JSONObject();
		result.put("group", group);
		group.setTeacherId(app.getTeacher().getId());
		group.setClassId(app.getClasses().getId());
		params.put("group", result.toJSONString());
		try {
			return ApiClient._post(URLs.URL_GROUP_SAVE, params, null);
		} catch (Exception e) {
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}
	public TempStudent getStudentIdByImei(String imei){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("imei", imei);
		try {
			String content= ApiClient._post(URLs.URL_GETSTUDENT_BY_IMEI, params, null);
			JSONObject jsonObject = JSON.parseObject(content);
			TempStudent student=(TempStudent) JSON.parseObject(jsonObject.get("data").toString(), TempStudent.class);
			return student;
		} catch (AppException e) {
			e.printStackTrace();
		}
		return null;
	}
}
