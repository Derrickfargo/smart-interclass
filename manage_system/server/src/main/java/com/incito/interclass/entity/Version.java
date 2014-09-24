package com.incito.interclass.entity;

import java.io.Serializable;
import java.util.Date;

import com.incito.interclass.app.result.IApiResultData;


public class Version  implements Serializable,IApiResultData {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3312254137390874557L;
	private int id;
	private int code;
	private String name;
	private String url;
	private int type;
	private Date ctime;
	private String description;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getCode() {
		return code;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public Date getCtime() {
		return ctime;
	}
	
	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}
