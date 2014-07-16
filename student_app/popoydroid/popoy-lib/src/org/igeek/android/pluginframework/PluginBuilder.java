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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.igeek.android.pluginframework.beans.Plugin;
import org.igeek.android.pluginframework.util.XMLParse;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * @author 作者 E-mail:hangxin1940@gmail.com
 * @version 创建时间：2011-12-15 下午03:29:38
 * 插件构造器
 */
public class PluginBuilder {

	private Context context;
	private Context pluginContext;
	
	/**
	 * 插件构造器
	 * @param context 句柄
	 */
	public PluginBuilder(Context context) {
		this.context=context;
		
	}
	
	
	
	
	/**
	 * 为给定的插件组装功能
	 * @param plugins 搜索到的插件
	 * @return 组装好的插件
	 * @throws NameNotFoundException 
	 * @throws IOException 
	 * @throws XmlPullParserException 
	 */
	public List<Plugin> buildPluginsDescrition(List<Plugin> plugins) throws NameNotFoundException, IOException, XmlPullParserException{
		
		List<Plugin> richPlugin=new ArrayList<Plugin>();
		
		for(Plugin plugin:plugins){
			richPlugin.add(getPluginFeatureByXML(plugin));
		}
		return richPlugin;
	}
	
	/**
	 * 获取指定插件的功能,XML解析
	 * @param plugin
	 * @return
	 * @throws NameNotFoundException 
	 * @throws IOException
	 * @throws XmlPullParserException 
	 */
	private Plugin getPluginFeatureByXML(Plugin plugin) throws NameNotFoundException, IOException, XmlPullParserException{
		pluginContext=context.createPackageContext(plugin.getPkgInfo().packageName, Context.CONTEXT_INCLUDE_CODE|Context.CONTEXT_IGNORE_SECURITY);
		
		InputStream pluginXmlInput=pluginContext.getAssets().open("plugin.xml");
		XMLParse parser=new XMLParse();
		Plugin pluginFinal= parser.parsePluginXML(pluginXmlInput);
		
		pluginFinal.setPkgInfo(plugin.getPkgInfo());
		pluginFinal.setPluginLable(plugin.getPluginLable());
		pluginFinal.setContext(pluginContext);
		return pluginFinal;
		
	}
	
	/**
	 * 获取指定插件的功能,注解解析
	 * @param plugin
	 * @return
	 * @throws NameNotFoundException 
	 */
	private Plugin getPluginFeatureByAnnotation(Plugin plugin) throws NameNotFoundException{
		pluginContext=context.createPackageContext(plugin.getPkgInfo().packageName, Context.CONTEXT_INCLUDE_CODE|Context.CONTEXT_IGNORE_SECURITY);
		
		//pluginContext.getApplicationInfo().
		
		return plugin;
		
	}
	
	
	
}
