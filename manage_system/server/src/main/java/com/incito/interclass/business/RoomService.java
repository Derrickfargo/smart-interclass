package com.incito.interclass.business;

import java.util.List;

import org.springframework.stereotype.Service;

import com.incito.base.dao.BaseService;
import com.incito.interclass.entity.Room;

@Service
public class RoomService extends BaseService {


	public List<Room> getRoomList(){
		return findForList("getRoomList", null);
	}
	public boolean saveRoom(Room room) {
		int id = (Integer) addObject("saveRoom", room);
		return id != 0;
	}

	public void deleteRoom(int roomId) {
		delObject("deleteRoom", roomId);
	}
}
