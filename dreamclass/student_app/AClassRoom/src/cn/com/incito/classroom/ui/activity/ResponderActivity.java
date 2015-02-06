package cn.com.incito.classroom.ui.activity;

import java.lang.ref.WeakReference;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.socket.core.util.SendMessageUtil;

/**
 * 抢答界面
 * @author hm
 */
public class ResponderActivity extends BaseActivity {
	
	private static final int RANDOM_BUTTEON_POSITION = 1;
	private static final int HIDE_BUTTON = 2;
	private ImageButton imageButton;
	private RotateAnimation rotateAnimation;//旋转动画
	private boolean beforResponderisLockScreeen;
	private RelativeLayout.LayoutParams layoutParams;//按钮的布局属性
	private Timer timer;
	private TimerTask timerTask;
	private ResponderActivityHandler handler;
	
	private static final class ResponderActivityHandler extends Handler{
		WeakReference<ResponderActivity> weakReference;
		
		public ResponderActivityHandler(ResponderActivity activity) {
			weakReference = new WeakReference<ResponderActivity>(activity);
		}
		
		@Override
		public void handleMessage(android.os.Message msg) {
			final ResponderActivity responderActivity = weakReference.get();
			switch (msg.what) {
			case RANDOM_BUTTEON_POSITION:
				if(responderActivity != null){
					responderActivity.RandomButton();
				}
				break;
			case HIDE_BUTTON:
				if(responderActivity != null){
					responderActivity.imageButton.setVisibility(View.GONE);
				}
			}
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		handler = new ResponderActivityHandler(this);
		this.beforResponderisLockScreeen = getIntent().getExtras().getBoolean("beforResponderisLockScreeen");
		timer = new Timer();
		timerTask = new TimerTask() {
			
			@Override
			public void run() {
				handler.sendEmptyMessage(RANDOM_BUTTEON_POSITION);
			}
		};
		showImageButton();
		timer.schedule(timerTask, 0, 2*1000);
	}
	
	/**
	 * 随机改变按钮的位置
	 */
	private void RandomButton(){
		int x = randomX();
		int y = randomY();
		PropertyValuesHolder xPropertyValuesHolder = PropertyValuesHolder.ofFloat("x", x);
		PropertyValuesHolder yPropertyValuesHolder = PropertyValuesHolder.ofFloat("y", y);
		PropertyValuesHolder rotation = PropertyValuesHolder.ofFloat("rotation", 0,360);
		ObjectAnimator.ofPropertyValuesHolder(imageButton, xPropertyValuesHolder,yPropertyValuesHolder,rotation).setDuration(2*1000).start();
	}
	
	
	/**
	 * 显示布局与按钮
	 */
	private void showImageButton(){
		RelativeLayout relativeLayout = new RelativeLayout(this);
		relativeLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		relativeLayout.setBackgroundResource(R.drawable.bg_login);
		
		imageButton = new ImageButton(this);
		imageButton.setBackgroundResource(R.drawable.responder_select);
		imageButton.setX(randomX());
		imageButton.setY(randomY());
		imageButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		imageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				imageButton.setVisibility(View.GONE);
				SendMessageUtil.sendStudentResponder();
			}
		});
		relativeLayout.addView(imageButton,layoutParams);
		setContentView(relativeLayout);
	}

	/**
	 * 随机的x轴坐标
	 * @return
	 */
	private int randomX(){
		Random random = new Random();
		return random.nextInt(723);
	}
	
	/**
	 * 随机的y坐标
	 * @return
	 */
	private int randomY(){
		Random random = new Random();
		return random.nextInt(520);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(rotateAnimation != null){
			rotateAnimation.cancel();
		}
		if(timer != null){
			timer.cancel();
		}
	}
	
	/**
	 * 获取在抢答前PAD是否是锁屏状态
	 * @return true 是锁屏  flase 没有锁屏
	 */
	public boolean getBeforResponderisLockScreeen(){
		return beforResponderisLockScreeen;
	}
	
	/**
	 * 收到抢答结束命令隐藏抢答按钮
	 * @author hm
	 * @date 2015年2月6日 上午9:45:36 
	 * @return void
	 */
	public void hideImageButton(){
		handler.sendEmptyMessage(HIDE_BUTTON);
	}
}
