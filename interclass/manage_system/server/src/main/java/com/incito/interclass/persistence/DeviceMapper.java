package com.incito.interclass.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.incito.interclass.entity.Device;

public interface DeviceMapper {
	List<Device> getDeviceListByCondition(@Param("imei")String imei, @Param("schoolName")String schoolName);
	
	List<Device> getDeviceListByRoomId(int roomId);
	
	Integer save(Device device);
	
	void delete(int id);
}
