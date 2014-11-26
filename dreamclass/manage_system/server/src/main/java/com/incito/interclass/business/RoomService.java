package com.incito.interclass.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incito.interclass.entity.Room;
import com.incito.interclass.entity.School;
import com.incito.interclass.persistence.RoomMapper;

@Service
public class RoomService {

	@Autowired
	private RoomMapper roomMapper;

	public List<Room> getRoomListByByCondition(String schoolName,String mac) {
		return roomMapper.getRoomListByByCondition(schoolName,mac);
	}

	public Room getRoomById(int id) {
		return roomMapper.getRoomById(id);
	}
	
	public boolean update(Room room){
		try {
			int result = roomMapper.update(room);
			return result == 1;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
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

	public List<School> search(String name, int pageNum) {
	
		List<School> room=roomMapper.getRoomListByVagueName(name,pageNum);
		
		return room;
	}

	public List<Room> getRoomListBySchoolId(int id) {
		return roomMapper.getRoomListBySchoolId(id);
	}
}
