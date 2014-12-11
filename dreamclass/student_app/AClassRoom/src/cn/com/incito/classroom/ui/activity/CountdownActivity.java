package cn.com.incito.classroom.ui.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.BaseActivity;

/**
 * 倒计时activity
 * @author hm
 *
 */
@SuppressLint("HandlerLeak")
public class CountdownActivity extends BaseActivity {

	private ImageView countdown_image;
	private Timer timer;
	private TimerTask timerTask;
	private int backgroundNumber = 3;
	private ScaleAnimation scaleAnimation;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 3:
				countdown_image.setImageResource(R.drawable.cuountdown_3);
				countdown_image.startAnimation(scaleAnimation);
				backgroundNumber--;
				break;
			case 2:
				countdown_image.setImageResource(R.drawable.cuountdown_2);
				countdown_image.startAnimation(scaleAnimation);
				backgroundNumber--;
				break;
			case 1:
				countdown_image.setImageResource(R.drawable.cuountdown_1);
				countdown_image.startAnimation(scaleAnimation);
				backgroundNumber--;
				break;
			case 0:
				Intent intent = new Intent(CountdownActivity.this,ResponderActivity.class);
				CountdownActivity.this.finish();
				startActivity(intent);
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.count_down_activity);
	
		countdown_image = (ImageView) findViewById(R.id.countdown_image);
		
		scaleAnimation = new ScaleAnimation(0.5f, 1, 0.7f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnimation.setDuration(1000);
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
				}else if (backgroundNumber == 2) {
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
		timer.schedule(timerTask, 1 * 1000, 2 * 1000);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(timer != null){
			timer.cancel();
		}
		if(scaleAnimation != null){
			scaleAnimation.cancel();
		}
	}
}
