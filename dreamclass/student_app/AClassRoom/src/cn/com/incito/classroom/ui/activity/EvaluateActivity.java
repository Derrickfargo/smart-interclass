package cn.com.incito.classroom.ui.activity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.vo.EvaluateTempVo;
import cn.com.incito.classroom.vo.EvaluateVo;
import cn.com.incito.common.utils.AndroidUtil;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.CoreSocket;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class EvaluateActivity extends BaseActivity implements OnClickListener {

	LayoutInflater mInflater;

	private int mCurrentViewID = 0; // 当前页卡编号

	private Button button_one;

	private Button button_two;

	private Button button_three;

	private Button button_four;

	private Button button_five;

	private Button button_confirm;

	private Button pager_up;

	private Button pager_down;

	private ViewPager viewPage;

	private ArrayList<View> listViews;

	private Animation animation;

	public Button number1;

	public Button number2;

	ArrayList<EvaluateVo> quizList = new ArrayList<EvaluateVo>();// 收到教师端发送的作业的所有属性都存在里面

	private MyPagerAdapter adpt;

	private byte[] paper;

	private String uuid;

	private Button evalute_right;

	private LinearLayout layout_evalute;

	private boolean isClick;

	private LinearLayout relative_layout;
	ArrayList<ImageView> imageList = new ArrayList<ImageView>();//存放导航图标

	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState) {
		super.onAfterOnCreate(savedInstanceState);
		setContentView(R.layout.evaluate_activity);
		UIHelper.getInstance().setEvaluateActivity(this);
		initView();
	}

	/**
	 * 初始化界面
	 */
	private void initView() {
		mInflater = getLayoutInflater();
		byte[] paper = getIntent().getExtras().getByteArray("paper");
		String quizId=getIntent().getStringExtra("quizId");
		EvaluateVo evaluateVo = new EvaluateVo();
		evaluateVo.setQuizId(quizId);
		evaluateVo.setPaperPic(getDrawable(paper));
		quizList.add(evaluateVo);
		animation = AnimationUtils.loadAnimation(EvaluateActivity.this, R.anim.score_anim);
		pager_up = (Button) findViewById(R.id.pager_up);
		pager_up.setOnClickListener(this);
		pager_down = (Button) findViewById(R.id.pager_down);
		pager_down.setOnClickListener(this);
		button_one = (Button) findViewById(R.id.number_one);
		button_one.setOnClickListener(this);
		button_two = (Button) findViewById(R.id.number_two);
		button_two.setOnClickListener(this);
		button_three = (Button) findViewById(R.id.number_three);
		button_three.setOnClickListener(this);
		button_four = (Button) findViewById(R.id.number_four);
		button_four.setOnClickListener(this);
		button_five = (Button) findViewById(R.id.number_five);
		button_five.setOnClickListener(this);
		button_confirm = (Button) findViewById(R.id.btn_confirm);
		button_confirm.setOnClickListener(this);
		evalute_right=(Button)findViewById(R.id.evalute_right);
		evalute_right.setOnClickListener(this);
		layout_evalute=(LinearLayout)findViewById(R.id.layout_evalute);
		layout_evalute.setOnClickListener(this);
		relative_layout=(LinearLayout)findViewById(R.id.relative_layout);
		InitViewPager();
	}

	private void InitViewPager() {
		viewPage = (ViewPager) findViewById(R.id.viewPager);
		listViews = new ArrayList<View>();
		listViews.add(mInflater.inflate(R.layout.evaluate_item, null));
		ImageView imageView= new ImageView(this);
		imageView.setImageResource(R.drawable.circle_foucs);
		imageList.add(imageView);
		relative_layout.addView(imageView);
		adpt = new MyPagerAdapter(listViews, quizList);
		viewPage.setAdapter(adpt);
		viewPage.setCurrentItem(0);
		viewPage.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	@Override
	public void onClick(View v) {
		int viewID = v.getId();
		switch (viewID) {
		case R.id.pager_up:
			if (mCurrentViewID != 0) {
				mCurrentViewID--;
				viewPage.setCurrentItem(mCurrentViewID, true);
			}
			break;
		case R.id.pager_down:
			if (mCurrentViewID != quizList.size() - 1&&quizList.size()>0) {
				mCurrentViewID++;
				viewPage.setCurrentItem(mCurrentViewID, true);
			}
			break;
		case R.id.number_one:
			setScore(1, mCurrentViewID);
			isConfirmBtnVisible();
			break;
		case R.id.number_two:
			setScore(2, mCurrentViewID);
			isConfirmBtnVisible();
			break;
		case R.id.number_three:
			setScore(3, mCurrentViewID);
			isConfirmBtnVisible();
			break;
		case R.id.number_four:
			setScore(4, mCurrentViewID);
			isConfirmBtnVisible();
			break;
		case R.id.number_five:
			setScore(5, mCurrentViewID);
			isConfirmBtnVisible();
			break;
		case R.id.btn_confirm:
			sendScore();
		case R.id.evalute_right:
			if(!isClick){
				Animator button_animation = AnimatorInflater.loadAnimator(this, R.animator.button_dismiss_out);
				button_animation.setTarget(layout_evalute);
				button_animation.start();
				isClick=true;
			}else{
				Animator button_animation = AnimatorInflater.loadAnimator(this, R.animator.button_dismiss_in);
				button_animation.setTarget(layout_evalute);
				button_animation.start();
				isClick=false;
			}
			break;
		default:
			break;
		}
	}

	/**
	 * @param score
	 * @param ViewId
	 *            根据viewpager的当前页 设置名次
	 */
	public void setScore(int score, int currentmViewPageId) {
		// 获得的按钮是当前页面按钮的时候才进行名次显示
		Button button = (Button) listViews.get(currentmViewPageId).findViewById(R.id.number);
		if(score==1){
			button.setBackgroundResource(R.drawable.number_one);
		}else if(score==2){
			button.setBackgroundResource(R.drawable.number_two);
		}else if(score==3){
			button.setBackgroundResource(R.drawable.number_three);
		}else if(score==4){
			button.setBackgroundResource(R.drawable.number_four);
		}else if(score==5){
			button.setBackgroundResource(R.drawable.number_five);
		}
		button.setVisibility(View.VISIBLE);
		quizList.get(currentmViewPageId).setSelectNumber(score);
		int temp = score;
		int pagerID=currentmViewPageId;
		for (int j = 0; j < quizList.size(); j++) {
			if (temp == quizList.get(j).getSelectNumber() && j != pagerID) {
				if(temp==5){
					quizList.get(j).setSelectNumber(0);
				}else{
					quizList.get(j).setSelectNumber(quizList.get(j).getSelectNumber() + 1);
					for (int k = 0; k < quizList.size(); k++) {
						if (quizList.get(k).getSelectNumber() == quizList.get(j).getSelectNumber() && k != j) {
							quizList.get(k).setSelectNumber(quizList.get(k).getSelectNumber() + 1);
							temp = quizList.get(k).getSelectNumber();
							pagerID=k;
						}
					}
				}
			}
			
		}
		adpt.setPaperList(quizList);
		button.startAnimation(animation);
	}

	/**
	 * 发送评价结果
	 */
	public void sendScore() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("imei", MyApplication.deviceId);
		jsonObject.put("feedback",  getInfo());
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_QUIZ_FEEDBACK_SUBMIT);
		messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(jsonObject.toJSONString()));
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+"提交的评论结果"+jsonObject.toJSONString());
		CoreSocket.getInstance().sendMessage(messagePacking);
		this.finish();
	}
	/**
	 * ViewPager适配器
	 */
	public class MyPagerAdapter extends PagerAdapter {

		public List<View> mListViews;

		public ArrayList<EvaluateVo> paperList;

		public void setPaperList(ArrayList<EvaluateVo> paperList) {
			this.paperList = paperList;
			notifyDataSetChanged();
		}

		public MyPagerAdapter(List<View> mListViews, ArrayList<EvaluateVo> quizList) {
			this.mListViews = mListViews;
			this.paperList = quizList;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			mListViews.get(arg1).setBackgroundDrawable(quizList.get(arg1).getPaperPic());
			Button score = (Button) mListViews.get(arg1).findViewById(R.id.number);// 名次显示按钮
			if (quizList.get(arg1).getSelectNumber() != 0) {
				score.setVisibility(View.VISIBLE);
			}
			
			
			
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {

			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}

	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			mCurrentViewID = arg0;
			for (int i = 0; i < imageList.size(); i++) {
				if(arg0==i){
					imageList.get(i).setImageResource(R.drawable.circle_foucs);
				}else{
					imageList.get(i).setImageResource(R.drawable.circle_no_foucs);
				}
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	public void setQuizList(byte[] paper,String uuid) {
		handler.sendEmptyMessage(1);
		this.paper=paper;
		this.uuid=uuid;
	}

	/**
	 * @param paper
	 *            获得作业
	 */
	public Drawable getDrawable(byte[] paper) {
		ByteArrayOutputStream outPut = new ByteArrayOutputStream();
		Bitmap mBitmap = BitmapFactory.decodeByteArray(paper, 0, paper.length);
		mBitmap.compress(CompressFormat.PNG, 100, outPut);
		BitmapDrawable bd = new BitmapDrawable(mBitmap);
		return bd;
	}

	/**
	 * 获取上传
	 */
	public ArrayList<EvaluateTempVo> getInfo() {
		ArrayList<EvaluateTempVo> tempList = new ArrayList<EvaluateTempVo>();
		for (int i = 0; i < quizList.size(); i++) {
			EvaluateTempVo tempVo = new EvaluateTempVo();
			tempVo.setId(quizList.get(i).getQuizId());
			tempVo.setScore(quizList.get(i).getSelectNumber());
			tempList.add(tempVo);
		}
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+"作业评分结果："+JSON.toJSONString(tempList));
		return tempList;
	}
	/**
	 * 检测提交按钮是否出现
	 */
	public void isConfirmBtnVisible(){
		int j=0;
		for (int i = 0; i < quizList.size(); i++) {
			if(quizList.get(i).getSelectNumber()!=0){
				j++;
			}
		}
		MyApplication.Logger.debug("已经选择的名次个数"+j);
		if(j>=3){
			button_confirm.setVisibility(View.VISIBLE);
		}else{
			button_confirm.setVisibility(View.GONE);
		}
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				EvaluateVo evaluateVo = new EvaluateVo();
				evaluateVo.setPaperPic(getDrawable(paper));
				evaluateVo.setQuizId(uuid);
				quizList.add(evaluateVo);
				ImageView imageView= new ImageView(EvaluateActivity.this);
				imageView.setImageResource(R.drawable.circle_no_foucs);
				imageView.setPadding(10, 0, 0, 0);
				imageList.add(imageView);
				relative_layout.addView(imageView);
				listViews.add(mInflater.inflate(R.layout.evaluate_item, null));
				adpt.setPaperList(quizList);
				break;
			default:
				break;
			}
		}
	};
	@Override
	protected void onDestroy() {
		super.onDestroy();
		UIHelper.getInstance().setEvaluateActivity(null);
	}
}
