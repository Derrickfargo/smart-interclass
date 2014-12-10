package cn.com.incito.classroom.ui.activity;

import java.util.ArrayList;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.adapter.RandomGroupAdapter;
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
	
	private LinearLayout linearLayout;
	private ListView student_list;
	private RandomGroupAdapter randomGroupAdapter;
	
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
		
		linearLayout = (LinearLayout) findViewById(R.id.liner_layout);
		student_list = (ListView) findViewById(R.id.student_list);
		student_list.setVisibility(View.GONE);
		
		//TODO 后台数据回来后这段将删除
		List<Student> studentList = new ArrayList<Student>();
		
		Student s = new Student();
		for(int i = 0; i < 5; i++){
			studentList.add(s);
		}
		
		randomGroupAdapter = new RandomGroupAdapter(this, studentList);
		
		student_list.setAdapter(randomGroupAdapter);
		
		backAnimation = AnimationUtils.loadAnimation(this, R.anim.back_scale);
		frontAnimation = AnimationUtils.loadAnimation(this, R.anim.front_scale);
		
		myAnimationListener = new myAnimationListener();
		backAnimation.setAnimationListener(myAnimationListener);
		frontAnimation.setAnimationListener(myAnimationListener);
		
		linearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!isFront){
					//如果是反面则翻转到正面
					linearLayout.startAnimation(frontAnimation);
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
		bundle.putString("student", data.toJSONString());
		message.setData(bundle);
		message.what = 1;
		
		handler.sendMessage(message);
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				linearLayout.startAnimation(backAnimation);
				break;
			case 1:
				Bundle bundle = msg.getData();
				String students = bundle.getString("student");
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
				linearLayout.setBackgroundResource(R.drawable.puke);
				student_list.setVisibility(View.GONE);
				isFront = false;
				if(timer != null){
					timer.cancel();
				}
			}else{
				linearLayout.setBackgroundResource(R.drawable.puke_hover);
				student_list.setVisibility(View.VISIBLE);
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
