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
	
	public List<Device> getDeviceListByRoomId(int roomId){
		return deviceMapper.getDeviceListByRoomId(roomId);
	}
	
	public int saveDevice(Device device) {
		return (Integer) deviceMapper.save(device);
	}

	public void deleteDevice(int deviceId) {
		deviceMapper.delete(deviceId);
	}
}
