package com.incito.interclass.entity;

import java.io.Serializable;
import java.util.Date;

public class Table implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4522455030940880046L;

	private int id;
	private int roomId;
	private String number;
	private Date ctime;

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

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

}
