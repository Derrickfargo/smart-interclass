package cn.com.incito.classroom.ui.activity;

import com.popoy.common.TAActivity;
import com.popoy.common.TAApplication;
import com.popoy.tookit.cache.TAFileCache;
import com.popoy.tookit.cache.TAFileCache.TACacheParams;
import com.popoy.tookit.netstate.TANetworkStateReceiver;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import cn.com.incito.classroom.Canvas1Activity;
import cn.com.incito.classroom.Canvas2Activity;
import cn.com.incito.classroom.R;

/**
 * @Title: 用户其启动界面
 * @Package com.cat.activity
 * @Description: 用户其启动界面时候的一个启动页面完成一些初始化工作
 * @author 白猫
 * @date 2013-5-6
 * @version V1.0
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
//		TAApplication application = (TAApplication) getApplication();
//		// 配置系统的缓存,可以选择性的配置
//		TACacheParams cacheParams = new TACacheParams(this, SYSTEMCACHE);
//		TAFileCache fileCache = new TAFileCache(cacheParams);
//		application.setFileCache(fileCache);
		// 注册activity
        // 注册activity
//        getTAApplication().registerCommand(R.string.testmvccommand,
//                TestMVCCommand.class);
        getTAApplication().registerActivity(R.string.wifilistactivity,
                WifiListActivity.class);
        getTAApplication().registerActivity(R.string.cavas1activity,
                Canvas1Activity.class);
        getTAApplication().registerActivity(R.string.cavas2activity,
                Canvas2Activity.class);
        getTAApplication().registerActivity(R.string.clientactivity,
                ClientActivity.class);

	}

	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState) {
		super.onAfterOnCreate(savedInstanceState);

	}

	private void startMain() {
		doActivity(R.string.wifilistactivity);
	}

}
