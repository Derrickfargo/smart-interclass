package cn.com.incito.socket.handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.SharedPreferences.Editor;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.ui.activity.SplashActivity;
import cn.com.incito.classroom.utils.FTPUtils;
import cn.com.incito.common.utils.AndroidUtil;
import cn.com.incito.common.utils.AutoInstallApkUtil;
import cn.com.incito.common.utils.LogUtil;
import cn.com.incito.socket.core.MessageHandler;

/**
 * 下载apk更新包
 * 
 * @author hm
 *
 */
public class DownLoadApkHandler extends MessageHandler {
	
	/**
	 * 专门用于做升级的线程池
	 */
	private ExecutorService updateService = Executors.newSingleThreadExecutor();

	@Override
	protected void handleMessage() {
		
		/**
		 * 服务器返回的版本的code
		 */
		int serverVersionCode = data.getIntValue("serverVersionCode");
		int packageVersionCode = AutoInstallApkUtil.getAapPackageVersioncode(activity);
		int currentVersionCode = AndroidUtil.currentVersionCode(MyApplication.getInstance().getApplicationContext());
		/**
		 * 更新包的名字
		 */
		String fileName = data.getString("fileName");
		
		LogUtil.d("服务器返回的版本:" + serverVersionCode + ":文件名称:" + fileName + ":正在运行的版本:" + currentVersionCode);

		/**
		 * 本地目录下有没有安装包 有的话比较该安装包的版本与服务器返回的版本 如果服务器返回的版本大于本地的版本则进行重新下载 
		 * 否则不做下载
		 */
		
		if (serverVersionCode > currentVersionCode) {
			if(serverVersionCode == packageVersionCode){
				//另外一种情况 就是pc端连续发两次最新版本而第一次我已经把版本下载好
				if(SplashActivity.class.equals(activity.getClass())){
					AutoInstallApkUtil.installApkDefaul(activity);
				}
			}
			if(serverVersionCode > packageVersionCode){
				LogUtil.d("apk有更新");
				updateService.execute(new updateRunable(fileName));
			}
		}else{
			reconnectNew();
		}
	}


	
	/**
	 * 升级任务类
	* @ClassName: updateRunable 
	* @Description: 封装ftp下载升级包
	* @author hm
	* @date 2015年2月1日 下午2:08:17 
	*/
	private class updateRunable implements Runnable{
		
		private String fileName;
		
		public  updateRunable(String fileName) {
			this.fileName = fileName;
		}
		
		/**
		 * 下载apkg更新包
		 * @author hm
		 * @date 2015年2月9日 上午10:58:54 
		 * @return void
		 */
		private boolean downApk() {
			FTPUtils.getInstance();
			if(SplashActivity.class.equals(activity.getClass())){
				SplashActivity splashActivity = (SplashActivity) activity;
				splashActivity.showUpdate();
			}
			if (FTPUtils.downLoadApk(activity, fileName)) {
				LogUtil.d("apk更新包下载成功");
				//保存服务器返回的更新的版本值与文件名称
				Editor edit = MyApplication.getInstance().getSharedPreferences().edit();
				edit.putString("fileName", fileName);
				edit.commit();
				return true;
			}
			return false;
		}

		@Override
		public void run() {
			boolean flag = downApk();
			if(flag){
				if(SplashActivity.class.equals(activity.getClass())){
					AutoInstallApkUtil.installApkDefaul(activity);
				}
			}else{
				updateService.execute(new updateRunable(fileName));
			}
		}
	}
}
