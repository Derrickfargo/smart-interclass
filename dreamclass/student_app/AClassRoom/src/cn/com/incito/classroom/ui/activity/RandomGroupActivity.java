package cn.com.incito.classroom.ui.activity;

import java.util.List;
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
import cn.com.incito.classroom.vo.Student;
import cn.com.incito.common.utils.UIHelper;

import com.alibaba.fastjson.JSON;
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
		
		String extraData = getIntent().getExtras().getString("data");
		text_group = (TextView) findViewById(R.id.text_group);
		text_group.setText(resolveData(JSON.parseObject(extraData)));
		
		backAnimation = AnimationUtils.loadAnimation(this, R.anim.back_scale);
		frontAnimation = AnimationUtils.loadAnimation(this, R.anim.front_scale);
		
		myAnimationListener = new myAnimationListener();
		backAnimation.setAnimationListener(myAnimationListener);
		frontAnimation.setAnimationListener(myAnimationListener);
		
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
		timer.schedule(timerTask, 5 *1000);
	}
	
	/**
	 * 刷新数据
	 * @param data
	 */
	public void refreshData(JSONObject data){
		android.os.Message message = handler.obtainMessage();
		Bundle bundle = new Bundle();
		bundle.putString("student", resolveData(data));
		message.setData(bundle);
		message.what = 1;
		
		handler.sendMessage(message);
	}

	/**
	 * 解析json数据
	 * @return
	 */
	private String resolveData(JSONObject data) {
		StringBuilder sb = new StringBuilder();
		
		List<Student> students = JSON.parseArray(data.getString("students"), Student.class);
		if(students != null && students.size() > 0){
			for (Student student : students) {
				if(student != null){
					sb.append(student.getName() + "\n");
				}
			}
		}
		return sb.toString();
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
				isFront = false;
				if(timer != null){
					timer.cancel();
				}
			}else{
				text_group.setBackgroundResource(R.drawable.bg_badges);
				isFront = true;
				startTimer();
			}
		}
		@Override
		public void onAnimationRepeat(Animation animation) {}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(timer != null){
			timer.cancel();
		}
	}
}
