package com.incito.interclass.app;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.incito.interclass.entity.Log;
import com.incito.interclass.business.LogService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.constant.Constants;

@RestController
@RequestMapping("/api/log")
public class LogCtrl extends BaseCtrl {

	@Autowired
	private LogService logService;
	

	/**
	 * 客户端上传日志
	 * @param type 类别，1是教师端，2是Pad端
	 * @param mac mac地址
	 * @param file 上传的文件
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	public String save(int type, String mac, MultipartFile file) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String filename = file.getOriginalFilename();
		File dir = new File(Constants.LOG_DIR + File.separator + sdf.format(date));
		dir.mkdirs();
		File newFile = new File(dir, filename);
		try {
			file.transferTo(newFile);
		} catch (IOException e) {
			return renderJSONString(-1);//文件上传失败
		}
		Log log = new Log();
		log.setType(type);
		log.setMac(mac);
		log.setUrl(newFile.getAbsolutePath());
		logService.saveLog(log);
		return renderJSONString(0);
	}
	
}
