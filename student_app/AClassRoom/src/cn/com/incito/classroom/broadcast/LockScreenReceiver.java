package cn.com.incito.classroom.broadcast;

import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.wisdom.sdk.log.WLog;
import android.app.ExecRootCmd;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;

public class LockScreenReceiver extends BroadcastReceiver{
	private boolean isRegisterReceiver = false;
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if(MyApplication.getInstance().isLockScreen()){
			if (action.equals(Intent.ACTION_SCREEN_ON)) {
				ExecRootCmd execRootCmd = new ExecRootCmd();
				execRootCmd.powerkey();
				boolean ret = Settings.Global.putInt(context.getContentResolver(),"disable_powerkey", 1);//屏蔽电源按钮唤醒功能
				execRootCmd.powerkey();	
			} else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
				WLog.d(LockScreenReceiver.class, "屏幕加锁广播...");
			}
		}
		
	}
	public void registerScreenActionReceiver(Context mContext) {
		if (!isRegisterReceiver) {
			isRegisterReceiver = true;
			IntentFilter filter = new IntentFilter();
			filter.addAction(Intent.ACTION_SCREEN_OFF);
			filter.addAction(Intent.ACTION_SCREEN_ON);
			WLog.d(LockScreenReceiver.class, "注册屏幕解锁、加锁广播接收者...");
			mContext.registerReceiver(LockScreenReceiver.this, filter);
		}
	}

	public void unRegisterScreenActionReceiver(Context mContext) {
		if (isRegisterReceiver) {
			isRegisterReceiver = false;
			WLog.d(LockScreenReceiver.class, "注销屏幕解锁、加锁广播接收者...");
			mContext.unregisterReceiver(LockScreenReceiver.this);
		}
	}
}
