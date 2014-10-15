package com.incito.interclass.app;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incito.interclass.business.VersionService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.entity.Version;

@RestController
@RequestMapping("/api/version")
public class VersionCtrl extends BaseCtrl {

	@Autowired
	private VersionService versionService;
	

	/**
	 * 根据类型和版本代码检查是否有更新
	 * @param type	类型：1是教师端，2是学生端
	 * @param code 版本代码
	 * @return
	 */
	@RequestMapping(value = "/check", produces = { "application/json;charset=UTF-8" })
	public String checkVersion(int type, int code) {
		Version version = versionService.getVersion(type, code);
		if (version == null || version.getId() == 0) {
			return renderJSONString(SUCCESS);
		}
		return renderJSONString(SUCCESS, version);
	}
	
	/**
	 * 下载版本
	 * @param id
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/download", produces = { "application/octet-stream;charset=UTF-8" })
	public void download(Integer versionId, final HttpServletResponse response) throws IOException{  
		Version version = versionService.getVersionById(versionId);
	    File file = new File(version.getUrl());
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
}
