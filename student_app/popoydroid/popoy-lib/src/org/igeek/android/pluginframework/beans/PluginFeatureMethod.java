/*
 * Copyright (C) 2011 hangxin1940@gmail.com  http://hangxin1940.cnblogs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.igeek.android.pluginframework.beans;


/**
 * @author 作者 E-mail:hangxin1940@gmail.com
 * @version 创建时间：2011-12-15 下午01:01:45
 * 插件某功能提供的方法，也就相当于插件提供的某类的方法
 * 目前，方法只支持最多
 */
public class PluginFeatureMethod {
	private String methodName; //方法名
	private String description; //描述
	
	private boolean needContext; //需要窗口句柄

	/**
	 * 插件类所提供的方法
	 */
	public PluginFeatureMethod() {
	}
	



	/**
	 * 获得方法名
	 * @return
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * 设置方法名
	 * @param methodName
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}




	public boolean needContext() {
		return needContext;
	}




	public void setNeedContext(boolean needContext) {
		this.needContext = needContext;
	}




	public String getDescription() {
		return description;
	}




	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
