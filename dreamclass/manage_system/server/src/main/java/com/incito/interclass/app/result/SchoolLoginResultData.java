package com.incito.interclass.app.result;

import java.util.List;

import com.incito.interclass.entity.Room;
import com.incito.interclass.entity.School;



public class SchoolLoginResultData implements IApiResultData{
	private School school;
	private Room room;
	private List<Room> rooms;
	
	
	public School getSchool() {
		return school;
	}
	public void setSchool(School school) {
		this.school = school;
	}
	public Room getRoom() {
		return room;
	}
	public void setRoom(Room room) {
		this.room = room;
	}
	public List<Room> getRooms() {
		return rooms;
	}
	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}
}
