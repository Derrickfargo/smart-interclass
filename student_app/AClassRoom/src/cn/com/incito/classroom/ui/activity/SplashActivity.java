package cn.com.incito.classroom.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import cn.com.incito.classroom.Canvas1Activity;
import cn.com.incito.classroom.Canvas2Activity;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.socket.core.CoreSocket;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.socket.core.MessageInfo;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;

import com.alibaba.fastjson.JSONObject;
import com.popoy.common.TAActivity;
import com.popoy.common.TAApplication;
import com.popoy.tookit.cache.TAFileCache;

/**
 * @author 白猫
 * @version V1.0
 * @Title: 用户其启动界面
 * @Package com.cat.activity
 * @Description: 用户其启动界面时候的一个启动页面完成一些初始化工作
 * @date 2013-5-6
 */
public class SplashActivity extends TAActivity {
    private static final String SYSTEMCACHE = "adream";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = View.inflate(this, R.layout.splash, null);
        setContentView(view);
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
    protected void onPreOnCreate(Bundle savedInstanceState) {
        super.onPreOnCreate(savedInstanceState);
        TAApplication application = (TAApplication) getApplication();
        // 配置系统的缓存,可以选择性的配置
        TAFileCache.TACacheParams cacheParams = new TAFileCache.TACacheParams(this, SYSTEMCACHE);
        TAFileCache fileCache = new TAFileCache(cacheParams);
        application.setFileCache(fileCache);
        // 注册activity
        getTAApplication().registerActivity(R.string.cavas1activity,
                Canvas1Activity.class);
        getTAApplication().registerActivity(R.string.cavas2activity,
                Canvas2Activity.class);
        getTAApplication().registerActivity(R.string.waitingactivity,
                WaitingActivity.class);
        getTAApplication().registerActivity(R.string.binddeskactivity,
                BindDeskActivity.class);

    }

    @Override
    protected void onAfterOnCreate(Bundle savedInstanceState) {
        super.onAfterOnCreate(savedInstanceState);

    }

    private void startMain() {
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
            }

        });
    }

    void init1() {
//        CmdClient.getInstance().start(this, Constant.IP, Constant.PORT);
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
                        doActivity(R.string.waitingactivity);
                    } else {
                        doActivity(R.string.binddeskactivity);
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
}
