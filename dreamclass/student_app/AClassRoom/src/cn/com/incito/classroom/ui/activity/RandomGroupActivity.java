package cn.com.incito.classroom.ui.activity;

import java.lang.ref.WeakReference;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ListView;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.adapter.RandomGroupAdapter;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.vo.Student;
import cn.com.incito.common.utils.UIHelper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 随机分组activity
 * @author hm
 */
public class RandomGroupActivity extends BaseActivity {
	
	private LinearLayout linearLayout;
	private ListView student_list;
	private RandomGroupAdapter randomGroupAdapter;
	
	private Animation frontAnimation;  //翻转到正面动画
	
	private myAnimationListener myAnimationListener;
	private List<Student> studentList;
	
	private RandomGroupActivityHandler handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UIHelper.getInstance().setRandomGroupActivity(this);
		setContentView(R.layout.random_group_activity);
		handler = new RandomGroupActivityHandler(this);
		if(MyApplication.getInstance().isLockScreen()){
			MyApplication.getInstance().lockScreen(false);
		}
		
		linearLayout = (LinearLayout) findViewById(R.id.liner_layout);
		student_list = (ListView) findViewById(R.id.student_list);
		student_list.setVisibility(View.GONE);
		
		
		//解析后台传回的数据
		String students = getIntent().getExtras().getString("data");
		studentList = JSONArray.parseArray(JSON.parseObject(students).getString("students"), Student.class);
		
		randomGroupAdapter = new RandomGroupAdapter(this, studentList);
		student_list.setAdapter(randomGroupAdapter);
		
		//加载动画
		frontAnimation = AnimationUtils.loadAnimation(this, R.anim.front_scale);
		
		myAnimationListener = new myAnimationListener();
		frontAnimation.setAnimationListener(myAnimationListener);
		
		linearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				linearLayout.startAnimation(frontAnimation);
			}
		});
		
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
	
	private static class RandomGroupActivityHandler extends Handler{
		
		private WeakReference<RandomGroupActivity> weakReference;
		
		public RandomGroupActivityHandler(RandomGroupActivity activity) {
			weakReference = new WeakReference<RandomGroupActivity>(activity);
		}
		
		@Override
		public void handleMessage(Message msg) {
			RandomGroupActivity randomGroupActivity = weakReference.get();
			switch (msg.what) {
			case 1:
				Bundle bundle = msg.getData();
				String students = bundle.getString("student");
				if(randomGroupActivity != null){
					randomGroupActivity.studentList = JSONArray.parseArray(JSON.parseObject(students).getString("students"), Student.class);
					randomGroupActivity.randomGroupAdapter.setData(randomGroupActivity.studentList);
				}
			default:
				break;
			}
		}
	}
	/**
	 * 动画监听类
	 * @author hm
	 */
	class myAnimationListener implements AnimationListener{

		@Override
		public void onAnimationStart(Animation animation) {}

		@Override
		public void onAnimationEnd(Animation animation) {
				linearLayout.setBackgroundResource(R.drawable.puke_hover);
				student_list.setVisibility(View.VISIBLE);
				linearLayout.setClickable(false);
		}
		@Override
		public void onAnimationRepeat(Animation animation) {}
	}
	
	@Override
	protected void onDestroy() {
		if(frontAnimation != null){
			frontAnimation.cancel();
		}
		super.onDestroy();
	}
}
