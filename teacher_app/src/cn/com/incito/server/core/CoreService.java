package cn.com.incito.server.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.List;

import javax.imageio.stream.FileImageOutputStream;

import cn.com.incito.interclass.po.Device;
import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.interclass.po.Table;
import cn.com.incito.server.api.ApiClient;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.api.result.TeacherGroupResultData;
import cn.com.incito.server.exception.AppException;
import cn.com.incito.server.utils.JSONUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class CoreService {
	private Application app = Application.getInstance();

	public void deviceLogin(String imei) {
		app.getOnlineDevice().add(imei);
		app.refreshMainFrame();// 更新UI
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
			int roomId = app.getRoom().getId();
			final String result = ApiClient.deviceBind(imei, number, roomId);
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
						resultData.getTables(), resultData.getGroups());
				Application.getInstance().refreshMainFrame();
			}
			System.out.println(result);
		} catch (AppException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 登陆
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
				app.refreshMainFrame();// 更新UI
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
				app.refreshMainFrame();// 更新UI
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
						}
						if (app.getOnlineStudent().contains(student)) {
							student.setLogin(true);
						}
					}
					app.addGroup(group);
					app.getTableGroup().put(group.getTableId(), group);
					app.refreshMainFrame();// 更新UI
					JSONUtils.renderJSONString(JSONUtils.SUCCESS, group);
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

	public List<SocketChannel> getGroupSocketChannelByGroupId(int groupId) {
		return app.getClientChannelByGroup(groupId);
	}

	/**
	 * 保存学生作业
	 * 
	 * @param imei
	 * @return
	 */
	public String SavePaper(String imei,String id, byte[] imageByte) {
		File path = new File(getProjectPath()+"/"+id);
		path.mkdirs();
		File file = new File(path, imei + ".jpg");
		try {
			 FileImageOutputStream imageOutput = new FileImageOutputStream(file);
			  imageOutput.write(imageByte, 0, imageByte.length);
			  imageOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return JSONUtils.renderJSONString(1);
	}
	 /**
	 * @return 获取程序路径
	 */
	public static String getProjectPath() {
	       java.net.URL url = CoreService.class.getProtectionDomain().getCodeSource().getLocation();
	       String filePath = null ;
	       try {
	           filePath = java.net.URLDecoder.decode (url.getPath(), "utf-8");
	       } catch (Exception e) {
	           e.printStackTrace();
	       }
	    if (filePath.endsWith(".jar"))
	       filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
	       java.io.File file = new java.io.File(filePath);
	       filePath = file.getAbsolutePath();
	    return filePath;

	}
	
	
}
