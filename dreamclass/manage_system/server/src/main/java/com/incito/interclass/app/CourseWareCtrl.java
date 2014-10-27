package com.incito.interclass.app;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.incito.interclass.business.cloud.CourseWareService;
import com.incito.interclass.common.BaseCtrl;

@RestController
@RequestMapping("/api/course")
public class CourseWareCtrl extends BaseCtrl {

	@Autowired
	private CourseWareService courseService;

	/**
	 * 保存课件
	 * 
	 * @return
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> upload(MultipartFile file) {
		int count = courseService.save(file);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", "0");
		map.put("count", count);
		return map;
	}

}
