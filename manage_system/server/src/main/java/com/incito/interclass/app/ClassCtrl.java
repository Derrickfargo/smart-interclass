package com.incito.interclass.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incito.interclass.business.ClassService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.entity.Classes;

@RestController
@RequestMapping("/api/class")
public class ClassCtrl extends BaseCtrl {

	private static final int ERROR = 1;

	@Autowired
	private ClassService classService;

	/**
	 * 添加班级
	 * 
	 * @param Classes
	 * @return
	 */
	@RequestMapping(value = "/register", produces = { "application/json;charset=UTF-8" })
	public String register(Classes classes) {
		if (!classService.saveClass(classes)) {
			return renderJSONString(ERROR);
		} else {
			return renderJSONString(SUCCESS);
		}
	}

}
