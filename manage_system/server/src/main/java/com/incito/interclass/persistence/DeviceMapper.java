package com.incito.interclass.persistence;

import java.util.List;

import com.incito.interclass.entity.Device;

public interface DeviceMapper {
	List<Device> getDeviceList();
	
	List<Device> getDeviceListByRoomId(int roomId);
	
	Integer save(Device device);
	
	void delete(int id);
}