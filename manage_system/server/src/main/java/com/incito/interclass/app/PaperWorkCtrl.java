package com.incito.interclass.app;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.incito.interclass.app.result.ApiResult;
import com.incito.interclass.business.cloud.PaperWorkService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.entity.cloud.PaperWork;
import com.incito.interclass.entity.cloud.TestEntity;

@RestController
@RequestMapping("/api/paper")
public class PaperWorkCtrl extends BaseCtrl {

	@Autowired
	private PaperWorkService paperService;

	/**
	 * 保存练习题
	 * 
	 * @return
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Map<String, Object> upload(String type, String teacher_id,
			String imei, String quizid,String lastupdatetime, MultipartFile file) {
		PaperWork paperWork = new PaperWork();
		paperWork.setType(type);
		paperWork.setTeacher_id(teacher_id);
		paperWork.setImei(imei);
		paperWork.setQuizid(quizid);
		int count = paperService.upload(paperWork, file);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", "0");
		map.put("lastupdatetime", lastupdatetime);
		map.put("count", count);
		return map;
	}
}
