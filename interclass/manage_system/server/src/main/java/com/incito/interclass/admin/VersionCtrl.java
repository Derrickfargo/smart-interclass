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
	public ModelAndView index(@RequestParam(value = "type", defaultValue = "1")Integer type,
			@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
		ModelAndView res = new ModelAndView("system/versionList");
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
	public ModelAndView save(int type, int code, MultipartFile file) {
		Version version = new Version();
		version.setType(type);
		version.setCode(code);
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
		} catch (IOException e) {
			return new ModelAndView("version/versionAdd");
		}
		version.setUrl(newFile.getAbsolutePath());
		version.setName(filename);
		versionService.saveVersion(version);
		return new ModelAndView("redirect:/version/list");
	}
	
	@RequestMapping(value = "/delete")
	public ModelAndView delete(int id) {
		versionService.deleteVersion(id);
		return new ModelAndView("redirect:list");
	}
}
