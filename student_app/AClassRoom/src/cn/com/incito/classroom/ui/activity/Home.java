package cn.com.incito.classroom.ui.activity;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;

import cn.com.incito.classroom.R;
import cn.com.incito.classroom.utils.PowerManagerWakeLock;

public class Home extends Activity {
    public static Home instance;
    BroadcastReceiver screenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_SCREEN_ON)) {

            } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {//�����ܵ��ر���Ļ�Ĺ㲥  
                if (Home.getInstance() != null) {
                    //������Ļ���ѣ�����  
                    PowerManagerWakeLock.acquire(Home.this);
                }
                PowerManagerWakeLock.release();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        instance = this;

        // ����ϵͳ������  
        KeyguardManager manager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock lock = manager.newKeyguardLock("KeyguardLock");
        lock.disableKeyguard();

        // ע��һ��������Ļ�����͹رյĹ㲥  
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenReceiver, filter);
    }

    public void onDestroy() {
        super.onDestroy();
        KeyguardManager manager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock lock = manager.newKeyguardLock("KeyguardLock");
        lock.reenableKeyguard();
        PowerManagerWakeLock.release();
        unregisterReceiver(screenReceiver);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            return true;
        }
        if (KeyEvent.KEYCODE_HOME == keyCode) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static Home getInstance() {
        return instance;
    }

    @Override
    public void onAttachedToWindow() {
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
        super.onAttachedToWindow();
    }
}
