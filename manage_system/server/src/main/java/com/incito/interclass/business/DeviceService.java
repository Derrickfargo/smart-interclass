package com.incito.interclass.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incito.interclass.entity.Device;
import com.incito.interclass.persistence.DeviceMapper;

@Service
public class DeviceService {

	@Autowired
	private DeviceMapper deviceMapper;
	public List<Device> getDeviceList(Object parameterObject, int skipResults,
			int maxResults) {
		return deviceMapper.getDeviceList();
	}

	public List<Device> getDeviceList(){
		return deviceMapper.getDeviceList();
	}
	public int saveDevice(Device device) {
		return (Integer) deviceMapper.save(device);
	}

	public void deleteDevice(int deviceId) {
		deviceMapper.delete(deviceId);
	}
}
