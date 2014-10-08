package cn.com.incito.interclass.ui.widget;

public class Item {
	private int key;
	private String value;

	public Item(int key, String value) {
		this.key = key;
		this.value = value;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int getKey() {
		return key;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value;
	}
	
	
}
