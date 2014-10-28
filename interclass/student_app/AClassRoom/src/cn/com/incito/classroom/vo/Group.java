package cn.com.incito.classroom.vo;

import java.util.List;

public class Group {
	
	private  int iconSourceId;
	private String name;
	private List<String> memberNames;
	
	public int getIconSourceId() {
		return iconSourceId;
	}
	public void setIconSourceId(int iconSourceId) {
		this.iconSourceId = iconSourceId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getMemberNames() {
		return memberNames;
	}
	public void setMemberNames(List<String> memberNames) {
		this.memberNames = memberNames;
	}
	
	

}
