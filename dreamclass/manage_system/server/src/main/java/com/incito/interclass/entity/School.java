package com.incito.interclass.entity;

import java.io.Serializable;
import java.util.Date;

public class School implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5177659123357676548L;

	private int id;
	private String name;
	private String schoolAlias;
	private String schoolNumber;
	private String phone;
	private String email;
	private String address;
	private String zipCode;
	private int schoolType;
	private int educationalType;
	private String intro;
	private Date ctime;

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

	public String getSchoolAlias() {
		return schoolAlias;
	}

	public void setSchoolAlias(String schoolAlias) {
		this.schoolAlias = schoolAlias;
	}

	public String getSchoolNumber() {
		return schoolNumber;
	}

	public void setSchoolNumber(String schoolNumber) {
		this.schoolNumber = schoolNumber;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public int getSchoolType() {
		return schoolType;
	}

	public void setSchoolType(int schoolType) {
		this.schoolType = schoolType;
	}

	public int getEducationalType() {
		return educationalType;
	}

	public void setEducationalType(int educationalType) {
		this.educationalType = educationalType;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

}
