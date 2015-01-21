/**
 * CopyRight 2011 Declan.z
 */
package cn.com.incito.common.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import cn.com.incito.classroom.utils.ApiClient;
/**
 * @author john
 *android 工具类
 */
public class AndroidUtil {
	
	 /** 
     * 获得指定文件的byte数组 
     */  
    public static byte[] getBytes(File file){  
        byte[] buffer = null;  
        try {  
            FileInputStream fis = new FileInputStream(file);  
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);  
            byte[] b = new byte[1000];  
            int n;  
            while ((n = fis.read(b)) != -1) {  
                bos.write(b, 0, n);  
            }  
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return buffer;  
    }  
  
    /** 
     * 根据byte数组，生成文件 
     */  
    public static void getFile(byte[] bfile, String filePath,String fileName) {  
        BufferedOutputStream bos = null;  
        FileOutputStream fos = null;  
        File file = null;  
        try {  
            File dir = new File(filePath);  
            if(!dir.exists()&&dir.isDirectory()){//判断文件目录是否存在  
                dir.mkdirs();  
            }  
            file = new File(filePath+"\\"+fileName);  
            fos = new FileOutputStream(file);  
            bos = new BufferedOutputStream(fos);  
            bos.write(bfile);  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if (bos != null) {  
                try {  
                    bos.close();  
                } catch (IOException e1) {  
                    e1.printStackTrace();  
                }  
            }  
            if (fos != null) {  
                try {  
                    fos.close();  
                } catch (IOException e1) {  
                    e1.printStackTrace();  
                }  
            }  
        }  
    }  

	/**
	 * sdcard是否可用
	 * @return
	 */
	public static boolean isSDCardAvailable() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	/**
	 * 定位器是否可用 gps或者netwrok打开一个即可
	 * @param context
	 * @return
	 */
	public static boolean isLocationProviderAvailable(Context context) {
		String locations = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		return locations != null && (locations.contains(LocationManager.GPS_PROVIDER) || locations.contains(LocationManager.NETWORK_PROVIDER));
	}


	/**
	 * 是否打开GPS
	 * @param context
	 * @return
	 */
	public static boolean isGpsProviderAvailable(Context context) {
		String locations = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		return locations != null && locations.contains(LocationManager.GPS_PROVIDER);
	}

	/**
	 * 网络连接是否可用
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
			return false;
		}
		NetworkInfo[] netinfo = cm.getAllNetworkInfo();
		if (netinfo == null) {
			return false;
		}
		for (int i = 0; i < netinfo.length; i++) {
			if (netinfo[i].isConnected()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取屏幕宽度
	 * @return
	 */
	public static String getScreenSize(Activity context) {
		DisplayMetrics displayMetrics = getDisplayMetrics(context);
		return displayMetrics.widthPixels + "x" + displayMetrics.heightPixels;
	}

	/**
	 * @Title: getDensityDPI
	 * @Description: 获取屏幕密度
	 * @param @param context
	 * @param @return 设定文件
	 * @return int 返回类型
	 * @throws
	 */
	public static int getDensityDPI(Context pContext) {
		return getDisplayMetrics(pContext).densityDpi;
	}

	/**
	 * @Title: getDisplayMetrics
	 * @Description: TODO
	 * @param @param context
	 * @param @return 设定文件
	 * @return DisplayMetrics 返回类型
	 * @throws
	 */
	public static DisplayMetrics getDisplayMetrics(Context pContext) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) pContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		return displayMetrics;
	}

	/**
	 * 获取设备唯一编码
	 * 规则: android-ANDROID_ID-IMEI-IMSI
	 * 如 android-b63f657eab2edea-354316035235342-460006303765728
	 * IMEI为null时取ANDROID_ID, IMSI为null时取IMEI
	 * @param context
	 * @return
	 */
	public static String getDeviceId(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String androidId = System.getString(context.getContentResolver(), Secure.ANDROID_ID);
		String imei = tm.getDeviceId();
		String imsi = tm.getSubscriberId();
		imei = imei == null ? androidId : imei;
		imsi = imsi == null ? imei : imsi;
		StringBuffer sb = new StringBuffer(128);
		sb.append("android-");
		sb.append(androidId);
		sb.append("-");
		sb.append(imei);
		sb.append("-");
		sb.append(imsi);
		return sb.toString();
	}

	public static int getChannelId(Context context) {
		try {
			return context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData.getInt("ICD_CHANNEL");
		} catch (NameNotFoundException e) {
			ApiClient.uploadErrorLog(e.getMessage());
			return 0;
		}

	}

	/**
	 * 手机是否支持google map add-on
	 * @return
	 */
	public static final boolean isGoogleMapAvailable() {
		try {
			Class.forName("com.google.android.maps.MapActivity");
			return true;
		} catch (Exception e) {
			ApiClient.uploadErrorLog(e.getMessage());
			return false;
		}
	}

	// /**
	// * 显示分享对话框
	// * @param context
	// * @param messageId 分享的正文内容模板ID
	// * @param values 分享的征文内容数据
	// */
	// public static final void showShareDialog(Context context, int messageId, String... values) {
	// Intent intent=new Intent(Intent.ACTION_SEND);
	// intent.setType("text/plain");
	// intent.putExtra(Intent.EXTRA_SUBJECT,
	// context.getResources().getString(R.string.share_title));
	// intent.putExtra(Intent.EXTRA_TEXT,
	// StringUtil.format(context, messageId, values));
	// context.startActivity(Intent.createChooser(intent,
	// context.getResources().getString(R.string.share_dialog_title)));
	// }
	//
	/**
	 * 显示拨号界面
	 * @param context
	 * @param phone
	 */
	public static final void showDial(Activity context, String phone) {
		try {
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel://" + phone));
			context.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			ApiClient.uploadErrorLog(e.getMessage());
			// ToastTools.show(context, R.string.toast_setting_dial_error);
		}
	}

	/**
	 * 跳转到电子市场
	 * @param context
	 */
	public static final void showMarket(Activity context) {
		try {
			Intent intent = new Intent("android.intent.action.VIEW");
			intent.setData(Uri.parse("market://details?id=" + context.getPackageName()));
			context.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			ApiClient.uploadErrorLog(e.getMessage());
			// ToastTools.show(context, R.string.toast_market_error);
		}
	}

	/**
	 * 跳转到地图
	 * @param context
	 * @param lat
	 * @param lng
	 * @param placename
	 */
	public static void showMap(Context context, float lat, float lng, String placename) {
		Uri mapUri = Uri.parse(new StringBuffer().append("geo:0,0?q=").append(lat).append(",").append(lng).append("(").append(placename.replace("(", "<").replace(")", ">")).append(")").toString());
		Intent intent = new Intent(Intent.ACTION_VIEW, mapUri);
		context.startActivity(intent);
	}

	public static String getVersion() {
		return "Android-" + Build.VERSION.RELEASE;
	}

	public static String getModel() {
		return Build.BRAND + " " + Build.MODEL;
	}

	/**
	 * dp -> px 最好不要用, 不要用在字体上, 某些机子如M9会出错的..
	 * @param contxt
	 * @param dp
	 * @return
	 */
	public static final int dp2px(Context contxt, int dp) {
		final float scale = contxt.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	public static final String getDpi(Activity act) {
		if (act == null) {
			return "hdp"; // 默认返回hdp
		}
		// 由于设备的规格未知数太多, 没有简单根据 xdpi 来返回.
		int width = AndroidUtil.getScreenWidth(act);
		if (width > 480) {
			return "udp";
		} else if (width < 480) {
			return "mdp";
		} else {
			return "hdp";
		}
	}

	/**
	 * 判断手机是否有GPS定位系统 如果没有会报java.lang.IllegalArgumentException: provider=gps
	 * @pocketState context
	 * @return
	 */
	public static boolean isGPSEnable(Context context) {
		LocationManager locationManager = (LocationManager) context.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	/**
	 * 获取屏幕宽度
	 * @return
	 */
	public static int getScreenWidth(Activity context) {
		DisplayMetrics displayMetrics = getDisplayMetrics(context);
		return displayMetrics.widthPixels;
	}

	/**
	 * uos 实为 user-agent，包括厂商，机器型号，操作系统号
	 */
	public static String getUserAgent() {
		return Build.BRAND + " " + Build.MODEL + " " + Build.DISPLAY;
	}



	/**
	 * @Title: getApplicationName
	 * @Description: 获取当前应用名称
	 * @param @param pContext
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String getApplicationName(Context pContext) {
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = pContext.getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(pContext.getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			ApiClient.uploadErrorLog(e.getMessage());
			applicationInfo = null;
		}
		String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
		return applicationName;
	}

	/**
	 * @Title: getVersion
	 * @Description: 获取当前应用版本
	 * @param context
	 * @return String 返回类型
	 * @throws
	 */
	public static String getAppVersion(Context context) {
		// 1.获取当前应用程序的包的apk信息
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo packInfo = pm.getPackageInfo(context.getPackageName(), 0);
			return packInfo.versionName;
		} catch (NameNotFoundException e) {
			ApiClient.uploadErrorLog(e.getMessage());
			e.printStackTrace();
			// 不可能发生
			return "";
		}
	}

	/**
	 * 更新apk
	 * @param downloadManager
	 * @param url
	 * @param filename
	 * @param apkname
	 * @param title
	 * @param description
	 * @return
	 */
	// public static long DownloadApk(DownloadManager downloadManager, String url, String filename,
	// String apkname, String title, String description, Context context) {
	//
	// File folder = new File(filename);
	// if (!folder.exists() || !folder.isDirectory()) {
	// folder.mkdirs();
	// }
	// DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
	// request.setDestinationInExternalPublicDir(filename, apkname);
	// // request.setDestinationInExternalFilesDir(context, "icmrgin", apkname);
	// request.setTitle(title);
	// request.setShowRunningNotification(true);
	// request.setVisibleInDownloadsUi(true);
	// // request.allowScanningByMediaScanner();
	// request.setDescription(description);
	// // request.setNotificationVisibility(DownloadManager.Request.);
	// long id = downloadManager.enqueue(request);
	// return id;
	// }

	/**
	 * 获取当前时间前一个月时间
	 * @return
	 */
	public static String getLasMouthTime() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
		return df.format(calendar.getTime());
	}

	/**
	 * 获取系统当前时间
	 * @return
	 */
	public static String getCurrentTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS",Locale.CHINA);
		return df.format(new Date());
	}

	public static void downFile(final String url) {
		new Thread() {

			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						File file = new File(Environment.getExternalStorageDirectory(), "xubooooo");
						fileOutputStream = new FileOutputStream(file);
						byte[] buf = new byte[1024];
						int ch = -1;
						int count = 0;
						while ((ch = is.read(buf)) != -1) {
							fileOutputStream.write(buf, 0, ch);
							count += ch;
							if (length > 0) {
							}
						}
					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					// down();
				} catch (ClientProtocolException e) {
					ApiClient.uploadErrorLog(e.getMessage());
					e.printStackTrace();
				} catch (IOException e) {
					ApiClient.uploadErrorLog(e.getMessage());
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * 判断应用是否在前台运行
	 * @param context
	 * @return
	 */
	public static boolean isRunningForeground(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		String currentPackageName = cn.getPackageName();
		if (!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(context.getPackageName())) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断字符串有多个汉字
	 * @param str
	 * @return
	 */
	public static int TextNumber(String str){
		int count=0;
		String regEx = "[\\u4e00-\\u9fa5]";
		Pattern p = Pattern.compile(regEx);
		for(int i = 0; i < str.length(); i++){
			Matcher m = p.matcher(str.charAt(i)+"");
			if(m.matches()){
				count++;
			}
		}
		
		
		return count;
	}
	
	/**
	 * 非汉字的个数
	 * @param str
	 * @return
	 */
	public static int notTextNumber(String str){
		return str.length() - TextNumber(str);
	}
	
}
