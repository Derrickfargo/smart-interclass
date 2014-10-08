package com.incito.interclass.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Table implements Serializable,Comparable<Table> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4522455030940880046L;

	private int id;
	private int roomId;
	private int number;
	private Date ctime;

	private List<Device> devices;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public List<Device> getDevices() {
		return devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}

	@Override
	public int compareTo(Table o) {
		return number - o.getNumber();
	}

}
