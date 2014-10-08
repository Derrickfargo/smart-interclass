package cn.com.incito.interclass.po;

import java.io.Serializable;

public class StudentGroup implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6517861100227656945L;

	private int id;
	private int studentId;
	private int groupId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

}
