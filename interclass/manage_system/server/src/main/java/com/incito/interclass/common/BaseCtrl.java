package com.incito.interclass.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.incito.interclass.entity.User;

/**
 * 控制层基类
 * 
 */
public class BaseCtrl {

	@Autowired
	protected HttpServletRequest request;
	@Autowired
	protected HttpSession session;
	protected int maxResults = 10;
	protected final String WEB_SESSION_USER = "user";

	protected static final int SUCCESS = 0;
	protected static final int PAGE_SIZE = 10;
	/**
	 * 获取当前用户信息
	 * 
	 * @return
	 */
	protected User getUser() {
		return (User) session.getAttribute(WEB_SESSION_USER);
	}

	/**
	 * 获取请求参数 返回Map 格式
	 * 
	 * @return
	 */
	protected Map<String, Object> getReqParamMap() {
		// 使用map接收页面表单参数信息
		Map<String, Object> params = new HashMap(request.getParameterMap());

		// 由于接收的map值 Object 内容是String[]格式，在此需要格式转换
		Set<String> keys = params.keySet();
		for (String key : keys) {
			String value = "";
			Object valueObj = params.get(key);
			if (null == valueObj) {
				value = "";
			} else if (valueObj instanceof String[]) {
				String[] values = (String[]) valueObj;
				for (int i = 0; i < values.length; i++) {
					value += values[i] + ",";
				}
				value = value.substring(0, value.length() - 1);
			} else {
				value = valueObj.toString();
			}
			params.put(key, value);
		}
		return params;
	}

	/**
	 * 获取请求参数 返回Map 格式，主要用于包含文件上传的form表单
	 * 
	 * @param request请求
	 */
	protected Map<String, Object> getReqParamMap(HttpServletRequest req) {
		// 获取文件上传组件的request请求对象
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) req;
		// 使用map接收页面表单参数信息
		Map<String, Object> params = new HashMap(multipartRequest.getParameterMap());

		// 由于接收的map值 Object 内容是String[]格式，在此需要格式转换
		Set<String> keys = params.keySet();
		for (String key : keys) {
			String value = "";
			Object valueObj = params.get(key);
			if (null == valueObj) {
				value = "";
			} else if (valueObj instanceof String[]) {
				String[] values = (String[]) valueObj;
				for (int i = 0; i < values.length; i++) {
					value += values[i] + ",";
				}
				value = value.substring(0, value.length() - 1);
			} else {
				value = valueObj.toString();
			}
			params.put(key, value);
		}
		return params;
	}

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
}
