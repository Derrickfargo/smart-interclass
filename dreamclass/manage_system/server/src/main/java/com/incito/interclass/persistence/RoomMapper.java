package com.incito.interclass.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.incito.interclass.entity.Room;
import com.incito.interclass.entity.School;

public interface RoomMapper {
	List<Room> getRoomListByByCondition(@Param("schoolName") String schoolName,
			@Param("mac") String mac);

	Room getRoomByMac(String mac);

	public Room getRoomById(int id);
	
	public Integer update(Room room);
	
	Integer save(Room room);

	void delete(int id);

	List<School> getRoomListByVagueName(@Param("schoolName") String SchoolName,@Param("pageNum") int pageNum);

	List<Room> getRoomListBySchoolId(int id);

	Room checkMac(String mac);
}
