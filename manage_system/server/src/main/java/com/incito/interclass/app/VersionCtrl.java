package com.incito.interclass.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.incito.interclass.app.result.ApiResult;
import com.incito.interclass.business.VersionService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.entity.Version;

@RestController
@RequestMapping("/api/version")
public class VersionCtrl extends BaseCtrl {

	@Autowired
	private VersionService versionService;

	@RequestMapping(value = "/update", produces = { "application/json;charset=UTF-8" })
	public String updateApk(int oldVersion, int type) {
		Version data = versionService.updateApk(oldVersion, type);
		ApiResult result = new ApiResult();
		result.setCode(ApiResult.SUCCESS);
		result.setData(data);
		return JSON.toJSONString(result);
	}
}
