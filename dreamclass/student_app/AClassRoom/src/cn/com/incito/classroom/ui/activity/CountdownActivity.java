package cn.com.incito.classroom.ui.activity;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.base.MyApplication;

/**
 * 倒计时activity
 * @author hm
 *
 */
public class CountdownActivity extends BaseActivity {

	private ImageView countdown_image;
	private Timer timer;
	private TimerTask timerTask;
	private int backgroundNumber = 3;
	private ScaleAnimation scaleAnimation;
	private Handler handler;
	private boolean beforResponderisLockScreeen;

	private static class CountdownHandler extends Handler {
		private WeakReference<CountdownActivity> weakReference;
		public CountdownHandler(CountdownActivity activity) {
			weakReference = new WeakReference<CountdownActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			CountdownActivity activity = weakReference.get();
			switch (msg.what) {
			case 3:
				activity.countdown_image.setImageResource(R.drawable.cuountdown_3);
				activity.countdown_image.startAnimation(activity.scaleAnimation);
				activity.backgroundNumber--;
				break;
			case 2:
				activity.countdown_image.setImageResource(R.drawable.cuountdown_2);
				activity.countdown_image.startAnimation(activity.scaleAnimation);
				activity.backgroundNumber--;
				break;
			case 1:
				activity.countdown_image.setImageResource(R.drawable.cuountdown_1);
				activity.countdown_image.startAnimation(activity.scaleAnimation);
				activity.backgroundNumber--;
				break;
			case 0:
				Intent intent = new Intent(activity, ResponderActivity.class);
				intent.putExtra("beforResponderisLockScreeen",activity.beforResponderisLockScreeen);
				activity.finish();
				activity.startActivity(intent);
				break;
			default:
				break;
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.count_down_activity);
		handler = new CountdownHandler(this);
		countdown_image = (ImageView) findViewById(R.id.countdown_image);
		scaleAnimation = new ScaleAnimation(0.5f, 1, 0.7f, 1,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
		scaleAnimation.setDuration(500);
		scaleAnimation.setFillAfter(true);
		scaleAnimation.setFillBefore(false);
		scaleAnimation.setInterpolator(new AccelerateInterpolator());
		scaleAnimation.setRepeatMode(Animation.RESTART);
		scaleAnimation.setRepeatCount(0);
		timer = new Timer();
		timerTask = new TimerTask() {
			@Override
			public void run() {
				if (backgroundNumber == 3) {
					handler.sendEmptyMessage(3);
				} else if (backgroundNumber == 2) {
					handler.sendEmptyMessage(2);
				} else if (backgroundNumber == 1) {
					handler.sendEmptyMessage(1);
				} else {
					if (timer != null) {
						timer.cancel();
					}
					handler.sendEmptyMessage(0);
				}
			}
		};
		timer.schedule(timerTask, 1 * 1000, 1 * 1000);
		beforResponderisLockScreeen = MyApplication.getInstance().isLockScreen();
		if(beforResponderisLockScreeen){
			MyApplication.getInstance().lockScreen(false);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
		}
		if (scaleAnimation != null) {
			scaleAnimation.cancel();
		}
	}
}
