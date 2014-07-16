package com.popoy.tookit.Enum;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.text.TextUtils;


public class Dictionary {

	public static Enumeration[] getDictionary(Context context, String dictName) {
		dictName = dictName == null ? "" : dictName;
		Resources res = context.getResources();
		int id = res.getIdentifier(dictName, "array", context.getPackageName());
		if (id < 1)
			return null;
		Enumeration[] dict = getEnus(context, id);
		return dict == null ? new Enumeration[0] : dict;
	}

	public static Enumeration getEnum(Context context, int StringArrayId,
			String code) {
		if (TextUtils.isEmpty(code))
			return new Enumeration();
		Enumeration[] dict = getEnus(context, StringArrayId);
		if (dict == null || dict.length < 1)
			return new Enumeration();
		for (Enumeration e : dict) {
			if (code.equals(e.getCode())) {
				return e;
			}

		}
		return new Enumeration();
	}

	public static Enumeration[] getEnus(Context context, int id) {
		if (id < 1)
			return null;
		Resources res = context.getResources();
		String[] stringArray;
		try {
			stringArray = res.getStringArray(id);
		} catch (NotFoundException e) {
			e.printStackTrace();
			return null;
		}
		Enumeration[] array = new Enumeration[stringArray.length];
		for (int i = 0; i < array.length; i++) {
			String[] params = stringArray[i].split(",");
			switch (params.length) {
			case 2:
				array[i] = new Enumeration(params[0], params[1]);
				break;
			case 5:
				array[i] = new FatEnumeration(params[0], params[1],
						Boolean.valueOf(params[2]), params[3],
						Integer.valueOf(params[4]));
				break;
			default:
				array[i] = new Enumeration(params[0], params[1]);
				break;
			}
		}
		return array;
	}

	/**
	 * 生成将code转化为对应的name值
	 * 
	 * @param codes
	 * @param StringArrayId
	 * @return
	 */
	public static String codes2Names(Context context, String codes,
			int StringArrayId) {
		if (TextUtils.isEmpty(codes))
			return "";
		Enumeration[] dict = getEnus(context, StringArrayId);
		if (dict == null || dict.length < 1)
			return "";
		StringBuffer sb = new StringBuffer();
		String[] strs = codes.split(";");
		if (strs.length < 1)
			return "";
		for (Enumeration e : dict) {
			for (String str : strs) {
				if (str.equals(e.getCode())) {
					sb.append(e.getName());
				}
			}

		}
		return sb == null ? "" : sb.toString();

	}

	/**
	 * 生成将code转化为对应的name值
	 * 
	 * @param names
	 * @param StringArrayId
	 * @return
	 */
	public static String names2Codes(Context context, String names,
			int StringArrayId) {
		if (TextUtils.isEmpty(names))
			return "";
		Enumeration[] dict = getEnus(context, StringArrayId);
		if (dict == null || dict.length < 1)
			return "";
		StringBuffer sb = new StringBuffer();
		String[] strs = names.split(";");
		if (strs.length < 1)
			return "";
		for (Enumeration e : dict) {
			for (String str : strs) {
				if (str.equals(e.getName())) {
					sb.append(e.getCode());
				}
			}

		}
		return sb == null ? "" : sb.toString();

	}
}
