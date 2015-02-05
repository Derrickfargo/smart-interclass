package cn.com.incito.common.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;

/**
 * 创建快捷方式工具类
 * @author hm
 *
 */
public final class ShortCutUtil {
	
	/**
	 * 判断是否已经创建快捷方式
	 * @param context
	 * @param appName  应用名称
	 * @return
	 */
	private static boolean hasAddShortCut(Context context,String appName){
		ContentResolver contentResolver = context.getContentResolver();
		int versionLevel = android.os.Build.VERSION.SDK_INT;// 系统版本
		String systemFileName = "";
		if (versionLevel > 8) {
			systemFileName = "com.android.launcher2.settings";
		} else {
			systemFileName = "com.android.launcher.settings";
		}
		final Uri CONTENT_URI = Uri.parse("content://" + systemFileName + "/favorites?notify=true");
		Cursor cursor = contentResolver.query(CONTENT_URI, new String[] {"title", "iconResource" }, "title=?",new String[] { appName }, null);
		if (cursor != null && cursor.getCount() > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 添加快捷方式
	 * @param context
	 * @param clazz  点击快捷方式显示的界面
	 * @param iconId 快捷方式图标
	 * @param appName  应用名称
	 */
	public static void addShortCut(Context context,Class<? extends Activity> clazz,int iconId,String appName){
		
		/**
		 * 判断是否已经创建快捷方式
		 */
		if(hasAddShortCut(context, appName)){
			return;
		}
		
		Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		/**
		 * 设置属性
		 */
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,appName);

		/**
		 * 是否允许重复创建
		 */
		shortcut.putExtra("duplicate", false);

		/**
		 * 设置桌面快捷方式的图标
		 */
		Parcelable icon = Intent.ShortcutIconResource.fromContext(context,iconId);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);

		/**
		 *  点击快捷方式的操作
		 */
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setClass(context, clazz);

		/**
		 * 设置启动程序
		 */
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);

		/**
		 * 广播通知桌面去创建
		 */
		context.sendBroadcast(shortcut);
	}

}
