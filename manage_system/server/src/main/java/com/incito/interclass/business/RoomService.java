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
	

	public List<Room> getRoomList(){
		return roomMapper.getRoomList();
	}
	public boolean saveRoom(Room room) {
		int id = (Integer) roomMapper.save(room);
		return id != 0;
	}

	public void deleteRoom(int roomId) {
		roomMapper.delete(roomId);
	}
}
