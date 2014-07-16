package com.incito.interclass.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.incito.interclass.business.DeviceService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.entity.Device;

@RestController
@RequestMapping("/device")
public class DevicelCtrl extends BaseCtrl {

	@Autowired
	private DeviceService deviceService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public ModelAndView index(Device device,Integer page) {
		ModelAndView res = new ModelAndView("device/deviceList");
		if (page == null) {
			page = 0;
		}
		List<Device> devices = deviceService.getDeviceList(device, page * maxResults, maxResults);
		res.addObject("devices", devices);
		
		return res;
	}
	
}
