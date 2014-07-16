package cn.com.incito.classroom.service;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import cn.com.incito.classroom.ui.activity.Home;
import cn.com.incito.classroom.utils.PowerManagerWakeLock;

public class ScreenService extends Service {

    BroadcastReceiver screenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_SCREEN_ON)) {

            } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {//�����ܵ��ر���Ļ�Ĺ㲥  
                if (Home.getInstance() != null) {
                    //������Ļ���ѣ�����  
                    PowerManagerWakeLock.acquire(ScreenService.this);
                }
                PowerManagerWakeLock.acquire(ScreenService.this);
                Intent intent2 = new Intent(ScreenService.this, Home.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
                PowerManagerWakeLock.release();
            }
        }
    };

    public void onCreate() {
        // ����ϵͳ������  
        KeyguardManager manager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock lock = manager.newKeyguardLock("KeyguardLock");
        lock.disableKeyguard();  //disable ϵͳԭ��������

        // ע��һ��������Ļ�����͹رյĹ㲥  
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenReceiver, filter);
    }

    public void onDestroy() {
        PowerManagerWakeLock.release();
        unregisterReceiver(screenReceiver);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    ;
}
