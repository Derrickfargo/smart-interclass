package com.incito.interclass.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incito.interclass.entity.Room;
import com.incito.interclass.persistence.RoomMapper;

@Service
public class RoomService {

	@Autowired
	private RoomMapper roomMapper;

	public List<Room> getRoomListByByCondition(String schoolName,String mac) {
		return roomMapper.getRoomListByByCondition(schoolName,mac);
	}

	public Room getRoomByMac(String mac) {
		return roomMapper.getRoomByMac(mac);
	}

	public boolean saveRoom(Room room) {
		try {
			roomMapper.save(room);
			return room.getId() != 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void deleteRoom(int roomId) {
		roomMapper.delete(roomId);
	}
}
