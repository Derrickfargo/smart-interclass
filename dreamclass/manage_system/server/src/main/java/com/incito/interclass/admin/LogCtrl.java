package com.incito.interclass.admin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.incito.interclass.business.LogService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.entity.Log;
import com.incito.parent.pagehelper.PageHelper;
import com.incito.parent.pagehelper.PageInfo;

@RestController
@RequestMapping("/log")
public class LogCtrl extends BaseCtrl {

	@Autowired
	private LogService logService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public ModelAndView index(@RequestParam(value = "type", defaultValue = "-1")Integer type,String key, 
			@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,String address) {
		ModelAndView res = new ModelAndView("log/logList");
		PageHelper.startPage(pageNum, PAGE_SIZE);
		List<Log> logs = logService.getLogListByCondition(type,key,address);
		PageInfo<Log> page = new PageInfo<Log>(logs);
		res.addObject("page", page);
		res.addObject("type", type);
		res.addObject("key", key);
		res.addObject("address",address);
		return res;
	}

	@RequestMapping(value = "/delete")
	public ModelAndView delete(Integer logId) {
		logService.deleteLog(logId);
		return new ModelAndView("redirect:list");
	}
	
	@RequestMapping(value = "/download", produces = { "application/octet-stream;charset=UTF-8" })
	public void download(Integer logId, final HttpServletResponse response) throws IOException{  
		Log log = logService.getLogById(logId);
	    File file = new File(log.getUrl());
	    String fileName = URLEncoder.encode(file.getName(), "UTF-8");
	    response.reset();  
	    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");  
	    response.addHeader("Content-Length", "" + file.length());  
	    response.setContentType("application/octet-stream;charset=UTF-8");
	    
	    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file.getAbsolutePath()));
	    BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
	    byte[] buff = new byte[2048];
	    int bytesRead;
		while ((bytesRead = bis.read(buff, 0, buff.length)) != -1) {
			bos.write(buff, 0, bytesRead);
		}
	    bis.close();
	    bos.close();
	}
	
	@RequestMapping(value = "/view")
	public ModelAndView view(Integer logId) {
		ModelAndView res = new ModelAndView("log/logView");
		Log log = logService.getLogById(logId);
		res.addObject("log", log);
		return res;
	}
}
