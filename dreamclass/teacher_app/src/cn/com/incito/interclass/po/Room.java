package cn.com.incito.interclass.po;

import java.io.Serializable;
import java.util.Date;

public class Room implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -585484913660232515L;

	private int id;
	private String name;
	private int schoolId;
	private String mac;
	private Date ctime;

	private String schoolName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(int schoolId) {
		this.schoolId = schoolId;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
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

}
