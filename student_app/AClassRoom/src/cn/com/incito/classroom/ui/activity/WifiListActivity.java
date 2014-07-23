package cn.com.incito.classroom.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.util.Log;
import com.popoy.annotation.TAInjectView;
import com.popoy.common.TAActivity;
import com.popoy.common.TAApplication;
import com.popoy.common.core.AsyncTask;
import com.popoy.tookit.cache.TAFileCache;
import com.popoy.widget.pulltorefresh.AbMultiColumnBaseAbsListView;

import java.util.ArrayList;
import java.util.List;

import cn.com.incito.classroom.Canvas1Activity;
import cn.com.incito.classroom.Canvas2Activity;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.adapter.WifiListAdapter;
import cn.com.incito.classroom.adapter.WifiListAdapter1;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.ui.dialog.LoadingDialog;
import cn.com.incito.classroom.ui.dialog.SelectPicPopupWindow;
import cn.com.incito.classroom.ui.dialog.WifiConfigDialog;
import cn.com.incito.classroom.ui.widget.AbAnalogClock;
import cn.com.incito.classroom.ui.widget.caroursel.CarouselView;
import cn.com.incito.classroom.ui.widget.caroursel.CarouselViewAdapter;
import cn.com.incito.classroom.ui.widget.circlemenu.CircleImageView;
import cn.com.incito.classroom.ui.widget.circlemenu.CircleLayout;
import cn.com.incito.classroom.utils.WifiAdmin;
import cn.com.incito.classroom.ui.widget.caroursel.CarouselAdapter;
import cn.com.incito.socket.core.CoreSocket;

/**
 * 用户其启动界面
 * 用户其启动界面时候的一个启动页面完成一些初始化工作
 *
 * @author liubo
 * @version V1.0
 */
public class WifiListActivity extends BaseActivity {
    public static final String SYSTEMCACHE = "WifiListActivity";
    //自定义的弹出框类
//    @TAInjectView(id = R.id.gv_wifi_list)
//    GridView gv_wifi_list;
    @TAInjectView(id = R.id.carousel)
    CircleLayout carousel;
    @TAInjectView(id = R.id.contentLayout)
    LinearLayout contentLayout;
    @TAInjectView(id = R.id.rlayout)
    RelativeLayout rlayout;
    private WifiAdmin wifiAdmin;
    CarouselViewAdapter adapter;
    int selectedPos = -1;
    List<ScanResult> wifilist;
    List<View> mViews;
    int[] location = new int[2];

    @Override
    protected void onAfterOnCreate(Bundle savedInstanceState) {
        super.onAfterOnCreate(savedInstanceState);
//        CircleLayout circleMenu = (CircleLayout)findViewById(R.id.carousel);

        //不支持的动态添加adapter.notifyDataSetChanged()增强滑动的流畅
//        adapter = new CarouselViewAdapter(WifiListActivity.this, mViews, false);
//        ImageView imageView = new ImageView(this);
//        imageView.setImageDrawable(getResources().getDrawable(R.drawable.bg_common_toast));

        rlayout.getLocationOnScreen(location);
        carousel.setOnItemClickListener(new CircleLayout.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long id, String name) {
                selectedPos = position;
                if (wifiAdmin.targetWifiIsConfig(wifilist.get(position).SSID)) {
//                    adapter.changeWifiState(selectedPos, WifiListAdapter.ConnectState.CONNECTING);
//                    adapter.notifyDataSetChanged();
//                    LoadingDialog.hide();
                    LoadingDialog.show(WifiListActivity.this, R.string.waiting_access_wifi);
                    wifiAdmin.connectionTargetWifiBySSID(wifilist.get(position).SSID);
                } else {
//                    WifiConfigDialog dialog = WifiConfigDialog.newInstance(new WifiConfigDialog.WifiConfigCallBack() {
//                        @Override
//                        public void connectWifi(String ssid, String password) {
////                        adapter.changeWifiState(selectedPos, WifiListAdapter.ConnectState.CONNECTING);
////                        adapter.notifyDataSetChanged();
//                            LoadingDialog.hide();
//                            LoadingDialog.show(WifiListActivity.this, R.string.waiting_access_wifi);
//                            wifiAdmin.addNetwork(ssid, password, WifiAdmin.TYPE_WPA);
//                        }
//                    }, wifilist.get(position).SSID);
//                    dialog.show(getSupportFragmentManager().beginTransaction(), "dialog");
                    SelectPicPopupWindow popupWindow = new SelectPicPopupWindow(WifiListActivity.this, wifilist.get(position).SSID, new SelectPicPopupWindow.WifiConfigCallBack() {
                        @Override
                        public void connectWifi(String ssid, String password) {
                            LoadingDialog.hide();
                            LoadingDialog.show(WifiListActivity.this, R.string.waiting_access_wifi);
                            wifiAdmin.addNetwork(ssid, password, WifiAdmin.TYPE_WPA);
                        }
                    });
                    popupWindow.setFocusable(true);
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.setBackgroundDrawable(new BitmapDrawable());
                    popupWindow.showAtLocation(rlayout,Gravity.NO_GRAVITY, location[0]-popupWindow.getWidth(), location[1]);
                }
            }
        });
        Drawable dial = this.getResources().getDrawable(R.drawable.clock_dial);
        Drawable hourHand = this.getResources().getDrawable(R.drawable.clock_hour);
        Drawable minuteHand = this.getResources().getDrawable(R.drawable.clock_minute);
        Drawable secondHand = this.getResources().getDrawable(R.drawable.clock_second);
        AbAnalogClock clock = new AbAnalogClock(this, dial, hourHand, minuteHand, secondHand);


        contentLayout.addView(clock);
        wifiAdmin = new WifiAdmin(WifiListActivity.this) {
            @Override
            public Intent myRegisterReceiver(BroadcastReceiver receiver, IntentFilter filter) {
                getApplicationContext().registerReceiver(receiver, filter);
                return null;
            }

            @Override
            public void myUnregisterReceiver(BroadcastReceiver receiver) {
                try {
                    getApplicationContext().unregisterReceiver(receiver);
                } catch (IllegalArgumentException e) {
                    if (e.getMessage().contains("Receiver not registered")) {
                        // Ignore this exception. This is exactly what is desired
                    } else {
                        // unexpected, re-throw
                        throw e;
                    }
                }

            }

            @Override
            public void onNotifyWifiConnected() {
//                adapter.changeWifiState(selectedPos, WifiListAdapter.ConnectState.CONNECTED);
//                adapter.notifyDataSetChanged();
                LoadingDialog.hide();
                selectedPos = -1;
                login();
                doActivity(R.string.cavas1activity);
            }

            @Override
            public void onNotifyWifiConnectFailed() {
                Log.i("test");
                LoadingDialog.hide();
//                adapter.changeWifiState(selectedPos, WifiListAdapter.ConnectState.CONNECT_FAIL);
//                adapter.notifyDataSetChanged();
            }
        };

        startScanning();
        wifilist = wifiAdmin.getWifiList();
        List<CircleImageView> lists = new ArrayList<CircleImageView>();
        for (int i = 0; i < (wifilist.size() <= 9 ? wifilist.size() : 9); i++) {
            CircleImageView circleImageView = new CircleImageView(WifiListActivity.this);
            lists.add(circleImageView);
        }

        carousel.setChild(lists, wifilist);
    }
    AsyncTask task = new AsyncTask() {
        @Override
        protected Object doInBackground(Object[] params) {

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    };
    public void startScanning() {
//        getApplicationContext().registerReceiver(mReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        if (wifiAdmin.checkState() == WifiManager.WIFI_STATE_DISABLED || wifiAdmin.checkState() == WifiManager.WIFI_STATE_DISABLING) {
            wifiAdmin.openWifi();
        }
        wifiAdmin.startScan();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        getApplicationContext().unregisterReceiver(mReceiver);
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
