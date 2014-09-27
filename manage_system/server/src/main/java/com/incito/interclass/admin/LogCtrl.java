package com.incito.interclass.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.incito.interclass.business.LogService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.entity.Log;

@RestController
@RequestMapping("/log")
public class LogCtrl extends BaseCtrl {

	@Autowired
	private LogService logService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public ModelAndView index(@RequestParam(value = "type", defaultValue = "1")Integer type,String key, 
			@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
		ModelAndView res = new ModelAndView("system/logList");
		PageHelper.startPage(pageNum, PAGE_SIZE);
		List<Log> logs = logService.getLogListByCondition(type,key);
		PageInfo<Log> page = new PageInfo<Log>(logs);
		res.addObject("page", page);
		res.addObject("type", type);
		res.addObject("key", key);
		return res;
	}

}
