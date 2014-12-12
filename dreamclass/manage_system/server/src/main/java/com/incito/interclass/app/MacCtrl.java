package com.incito.interclass.app;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.incito.interclass.business.RoomService;
import com.incito.interclass.common.BaseCtrl;

@RestController
@RequestMapping("/api/mac")
public class MacCtrl extends BaseCtrl{
	@Autowired
	private RoomService roomservice;
	@RequestMapping(value = "/checkMac", produces = { "application/json;charset=UTF-8" })
	public String checkMac(String mac){
		int code=roomservice.checkMack(mac);
		Map<String, Object> result= new HashMap<String, Object>();
		result.put("code", code);
		result.put("mac",mac);
		
		return JSON.toJSONString(result);
	}
}
