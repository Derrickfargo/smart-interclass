package cn.com.incito.classroom.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.activeandroid.util.Log;
import com.popoy.annotation.TAInjectView;
import com.popoy.common.core.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import cn.com.incito.classroom.R;
import cn.com.incito.classroom.adapter.GroupNumAdapter;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.ui.dialog.LoadingDialog;
import cn.com.incito.classroom.ui.dialog.SelectPicPopupWindow;
import cn.com.incito.classroom.ui.widget.AbAnalogClock;
import cn.com.incito.classroom.ui.widget.caroursel.CarouselViewAdapter;
import cn.com.incito.classroom.ui.widget.circlemenu.CircleImageView;
import cn.com.incito.classroom.ui.widget.circlemenu.CircleLayout;
import cn.com.incito.classroom.utils.WifiAdmin;
import cn.com.incito.classroom.vo.GroupNumberListRes2;
import cn.com.incito.classroom.vo.RegisterInfo;
import cn.com.incito.socket.core.CoreSocket;
import cn.com.incito.socket.core.SocketMinaClient;

/**
 * 用户其启动界面
 * 用户其启动界面时候的一个启动页面完成一些初始化工作
 *
 * @author liubo
 * @version V1.0
 */
public class WaitingActivity extends BaseActivity {
    public static final String TAG = "WaitingActivity";
    //自定义的弹出框类
    @TAInjectView(id = R.id.btn_join)
    ImageButton btn_join;
    @TAInjectView(id = R.id.gv_group_member)
    GridView gv_group_member;
    List<GroupNumberListRes2> list;

    @Override
    protected void onAfterOnCreate(Bundle savedInstanceState) {
        super.onAfterOnCreate(savedInstanceState);
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        list = new ArrayList<GroupNumberListRes2>();
        GroupNumberListRes2 groupNumberListRes = new GroupNumberListRes2();
        groupNumberListRes.setMembergender("1");
        groupNumberListRes.setMembername("lisan");
        groupNumberListRes.setMembernumber("111");
        list.add(groupNumberListRes);
        gv_group_member.setAdapter(new GroupNumAdapter(this, list));


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onBackPressed() {
        exitApp();
    }

    /**
     * 与后台服务建立连接，并实现登陆
     */
    private void login() {
//        CoreSocket.getInstance().startConnection();

    }
}
