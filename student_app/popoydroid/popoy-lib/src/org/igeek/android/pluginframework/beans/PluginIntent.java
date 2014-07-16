package org.igeek.android.pluginframework.beans;

import android.content.Intent;

/**
 * 插件暴漏的Intent
 * 
 * @author CDroid
 */
public class PluginIntent {

	public static interface IntentKey {
		/** 插件主入口 */
		public static final String CATEGORY_MAIN = "main";
	}

	private String action;
	private String key;
	private String description; // 描述

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Intent getIntent() {
		return new Intent(action);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
