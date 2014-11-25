package cn.com.incito.server.api.result;

import java.util.List;

import cn.com.incito.interclass.po.Room;
import cn.com.incito.interclass.po.School;

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
