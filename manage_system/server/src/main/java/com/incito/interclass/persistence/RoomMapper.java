package com.incito.interclass.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.incito.interclass.entity.Room;

public interface RoomMapper {
	List<Room> getRoomListByByCondition(@Param("schoolName") String schoolName,
			@Param("mac") String mac);

	Room getRoomByMac(String mac);

	Integer save(Room room);

	void delete(int id);
}
