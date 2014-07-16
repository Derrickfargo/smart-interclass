package com.popoy.tookit.Enum;

@SuppressWarnings("serial")
public class FatEnumeration extends Enumeration {
	/**
	 * 
	 */
	private boolean expandable;
	private String expandingValue;
	private int group;
	
	public FatEnumeration(){}
	
	public FatEnumeration(String code, String name,
			boolean expandable, String expandingValue, int group) {
		super(code, name);
		this.expandable = expandable;
		this.expandingValue = expandingValue;
		this.group = group;
	}

	public boolean isExpandable() {
		return expandable;
	}

	public void setExpandable(boolean expandable) {
		this.expandable = expandable;
	}

	public String getExpandingValue() {
		return expandingValue;
	}

	public void setExpandingValue(String expandingValue) {
		this.expandingValue = expandingValue;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}
	
	@Override
	public String toString() {
		return "FatEnumeration{code=" + getCode()
				+ ", name=" + getName()
				+ ", expandable=" + expandable
				+ ", expandingValue=" + expandingValue
				+ ", group=" + group + "}";
	}
}
