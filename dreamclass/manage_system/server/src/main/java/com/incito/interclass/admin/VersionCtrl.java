package com.incito.interclass.admin;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.incito.interclass.business.VersionService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.constant.Constants;
import com.incito.interclass.entity.Version;
import com.incito.parent.pagehelper.PageHelper;
import com.incito.parent.pagehelper.PageInfo;

@RestController
@RequestMapping("/version")
public class VersionCtrl extends BaseCtrl {

	@Autowired
	private VersionService versionService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public ModelAndView index(@RequestParam(value = "type", defaultValue = "-1")Integer type,
			@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
		ModelAndView res = new ModelAndView("version/versionList");
		PageHelper.startPage(pageNum, PAGE_SIZE);
		List<Version> versions = versionService.getVersionListByCondition(type);
		PageInfo<Version> page = new PageInfo<Version>(versions);
		res.addObject("page", page);
		res.addObject("type", type);
		return res;
	}
	
	/**
	 * 添加版本
	 */
	@RequestMapping("/add")
	public ModelAndView add() {
		ModelAndView mav = new ModelAndView("version/versionAdd");
		return mav;
	}
	
	/**
	 * 保存
	 * @return
	 */
	@RequestMapping(value = "/save")
	public ModelAndView save(String name, Integer code, String description, MultipartFile teacherFile,MultipartFile stuFile) {
		Version stuVersion = saveVersion(2, name, code, description, stuFile);
		Version teacherVersion = saveVersion(1, name, code, description, teacherFile);
		
		if(stuVersion==null||teacherVersion==null)	
		return new ModelAndView("version/versionAdd");
		
		versionService.saveVersion(stuVersion);
		versionService.saveVersion(teacherVersion);
		return new ModelAndView("redirect:/version/list");
	}
	
	@RequestMapping(value = "/delete")
	public ModelAndView delete(Integer id) {
		versionService.deleteVersion(id);
		return new ModelAndView("redirect:list");
	}
	
	private Version saveVersion(Integer type , String name, Integer code, String description, MultipartFile file){
		Version version = new Version();
		version.setType(type);
		version.setCode(code);
		version.setName(name);
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat time = new SimpleDateFormat("HH-mm-ss");
		String filename = file.getOriginalFilename();
		File dir = new File(Constants.VERSION_DIR + File.separator
				+ sdf.format(date) + File.separator + time.format(date));
		dir.mkdirs();
		File newFile = new File(dir, filename);
		try {
			file.transferTo(newFile);
		} catch (IllegalStateException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		version.setUrl(newFile.getAbsolutePath());
		version.setFileSize(newFile.length());
		version.setDescription(description);
		return version;
	}
}
