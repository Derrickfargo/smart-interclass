package cn.com.incito.common.utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.RemoteException;
import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.ui.activity.SplashActivity;
import cn.com.incito.classroom.utils.FileUtils;

/**
 * 静默自动安装工具类
 * @author hm
 */
public class AutoInstallApkUtil {
	
	/**
	 * 自动静默安装apk
	 * @param context
	 * @param packageName  包名
	 * @param appName      应用名称
	 * @param file         安装文件
	 */
	public static void installApkDefaul(Context context){
		
		File file = hasApkUpatePackage(context);
		/**
		 * 有文件获取该文件的版本号与当前运行的apk的版本号进行对比
		 * 只有文件的版本号大于正在运行的版本号的时候才会静默安装
		 * 否则删除该文件
		 */
		if(null != file){
			int willInstallVersionCode = getWillInstallApkPackageInfo(context, file).versionCode;
			int currentVersionCode = AndroidUtil.currentVersionCode(context);
			LogUtil.d("安装包版本号:" + willInstallVersionCode + ":运行程序的版本号:" + currentVersionCode);
			if(willInstallVersionCode > currentVersionCode){
				/**
				* 调用静默安装方法
				*/
				Activity activity = AppManager.getAppManager().currentActivity();
				if(SplashActivity.class.equals(activity.getClass())){
					SplashActivity splashActivity = (SplashActivity) activity;
					splashActivity.showNotice();
				}
			 }else{
				boolean flag = file.delete();
				LogUtil.d("有安装包但是版本低于或者等于当前运行的版本进行删除,删除结果:" + flag);
			}
		}
    }
	
	/**
	 * 判断本地是否有apk安装包
	 * @return 有返回安装包的路径
	 * 	没有返回空
	 */
	private static File hasApkUpatePackage(Context context){
		String fileName = MyApplication.getInstance().getSharedPreferences().getString("fileName", "AclassRoom.apk");
		File localFile = new File(FileUtils.getSDCardPath() + "/" + fileName);
		if(!localFile.exists()){
			return null;
		}
		return localFile;
	}

	/**
	 * 静默安装方法
	* @author 名字 
	* @date 2015年2月3日 上午10:55:42 
	* @Title: silentInstallation 
	* @Description:  
	* @param context
	* @param apkPath
	* @return void    返回类型 
	* @throws
	 */
	public static void silentInstallation(Context context) {
		File file = hasApkUpatePackage(context);
        Uri mPackageURI = Uri.fromFile(file);   
        int installFlags = 0;
        PackageManager pm = context.getPackageManager();  
        PackageInfo info = getWillInstallApkPackageInfo(context, file); 
        if(info != null){   
            try {   
                PackageInfo pi = pm.getPackageInfo(info.packageName,PackageManager.GET_UNINSTALLED_PACKAGES);   
                if( pi != null){   
                    installFlags |= PackageManager.INSTALL_REPLACE_EXISTING;    
                }   
            } catch (NameNotFoundException e) {
            	LogUtil.e("获取包名出现异常",e.getCause());
            }   
            IPackageInstallObserver observer = new MyPakcageInstallObserver();   
            pm.installPackage(mPackageURI, observer, installFlags, context.getPackageName());   
        }
	}
	
	/**
	 * 获取将要安装的apk的安装包信息
	* @author hm
	* @date 2015年2月3日 下午5:15:25 
	* @Title: getWillInstallApkPackageInfo 
	* @Description:  
	* @param file
	* @return void    返回类型 
	* @throws
	 */
	private static PackageInfo getWillInstallApkPackageInfo(Context context,File file){
		PackageManager pm = context.getPackageManager();
		return pm.getPackageArchiveInfo(file.getPath(), PackageManager.GET_ACTIVITIES); 
	}
	
	public static int getAapPackageVersioncode(Context context){
		File file = hasApkUpatePackage(context);
		if(file != null){
			return getWillInstallApkPackageInfo(context, file).versionCode;
		}
		return 0;
	}
	
	/**
	* @author hm
	* @date 2015年2月3日 上午10:37:42 
	* @Title: execRootCmdSilent 
	* @Description:  如果pad已经root 可以通过该方法进行安装
	* @param cmd
	* @return
	* @return int    返回类型 
	* @throws
	 */
	public static int execRootCmdSilent(String cmd) {
	       int result = -1;
	       DataOutputStream dos = null;
	       try {
	           Process p = Runtime.getRuntime().exec("su");
	           dos = new DataOutputStream(p.getOutputStream());
	           dos.writeBytes(cmd + "\n");
	           dos.flush();
	           dos.writeBytes("exit\n");
	           dos.flush();
	           p.waitFor();
	           result = p.exitValue();
	       } catch (Exception e) {
	           LogUtil.e("获取包名出现异常",e.getCause());
	       } finally {
	           if (dos != null) {
	               try {
	                   dos.close();
	               } catch (IOException e) {
	                   e.printStackTrace();
	               }
	           }
	       }
	       return result;
	   }
	/**
	 * 安装观察者类
	 * 由于系统原因安装成功不会回调
	 * 只有在安装失败时才会发生回调
	* @ClassName: MyPakcageInstallObserver 
	* @Description: 
	* @author hm
	* @date 2015年2月3日 上午11:02:14 
	*
	*/
	private static class MyPakcageInstallObserver extends IPackageInstallObserver.Stub{
		 @Override
		 public void packageInstalled(String packageName, int returnCode) throws RemoteException {
		     LogUtil.d("安装结果过:" + returnCode);
		 }
	}
}
