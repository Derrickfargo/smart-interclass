package cn.com.incito.classroom.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.TextView;

import cn.com.incito.classroom.Canvas1Activity;
import cn.com.incito.classroom.Canvas2Activity;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.service.SocketService;
import cn.com.incito.common.utils.ToastHelper;
import cn.com.incito.socket.core.CoreSocket;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.socket.core.MessageInfo;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * @author 白猫
 * @version V1.0
 * @Title: 用户其启动界面
 * @Package com.cat.activity
 * @Description: 用户其启动界面时候的一个启动页面完成一些初始化工作
 * @date 2013-5-6
 */
public class SplashActivity extends BaseActivity {
    private TextView tv_loading_msg;
    private MsgReceiver msgReceiver;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        msgReceiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SocketService.NETWORK_RECEIVER);
        registerReceiver(msgReceiver, intentFilter);
        final View view = View.inflate(this, R.layout.splash, null);
        setContentView(view);
        tv_loading_msg = (TextView) view.findViewById(R.id.tv_loading_msg);

//		渐变展示启动屏
        AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
        aa.setDuration(2000);
        view.startAnimation(aa);
        aa.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                startMain();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        //注销广播
        unregisterReceiver(msgReceiver);
        super.onDestroy();
    }

    /**
     * 广播接收器
     *
     * @author len
     */
    public class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == SocketService.NETWORK_RECEIVER) {
                //拿到进度，更新UI
                String exception = intent.getStringExtra("exception");
                tv_loading_msg.setText(R.string.loading_network_disconnect);
            }

        }

    }

    private void startMain() {
        tv_loading_msg.setText(R.string.loading_msg);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("imei", MyApplication.deviceId);
        MessagePacking messagePacking = new MessagePacking(MessageInfo.MESSAGE_DEVICE_HAS_BIND);
        messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(jsonObject.toJSONString()));
        CoreSocket.getInstance().sendMessage(messagePacking, new MessageHandler() {
            @Override
            public void handleMessage(Bundle bundle) {
                Message message = new Message();
                message.what = 1;
                message.setData(bundle);
                mHandler.sendMessage(message);
                tv_loading_msg.setText(R.string.loading_complete_msg);
            }

        });
    }

    void init1() {
//        CmdClient.getInstance().start(this, Constants.IP, Constants.PORT);
//
////        SocketMinaClient socketMinaClient = SocketMinaClient.getInstance();
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("imei", MyApplication.deviceId);

//        MessagePacking messagePacking = new MessagePacking(MessageInfo.MESSAGE_GROUP_LIST);
//        messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(jsonObject.toJSONString()));
////        CoreSocket.getInstance().sendMessage(messagePacking);
//        socketMinaClient.sendMessage(messagePacking);
//        CmdClient.getInstance().sendMessage(jsonObject.toJSONString());
//        socketMinaClient.getSocketConnector().dispose();
    }


    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    JSONObject jsonObject = (JSONObject) msg.getData().getSerializable("data");
                    if (!"0".equals(jsonObject.getString("code"))) {
                        return;
                    } else if (jsonObject.getJSONObject("data").getBoolean("isbind")) {
                        startActivity(new Intent(SplashActivity.this,
                                WaitingActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(SplashActivity.this,
                                BindDeskActivity.class));
                        finish();
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        AppManager.getAppManager().AppExit(this);
    }


}
