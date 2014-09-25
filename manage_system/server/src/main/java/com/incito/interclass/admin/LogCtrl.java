package com.incito.interclass.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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
	public ModelAndView index(Log log, Integer page) {
		ModelAndView res = new ModelAndView("system/logList");
		if (page == null) {
			page = 0;
		}
		List<Log> logs = logService.getLogList(log, page * maxResults, maxResults);
		res.addObject("logs", logs);
		return res;
	}

}
