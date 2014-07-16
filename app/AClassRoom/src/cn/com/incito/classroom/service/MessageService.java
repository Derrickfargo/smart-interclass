package cn.com.incito.classroom.service;

import cn.com.incito.classroom.ui.activity.Home;
import cn.com.incito.classroom.ui.activity.ImageActivity;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;

public class MessageService extends Service {

    private MessageHandler client;
    private static MessageService instance;

    public static MessageService getInstance() {
        return instance;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        System.out.println("onBind");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        String ip = bundle.getString("IP");
        int port = Integer.valueOf(bundle.getString("PORT"));
        System.out.println("IP:" + ip);
        System.out.println("PORT:" + port);
        client = new MessageHandler(ip, port, this);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        HandlerThread thread = new HandlerThread("Stop");
        thread.start();
        Handler handler = new Handler(thread.getLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
                client.disconnected();
                System.out.println("Disconnected");
            }
        });
    }

    public void handleMessage(String message) {
        System.out.println(message);
        int commandPosition = message.indexOf(" ");
        String command = message.substring(0, commandPosition);
        String parget = message.substring(commandPosition + 1);
        if (command.toLowerCase().equals("open")) {
            Uri uri = Uri.parse(parget);
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(it);
        }
    }

    public void showPicture(byte[] data) {
        ImageActivity instance = ImageActivity.getInstance();
        if (instance != null) {
            instance.finish();
        }
        Intent it = new Intent(this, ImageActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        it.putExtra("data", data);
        startActivity(it);
    }

    public void lock() {
        ImageActivity instance = ImageActivity.getInstance();
        if (instance == null) {
            Intent it = new Intent(this, Home.class);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(it);
        }
    }

    public void unlock() {
        Home instance = Home.getInstance();
        if (instance != null) {
            instance.finish();
        }
    }

    public void sendPicture(byte[] data) {
        client.sendPicture(data);
    }
}
