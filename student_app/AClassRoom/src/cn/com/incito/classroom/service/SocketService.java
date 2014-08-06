package cn.com.incito.classroom.service;

import cn.com.incito.classroom.R;
import cn.com.incito.common.utils.ToastHelper;
import cn.com.incito.socket.core.CoreSocket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class SocketService extends Service {

    public static final String NETWORK_RECEIVER = "cn.com.incito.network.RECEIVER";
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
//		super.onCreate();
//        CoreSocket.getInstance().start();
        Thread.setDefaultUncaughtExceptionHandler(
                new MyUncaughtExceptionHandler());
        ExecutorService exec = Executors
                .newCachedThreadPool(new HandlerThreadFactory());
        exec.execute(CoreSocket.getInstance());
//        SocketMinaClient.getInstance().start();
    }

    class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            System.out.println("caught " + e);
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
            System.out.println("eh=" + t.getUncaughtExceptionHandler());
            return t;
        }
    }

}
