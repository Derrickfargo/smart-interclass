package cn.com.incito.classroom.ui.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.common.utils.UIHelper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 随机分组activity
 * @author hm
 */
public class RandomGroupActivity extends BaseActivity {
	
	private TextView text_group;
	
	private Timer timer;
	private TimerTask timerTask;
	
	private boolean isFront = false;
	
	private Animation backAnimation;
	private Animation frontAnimation;
	private myAnimationListener myAnimationListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UIHelper.getInstance().setRandomGroupActivity(this);
		setContentView(R.layout.random_group_activity);
		text_group = (TextView) findViewById(R.id.text_group);
		
		backAnimation = AnimationUtils.loadAnimation(this, R.anim.back_scale);
		frontAnimation = AnimationUtils.loadAnimation(this, R.anim.front_scale);
		
		myAnimationListener = new myAnimationListener();
		backAnimation.setAnimationListener(myAnimationListener);
		frontAnimation.setAnimationListener(myAnimationListener);
		
		startTimer();
		
		text_group.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!isFront){
					//如果是反面则翻转到正面
					text_group.startAnimation(frontAnimation);
				}else{
					if(timer != null){
						timer.cancel();
						startTimer();
					}
				}
			}
		});
	}

	private void startTimer() {
		timer = new Timer();
		timerTask = new TimerTask() {
			
			@Override
			public void run() {
				//如果是正面则自动反转到反面
				if(isFront){
					handler.sendEmptyMessage(0);
				}
			}
		};
		timer.schedule(timerTask, 3 *1000);
	}
	
	/**
	 * 刷新数据
	 * @param data
	 */
	public void refreshData(JSONObject data){
		android.os.Message message = handler.obtainMessage();
		
		//解析json数据
		StringBuilder sb = new StringBuilder();
		JSONArray jsonArray = JSONArray.parseArray(data.toString());
		if(jsonArray != null && jsonArray.size() > 0){
			for(int i = 0; i < jsonArray.size(); i++){
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				sb.append(jsonObject.getString("name")  + "\n");
				sb.append(jsonObject.getString("number"));
				sb.append("\n");
			}
		}
		
		Bundle bundle = new Bundle();
		bundle.putString("student", sb.toString());
		message.setData(bundle);
		message.what = 1;
		
		handler.sendMessage(message);
	}
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				text_group.startAnimation(backAnimation);
				break;
			case 1:
				Bundle bundle = msg.getData();
				String student = bundle.getString("student");
				text_group.setText(student);
			default:
				break;
			}
		};
	};

	/**
	 * 动画监听类
	 * @author hm
	 */
	class myAnimationListener implements AnimationListener{

		@Override
		public void onAnimationStart(Animation animation) {}

		@Override
		public void onAnimationEnd(Animation animation) {
			if(isFront){
				text_group.setBackgroundResource(R.drawable.bg);
				text_group.setText("");
				isFront = false;
				if(timer != null){
					timer.cancel();
				}
			}else{
				text_group.setBackgroundResource(R.drawable.bg_badges);
				text_group.setText("张三");
				isFront = true;
				startTimer();
			}
		}
		@Override
		public void onAnimationRepeat(Animation animation) {}
	}
}
