package com.incito.interclass.app;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.incito.interclass.business.GroupService;
import com.incito.interclass.business.UserService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.entity.Group;
import com.incito.interclass.entity.Student;

@RestController
@RequestMapping("/api/student")
public class StudentCtrl extends BaseCtrl {

	@Autowired
	private UserService userService;

	@Autowired
	private GroupService groupService;

	/**
	 * 学生增减分
	 * 
	 * @param studentId
	 * @param changeScore
	 * @return
	 */
	@RequestMapping(value = "/changepoint", produces = {"application/json;charset=UTF-8" })
	@ResponseBody
	public Map<String, Object> changePoint(String studentId, int score,int groupId) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (groupId == -1) {
			userService.changePoint(studentId, score);
			map.put("code", "0");
			return map;
		}
		int count = userService.changePoint(studentId, score,groupId);
		map.put("code", "0");
		map.put("score", count);
		return map;

	}
	
	/**
	 * 
	 * 更新勋章
	 * @param studentId
	 * @param changeScore
	 * @return
	 */
	@RequestMapping(value = "/medals", produces = {"application/json;charset=UTF-8" })
	@ResponseBody
	public Map<String, Object> updateMedals(int groupId,String medals) {
		userService.updateMedals(groupId, medals);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", "0");
		map.put("msg", "勋章奖励成功");
		return map;

	}
	/**
	 * 通过imei获得学生
	 * @param imei
	 * @return
	 */
	@RequestMapping(value = "/getStudentByImei", produces = {"application/json;charset=UTF-8" })
	@ResponseBody
	public String getStudentByImei(String imei){
		Student student=userService.getStudentByImei(imei);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", 0);
		map.put("data",student);
		return JSON.toJSONString(map);
	}
	

	@RequestMapping(value = "/test", produces = { "application/json;charset=UTF-8" })
	public String test() {
		Group group = groupService.getGroupById(2);
		return renderJSONString(SUCCESS, group);
	}
}
