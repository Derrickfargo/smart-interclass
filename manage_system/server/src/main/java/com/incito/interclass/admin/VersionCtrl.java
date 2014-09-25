package com.incito.interclass.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.incito.interclass.business.VersionService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.entity.Log;
import com.incito.interclass.entity.Version;

@RestController
@RequestMapping("/version")
public class VersionCtrl extends BaseCtrl {

	@Autowired
	private VersionService versionService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public ModelAndView index(Log log, Integer page) {
		ModelAndView res = new ModelAndView("system/versionList");
		if (page == null) {
			page = 0;
		}
		List<Version> versions = versionService.getVersionList(log, page * maxResults, maxResults);
		res.addObject("versions", versions);
		return res;
	}
	
}
