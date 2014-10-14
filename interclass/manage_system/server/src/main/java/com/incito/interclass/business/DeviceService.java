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

	public List<Device> getDeviceListByCondition(String imei, String schoolName){
		return deviceMapper.getDeviceListByCondition(imei, schoolName);
	}
	
	public Device getDeviceByIMEI(String imei){
		return deviceMapper.getDeviceByIMEI(imei);
	}
	
	public boolean saveDevice(Device device) {
		return (Integer) deviceMapper.save(device) == 1;
	}

	public void deleteDevice(int deviceId) {
		deviceMapper.delete(deviceId);
	}
}
