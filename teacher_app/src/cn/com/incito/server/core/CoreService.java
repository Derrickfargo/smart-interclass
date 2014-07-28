package cn.com.incito.server.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import cn.com.incito.interclass.po.Device;
import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.interclass.po.Table;
import cn.com.incito.server.api.ApiClient;
import cn.com.incito.server.api.Application;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class CoreService {
	private Application app = Application.getInstance();

	/**
	 * 输出为JSON字符串
	 * @param result 是否成功，0：成功，非零：失败（不同数值不同原因）
	 * @return JSON字符串
	 */
	protected String renderJSONString(int result) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", result);
		return JSON.toJSONString(map);
	}
	
	/**
	 * 输出为JSON字符串
	 * @param result 是否成功，0：成功，非零：失败（不同数值不同原因）
	 * @param data 要转换为JSON字符串的对象
	 * @return JSON字符串
	 */
	protected String renderJSONString(int result, Object data){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", result);
		map.put("data", data);
		return JSON.toJSONString(map);
	}
	
	/**
	 * 输出为JSON字符串
	 * @param result 是否成功，0：成功，非零：失败（不同数值不同原因）
	 * @param data 要转换为JSON字符串的List对象
	 * @return JSON字符串
	 */
	protected <T> String renderJSONString(int result, List<T> data){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", result);
		map.put("data", data);
		return JSON.toJSONString(map);
	}
	
	protected <T> String renderJSONString(int result, Map<String,T> data) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", result);
		map.put("data", data);
		return JSON.toJSONString(map);
	}
	
	/**
	 * 登陆
	 * @param uname
	 * @param sex
	 * @param number
	 * @param imei
	 * @return
	 */
	public String login(String uname, int sex, String number, String imei) {
		Device device = app.getImeiDevice().get(imei);
		if(device == null){
			//系统中无此设备
			renderJSONString(1);//失败
		}
		Table table = app.getDeviceTable().get(device.getId());
		if(table == null){
			//此设备未绑定课桌
			renderJSONString(2);//失败
		}
		Group group = app.getTableGroup().get(table.getId());
		for(Student student : group.getStudents()){
			if (student.getUname().equals(uname)
					&& student.getNumber().equals(number)) {
				student.setLogin(true);
				app.refreshMainFrame();//更新UI
				return renderJSONString(0, group);
			}
		}
		return register(uname, sex, number, imei);//学生未注册
	}
	
	/**
	 * 注销
	 * @param uname
	 * @param sex
	 * @param number
	 * @param imei
	 * @return
	 */
	public String logout(String uname, int sex, String number, String imei){
		Device device = app.getImeiDevice().get(imei);
		if(device == null){
			//系统中无此设备
			renderJSONString(1);//失败
		}
		Table table = app.getDeviceTable().get(device.getId());
		if(table == null){
			//此设备未绑定课桌
			renderJSONString(2);//失败
		}
		Group group = app.getTableGroup().get(table.getId());
		for(Student student : group.getStudents()){
			if (student.getUname().equals(uname)
					&& student.getNumber().equals(number)) {
				student.setLogin(false);
				app.refreshMainFrame();//更新UI
				return renderJSONString(0, group);
			}
		}
		return renderJSONString(3);//失败
	}
	
	/**
	 * 注册
	 * @param uname
	 * @param sex
	 * @param number
	 * @param imei
	 * @return
	 */
	public String register(String uname, int sex, String number, String imei){
		try {
			final String result = ApiClient.loginForStudent(uname, sex, number, imei);
			if (result != null && !result.equals("")) {
				JSONObject jsonObject = JSON.parseObject(result);
				if(jsonObject.getIntValue("code") == 0){
					String data = jsonObject.getString("data");
					Group group = JSON.parseObject(data, Group.class);
					
					app.refreshMainFrame();//更新UI
				}
				return result;
			}
		} catch (Exception e) {
			if(e instanceof AppException){
				return renderJSONString(1);//失败
			}
		}
		return renderJSONString(2);//失败
	}
}
