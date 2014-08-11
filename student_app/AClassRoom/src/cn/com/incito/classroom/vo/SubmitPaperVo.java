package cn.com.incito.classroom.vo;

public class SubmitPaperVo {
	private String imei;
	private String id;
	private String name;
	private byte[] paper;
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public byte[] getPaper() {
		return paper;
	}
	public void setPaper(byte[] paper) {
		this.paper = paper;
	}
	
}
