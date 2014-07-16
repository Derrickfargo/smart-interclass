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

import java.util.ArrayList;
import java.util.List;

import org.igeek.android.pluginframework.beans.Plugin;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * @author 作者 E-mail:hangxin1940@gmail.com
 * @version 创建时间：2011-12-15 上午11:55:50
 * 查找插件
 */
public class PluginSearch {
	
	/**
	 * 查找插件
	 */
	public PluginSearch() {
	}
	
	/**
	 * 获取所有插件
	 * @param context 句柄
	 * @return
	 * @throws NameNotFoundException
	 */
	public List<Plugin> getPlugins(Context context) throws NameNotFoundException{
		
		List<Plugin> plugins=new ArrayList<Plugin>();
		
		PackageManager  pkgManager=context.getPackageManager();
		String pkgName=context.getPackageName();
		String sharedUID=pkgManager.getPackageInfo(pkgName, PackageManager.GET_UNINSTALLED_PACKAGES).sharedUserId;
		
		List<PackageInfo> pkgs=pkgManager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		for(PackageInfo pkg	:pkgs){
			//查找具有相同suid的包，并且排除自身
			if(!sharedUID.equals(pkg.sharedUserId)||pkgName.equals(pkg.packageName))
				continue;
			
			Plugin plugin=new Plugin();
			plugin.setPkgInfo(pkg);
			plugin.setPluginLable(pkgManager.getApplicationLabel(pkg.applicationInfo).toString());
			plugins.add(plugin);
		
		}
		
		
		return plugins;
	}
}
