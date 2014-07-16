package cn.com.incito.classroom;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.popoy.annotation.TAInjectView;
import com.popoy.tookit.cache.TAFileCache;
import com.popoy.tookit.cache.TAFileCache.TACacheParams;

import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.ui.activity.ClientActivity;
import cn.com.incito.classroom.ui.activity.WifiListActivity;
import cn.com.incito.classroom.ui.dialog.SelectPicPopupWindow;

/**
 * 用户其启动界面
 * 用户其启动界面时候的一个启动页面完成一些初始化工作
 *
 * @author liubo
 * @version V1.0
 */
public class MainActivity extends BaseActivity {
    public static final String SYSTEMCACHE = "MainActivity";
    //自定义的弹出框类
    SelectPicPopupWindow menuWindow;
    @TAInjectView(id=R.id.rl_layout1)
    RelativeLayout rl_layout1;
    @TAInjectView(id=R.id.rl_layout2)
    RelativeLayout rl_layout2;
//    private Handler mHandler;
    private int detchTime = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mHandler = new Handler();
        // TANetworkStateReceiver.registerNetworkStateReceiver(this);
        final View view = View.inflate(this, R.layout.main, null);
        setContentView(view);
//        showLoginWindow();
        rl_layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doActivity(R.string.wifilistactivity);
            }
        });
        rl_layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doActivity(R.string.clientactivity);
            }
        });
    }

    @Override
    protected void onPreOnCreate(Bundle savedInstanceState) {
        super.onPreOnCreate(savedInstanceState);
        // 配置系统的缓存,可以选择性的配置
        final TAFileCache fileCache = new TAFileCache(new TACacheParams(this,
                SYSTEMCACHE));
        getTAApplication().setFileCache(fileCache);
        // 注册activity
        getTAApplication().registerActivity(R.string.cavas1activity,
                Canvas1Activity.class);
        getTAApplication().registerActivity(R.string.cavas2activity,
                Canvas2Activity.class);
        getTAApplication().registerActivity(R.string.clientactivity,
                ClientActivity.class);
        getTAApplication().registerActivity(R.string.wifilistactivity,
                WifiListActivity.class);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

//    void showLoginWindow() {
//        menuWindow = new SelectPicPopupWindow(this);
//        menuWindow.setBackgroundDrawable(null);
//        menuWindow.setOutsideTouchable(false);
////        menuWindow.startScanning();
//        /*****************以下代码用来循环检测activity是否初始化完毕***************/
//        Runnable showPopWindowRunnable = new Runnable() {
//
//            @Override
//            public void run() {
//                // 得到activity中的根元素
//                View view = findViewById(R.id.rlayout);
//                // 如何根元素的width和height大于0说明activity已经初始化完毕
//                if (view != null && view.getWidth() > 0 && view.getHeight() > 0) {
//                    // 显示popwindow
//                    //显示窗口
//                    menuWindow.showAtLocation(view, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
//                    // 停止检测
//                    mHandler.removeCallbacks(this);
//                } else {
//                    // 如果activity没有初始化完毕则等待5毫秒再次检测
//                    mHandler.postDelayed(this, detchTime);
//                }
//            }
//        };
//        // 开始检测
//        mHandler.post(showPopWindowRunnable);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        menuWindow.stopScanning();
    }


}
