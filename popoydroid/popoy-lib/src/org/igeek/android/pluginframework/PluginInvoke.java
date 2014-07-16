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


package org.igeek.android.pluginframework;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.igeek.android.pluginframework.beans.Plugin;
import org.igeek.android.pluginframework.beans.PluginFeature;
import org.igeek.android.pluginframework.beans.PluginFeatureMethod;
import org.igeek.android.pluginframework.beans.PluginIntent;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

/**
 * @author 作者 E-mail:hangxin1940@gmail.com
 * @version 创建时间：2011-12-15 下午11:31:47
 * 执行插件
 */
public class PluginInvoke {

	private Context context;
	
	/**
	 * 执行插件
	 * 
	 * @param context 参数context必须是主程序的句柄
	 */
	public PluginInvoke(Context context) {
		this.context=context;
	}
	
	/**
	 * 调用指定的插件的指定功能
	 * @param plugin
	 * @param feature 功能描述 
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws InstantiationException 
	 * @throws IllegalAccessException 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws NameNotFoundException 
	 */
	public void invoke(Plugin plugin,PluginFeature feature,PluginFeatureMethod method) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException, NameNotFoundException{
		
		Context targetContext=context.createPackageContext(plugin.getPkgInfo().packageName, Context.CONTEXT_INCLUDE_CODE|Context.CONTEXT_IGNORE_SECURITY);
		
		Class<?> c=targetContext.getClassLoader().loadClass(feature.getFeatureName());
		Object pluginActivity=c.newInstance();
		
		Method target;
		if(method.needContext()){
			target=c.getMethod(method.getMethodName(),Context.class);
			target.invoke(pluginActivity,context);	
		}
		else{
			target=c.getMethod(method.getMethodName());
			target.invoke(pluginActivity);		
		}
			
		
	}
	
	public void invoke(PluginIntent intent) {
		invoke(intent, null);
	}

	public void invoke(PluginIntent intent, Bundle extras) {
		Intent i = intent.getIntent();
		if (extras != null) {
			i.putExtras(extras);
		}
		context.startActivity(intent.getIntent());
	}
}
