package com.incito.interclass.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.incito.interclass.business.DeviceService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.entity.Device;
import com.incito.parent.pagehelper.PageHelper;
import com.incito.parent.pagehelper.PageInfo;

@RestController
@RequestMapping("/device")
public class DevicelCtrl extends BaseCtrl {

	@Autowired
	private DeviceService deviceService;
	
	/** 
	 * 列表
	 */
	@RequestMapping("/list")
	public ModelAndView index(String imei, String schoolName,
			@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
		ModelAndView res = new ModelAndView("device/deviceList");
		PageHelper.startPage(pageNum, PAGE_SIZE);
		List<Device> devices = deviceService.getDeviceListByCondition(imei,schoolName);
		PageInfo<Device> page = new PageInfo<Device>(devices);
		res.addObject("page", page);
		res.addObject("imei", imei);
		res.addObject("schoolName", schoolName);
		res.addObject("pageNum", pageNum);
		return res;
	}
	
}
