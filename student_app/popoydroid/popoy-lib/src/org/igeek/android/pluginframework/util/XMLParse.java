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

package org.igeek.android.pluginframework.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.igeek.android.pluginframework.beans.Plugin;
import org.igeek.android.pluginframework.beans.PluginFeature;
import org.igeek.android.pluginframework.beans.PluginFeatureMethod;
import org.igeek.android.pluginframework.beans.PluginIntent;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

/**
 * @author 作者 E-mail:hangxin1940@gmail.com
 * @version 创建时间：2011-12-15 上午11:58:36 解析xml
 */
public class XMLParse {

	public static interface XmlElement {
		public static final String _PLUGIN = "plugin-features";
		public static final String _FEATURE = "feature";
		public static final String _METHOD = "method";
		public static final String _DESCRIPTION = "description";
		public static final String _NEEDCTX = "need-context";
		public static final String _NAME = "name";
		public static final String _NAME_SPACE = "";
		public static final String _INTENT = "intent";
		public static final String _ACTION = "action";
		public static final String _KEY = "key";
	}

	public XMLParse() {
	}

	/**
	 * 解析xml组装纯类名文本描述的插件
	 * 
	 * @param ins
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public Plugin parsePluginXML(InputStream ins) throws IOException,
			XmlPullParserException {
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(ins, "utf-8");

		Plugin plugin = new Plugin();

		List<PluginFeature> features = new ArrayList<PluginFeature>();

		Map<String, PluginIntent> intents = new HashMap<String, PluginIntent>();
		plugin.setIntents(intents);

		int event = parser.getEventType();

		PluginFeature feature = new PluginFeature();

		PluginFeatureMethod featureMethod = new PluginFeatureMethod();

		PluginIntent intent = new PluginIntent();

		// 一直走到文档结束
		while (event != XmlPullParser.END_DOCUMENT) {

			switch (event) {
			// 结点标签开始
			case XmlPullParser.START_TAG:

				// 主节点
				if (XmlElement._PLUGIN.equalsIgnoreCase(parser.getName())) {
					Log.i("org.igeek.android-plugin",
							"plugin.xml document start parse");
				}

				// 功能描述结点
				else if (XmlElement._DESCRIPTION.equalsIgnoreCase(parser
						.getName())) {
					plugin.setDescription(parser.getAttributeValue(
							XmlElement._NAME_SPACE, XmlElement._NAME));
				}

				// 功能描述结点
				else if (XmlElement._FEATURE.equalsIgnoreCase(parser.getName())) {
					feature.setFeatureName(parser.getAttributeValue(
							XmlElement._NAME_SPACE, XmlElement._NAME));
				}

				// 方法描述结点
				else if (XmlElement._METHOD.equalsIgnoreCase(parser.getName())) {
					featureMethod.setMethodName(parser.getAttributeValue(
							XmlElement._NAME_SPACE, XmlElement._NAME));
					if ("true".endsWith(parser.getAttributeValue(
							XmlElement._NAME_SPACE, XmlElement._NEEDCTX))) {
						featureMethod.setNeedContext(true);
					} else {
						featureMethod.setNeedContext(false);
					}
					featureMethod.setDescription(parser.nextText());
					event = parser.getEventType();
					if (event == XmlPullParser.END_TAG) {
						continue;
					}
				} else if (XmlElement._INTENT
						.equalsIgnoreCase(parser.getName())) {
					intent.setAction(parser.getAttributeValue(
							XmlElement._NAME_SPACE, XmlElement._ACTION));
					intent.setKey(parser.getAttributeValue(
							XmlElement._NAME_SPACE, XmlElement._KEY));
					intent.setDescription(parser.nextText());
					event = parser.getEventType();
					if (event == XmlPullParser.END_TAG) {
						continue;
					}
				}
				break;

			// 节点表前结束
			case XmlPullParser.END_TAG:
				// 方法描述结点
				if (XmlElement._METHOD.equalsIgnoreCase(parser.getName())) {
					feature.addMethod(featureMethod);
					featureMethod = new PluginFeatureMethod();
				}
				// 功能描述结点
				else if (XmlElement._FEATURE.equalsIgnoreCase(parser.getName())) {
					features.add(feature);
					feature = new PluginFeature();
				}
				// intent
				else if (XmlElement._INTENT.equalsIgnoreCase(parser.getName())) {
					plugin.putIntent(intent.getKey(), intent);
					intent = new PluginIntent();
				}
				// 主节点
				else if (XmlElement._PLUGIN.equalsIgnoreCase(parser.getName())) {
					Log.i("org.igeek.android-plugin",
							"plugin.xml document parsed ok");
				}
				break;
			}

			event = parser.next();
		}

		ins.close();

		plugin.setFeatures(features);
		return plugin;

	}

}
