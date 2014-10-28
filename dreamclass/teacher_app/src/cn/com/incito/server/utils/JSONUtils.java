package cn.com.incito.server.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class JSONUtils {
	
	public static final int SUCCESS = 0;
	
	/**
	 * 输出为JSON字符串
	 * 
	 * @param result
	 *            是否成功，0：成功，非零：失败（不同数值不同原因）
	 * @return JSON字符串
	 */
	public static String renderJSONString(int result) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", result);
		return JSON.toJSONString(map);
	}

	/**
	 * 输出为JSON字符串
	 * 
	 * @param result
	 *            是否成功，0：成功，非零：失败（不同数值不同原因）
	 * @param data
	 *            要转换为JSON字符串的对象
	 * @return JSON字符串
	 */
	public static String renderJSONString(int result, Object data) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", result);
		map.put("data", data);
		return JSON.toJSONString(map);
	}

	/**
	 * 输出为JSON字符串
	 * 
	 * @param result
	 *            是否成功，0：成功，非零：失败（不同数值不同原因）
	 * @param data
	 *            要转换为JSON字符串的List对象
	 * @return JSON字符串
	 */
	public static <T> String renderJSONString(int result, List<T> data) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", result);
		map.put("data", data);
		return JSON.toJSONString(map);
	}

	public static <T> String renderJSONString(int result, Map<String, T> data) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", result);
		map.put("data", data);
		return JSON.toJSONString(map);
	}
}
