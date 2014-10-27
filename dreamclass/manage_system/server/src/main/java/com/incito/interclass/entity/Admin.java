package com.incito.interclass.entity;

import com.incito.base.util.Md5Utils;

public class Admin extends User {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1469903255940391815L;
	private int id;
	private String phone;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public static void main(String[] args) {
		System.out.println(Md5Utils.md5("admin"));
	}
}
