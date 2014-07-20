package com.incito.interclass.persistence;

import java.util.List;

import com.incito.interclass.entity.Room;

public interface RoomMapper {
	List<Room> getRoomList();

	Integer save(Room room);

	void delete(int id);
}
