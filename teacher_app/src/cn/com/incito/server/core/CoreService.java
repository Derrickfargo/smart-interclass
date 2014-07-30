package cn.com.incito.server.core;

import cn.com.incito.interclass.po.Device;
import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.interclass.po.Table;
import cn.com.incito.server.api.ApiClient;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.utils.JSONUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class CoreService {
	private Application app = Application.getInstance();

	public void deviceLogin(String imei){
		app.getOnlineDevice().add(imei);
		app.refreshMainFrame();// 更新UI
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
		for (Student student : group.getStudents()) {
			if (student.getUname().equals(uname)
					&& student.getNumber().equals(number)) {
				student.setLogin(true);
				app.getOnlineStudent().add(student);//加入在线的学生
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
						if((student.getName()+student.getName()).equals(uname + number)){
							student.setLogin(true);
							app.getOnlineStudent().add(student);
						}
					}
					app.addGroup(group);
					app.getTableGroup().put(group.getTableId(), group);
					app.refreshMainFrame();// 更新UI
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
		return JSONUtils.renderJSONString(0,  group);
	}
}
