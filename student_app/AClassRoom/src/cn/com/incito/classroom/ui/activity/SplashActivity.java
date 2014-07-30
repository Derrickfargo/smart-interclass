package cn.com.incito.classroom.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import cn.com.incito.classroom.Canvas1Activity;
import cn.com.incito.classroom.Canvas2Activity;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.vo.LoginResVo;
import cn.com.incito.socket.core.CoreSocket;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.socket.handler.SytemInitHandler1;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;

import com.alibaba.fastjson.JSONObject;
import com.popoy.common.TAApplication;
import com.popoy.tookit.helper.ToastHelper;

/**
 * @author 白猫
 * @version V1.0
 * @Title: 用户其启动界面
 * @Package com.cat.activity
 * @Description: 用户其启动界面时候的一个启动页面完成一些初始化工作
 * @date 2013-5-6
 */
public class SplashActivity extends BaseActivity {
    private static final String SYSTEMCACHE = "adream";
    private SytemInitHandler1 handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = View.inflate(this, R.layout.splash, null);
        setContentView(view);
        handler = new SytemInitHandler1();
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
//		TAApplication application = (TAApplication) getApplication();
//		// 配置系统的缓存,可以选择性的配置
//		TACacheParams cacheParams = new TACacheParams(this, SYSTEMCACHE);
//		TAFileCache fileCache = new TAFileCache(cacheParams);
//		application.setFileCache(fileCache);
        // 注册activity
        // 注册activity
//        getTAApplication().registerCommand(R.string.testmvccommand,
//                TestMVCCommand.class);
        getTAApplication().registerActivity(R.string.cavas1activity,
                Canvas1Activity.class);
        getTAApplication().registerActivity(R.string.cavas2activity,
                Canvas2Activity.class);
        getTAApplication().registerActivity(R.string.waitingactivity,
                WaitingActivity.class);
        getTAApplication().registerActivity(R.string.waitingactivity,
                WaitingActivity.class);

    }

    @Override
    protected void onAfterOnCreate(Bundle savedInstanceState) {
        super.onAfterOnCreate(savedInstanceState);

    }

    private void startMain() {
        init();
        doActivity(R.string.waitingactivity);
    }

    //    void init() {
////        SocketMinaClient socketMinaClient = new SocketMinaClient();
//        LoginReqVo loginReqVo = new LoginReqVo();
//        loginReqVo.setImei(MyApplication.deviceId);
//        loginReqVo.setName("liubo");
//        loginReqVo.setNumber("111");
//        loginReqVo.setSex("1");
//        String json = JSON.toJSONString(loginReqVo);
//
//        MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_GROUP_LIST);
//        messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(json));
//        CoreSocket.getInstance().sendMessage(messagePacking);
////        socketMinaClient.sendMessage(messagePacking);
////        socketMinaClient.getSocketConnector().dispose();
//    }
    void init() {
//        SocketMinaClient socketMinaClient = new SocketMinaClient();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("imei", MyApplication.deviceId);

        MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_GROUP_LIST);
        messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(jsonObject.toJSONString()));
        CoreSocket.getInstance().sendMessage(messagePacking);
//        socketMinaClient.sendMessage(messagePacking);
//        socketMinaClient.getSocketConnector().dispose();
    }

    public class SystemInitHandler extends MessageHandler {

        @Override
        protected void handleMessage() {
            System.out.println("收到获取分组回复消息:" + data);

            String code = data.getString("code");
            if ("0".equals(code)) {
                LoginResVo loginResVo = data.getObject("data", LoginResVo.class);
                ((MyApplication) TAApplication.getApplication()).setLoginResVo(loginResVo);
            } else {
                ToastHelper.showCustomToast(SplashActivity.this, "test");
            }
        }
    }
}