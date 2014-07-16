package com.popoy.tookit.Enum;

import java.io.Serializable;

public class Enumeration implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7633889625103584417L;
	private String code;
	private String name;

	public Enumeration() {
	}

	public Enumeration(String code, String name) {
		this.code = code;
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		Enumeration oo = (Enumeration) o;
		if (oo.code != null && oo.code.equals(this.code)) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return code == null ? 0 : code.hashCode();
	}

	public final int internalId() {
		return code.hashCode();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
