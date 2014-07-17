package cn.com.incito.interclass.po;


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

}
