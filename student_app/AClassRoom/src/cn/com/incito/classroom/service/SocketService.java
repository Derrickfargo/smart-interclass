package cn.com.incito.classroom.service;

import cn.com.incito.classroom.R;
import cn.com.incito.common.utils.ToastHelper;
import cn.com.incito.socket.core.CoreSocket;
import cn.com.incito.wisdom.sdk.log.WLog;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
/**
 * socket服务Service，保持通信连接
 * Created by liushiping on 2014/7/28.
 */
public class SocketService extends Service {

    public static final String NETWORK_RECEIVER = "cn.com.incito.network.RECEIVER";
    ExecutorService exec;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(
                new MyUncaughtExceptionHandler());
        exec = Executors
                .newCachedThreadPool(new HandlerThreadFactory());
        exec.execute(CoreSocket.getInstance());
        WLog.i(SocketService.class, "socket started");
    }

    class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            WLog.e(SocketService.class, "network unreachble exception:" + e);
            Intent intent = new Intent(NETWORK_RECEIVER);
            intent.putExtra("exception", e.getMessage());
            sendBroadcast(intent);

        }
    }

    class HandlerThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
            return t;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CoreSocket.getInstance().stopConnection();
        WLog.i(SocketService.class, "socket disconnected");
    }
}
