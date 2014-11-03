package cn.com.incito.interclass.po;

import java.io.Serializable;
import java.util.Date;

public class Device implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5030861381666684624L;
	private int id;
	private String imei;
	private Date ctime;

	private String roomName;
	private String schoolName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}


	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}


}
