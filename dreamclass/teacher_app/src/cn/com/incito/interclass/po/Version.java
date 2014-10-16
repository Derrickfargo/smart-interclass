package cn.com.incito.interclass.po;

import java.io.Serializable;
import java.util.Date;

public class Version implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1692187731213052318L;
	private int id;
	private int code;
	private String name;
	private String description;
	private int type;
	private String url;
	private boolean forcibly;
	private long fileSize;
	private Date ctime;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isForcibly() {
		return forcibly;
	}

	public void setForcibly(boolean forcibly) {
		this.forcibly = forcibly;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

}
