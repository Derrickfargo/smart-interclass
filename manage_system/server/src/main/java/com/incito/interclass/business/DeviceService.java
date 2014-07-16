package com.incito.interclass.business;

import java.util.List;

import org.springframework.stereotype.Service;

import com.incito.base.dao.BaseService;
import com.incito.interclass.entity.Device;

@Service
public class DeviceService extends BaseService {

	public List<Device> getDeviceList(Object parameterObject, int skipResults,
			int maxResults) {
		return findForList("getDeviceList", parameterObject, skipResults,
				maxResults);
	}

	public List<Device> getDeviceList(){
		return findForList("getDeviceList", null);
	}
	public int saveDevice(Device device) {
		return (Integer) addObject("saveDevice", device);
	}

	public void deleteDevice(int deviceId) {
		delObject("deleteDevice", deviceId);
	}
}
