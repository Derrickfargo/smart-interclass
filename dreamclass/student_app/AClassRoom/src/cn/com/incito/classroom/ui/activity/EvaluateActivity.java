package cn.com.incito.classroom.ui.activity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.vo.EvaluateVo;
import cn.com.incito.socket.core.CoreSocket;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;

import com.alibaba.fastjson.JSONObject;

public class EvaluateActivity extends BaseActivity implements OnClickListener {

	private int mCurrentViewID = 0; // 当前页卡编号

	private int PAGER_NUM = 5;// 页面总数

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

	// public Button number0;

	public Button number1;

	public Button number2;

	ArrayList<EvaluateVo> quizList = new ArrayList<EvaluateVo>();// 收到教师端发送的作业的所有属性都存在里面

	ArrayList<Button> scoreList = new ArrayList<Button>();// 获得的名次按钮

	private MyPagerAdapter adpt;

	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState) {
		super.onAfterOnCreate(savedInstanceState);
		setContentView(R.layout.evaluate_activity);
		initView();
	}

	/**
	 * 初始化界面
	 */
	private void initView() {
		 byte[] paper = getIntent().getExtras().getByteArray("paper");
		 EvaluateVo evaluateVo = new EvaluateVo();
		 evaluateVo.setPaperPic(getDrawable(paper));
		 quizList.add(evaluateVo);
//		for (int i = 0; i < 5; i++) {
//			EvaluateVo evaluateVo = new EvaluateVo();
//			evaluateVo.setPaperPic(getResources().getDrawable(R.drawable.bg_empty));
//			quizList.add(evaluateVo);
//		}
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
		InitViewPager();
	}

	private void InitViewPager() {
		viewPage = (ViewPager) findViewById(R.id.viewPager);
		listViews = new ArrayList<View>();
		LayoutInflater mInflater = getLayoutInflater();
		for (int i = 0; i < 5; i++) {
			listViews.add(mInflater.inflate(R.layout.evaluate_item, null));
		}
		adpt=new MyPagerAdapter(listViews, quizList);
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
			if (mCurrentViewID != PAGER_NUM - 1) {
				mCurrentViewID++;
				viewPage.setCurrentItem(mCurrentViewID, true);
			}
			break;
		case R.id.number_one:
			setScore("1", mCurrentViewID);
			break;
		case R.id.number_two:
			setScore("2", mCurrentViewID);
			break;
		case R.id.number_three:
			setScore("3", mCurrentViewID);
			break;
		case R.id.number_four:
			setScore("4", mCurrentViewID);
			break;
		case R.id.number_five:
			setScore("5", mCurrentViewID);
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
	public void setScore(String score, int currentViewPageId) {
		for (int i = 0; i < scoreList.size(); i++) {
			if (i == currentViewPageId) {// 获得的按钮是当前页面按钮的时候才进行名次显示
				scoreList.get(i).setText(score);
				quizList.get(i).setSelectNumber(Integer.parseInt(score));
				for (int j = 0; j < quizList.size(); j++) {
					if(quizList.get(i).getSelectNumber()==quizList.get(j).getSelectNumber()){
						quizList.get(j).setSelectNumber(Integer.parseInt(score)+1);
						for (int j2 = 0; j2 < quizList.size(); j2++){
							if(quizList.get(j).getSelectNumber()==quizList.get(j2).getSelectNumber()){
								quizList.get(j2).setSelectNumber(quizList.get(j).getSelectNumber()+1);
							}
						}
					}
				}
				scoreList.get(i).setVisibility(View.VISIBLE);
				scoreList.get(i).startAnimation(animation);
			}
		}
	}
	/**
	 * 发送评价结果
	 */
	public void sendScore() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("imei", MyApplication.deviceId);
		jsonObject.put("feedback", "作业部分json id是quizId和score是分数");
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_STUDENT_EVALUATE);
		messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(jsonObject.toJSONString()));
		CoreSocket.getInstance().sendMessage(messagePacking);
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
			scoreList.add(score);
			if (quizList.get(arg1).getSelectNumber() != 0) {
				score.setText(quizList.get(arg1).getSelectNumber() + "");
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
			if(quizList.get(arg0).getSelectNumber()!=0)
				scoreList.get(arg0).setText(quizList.get(arg0).getSelectNumber() + " ");
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	public void setQuizList(byte[] paper) {
		EvaluateVo evaluateVo = new EvaluateVo();
		evaluateVo.setPaperPic(getDrawable(paper));
		quizList.add(evaluateVo);
		adpt.setPaperList(quizList);
		
	}

	/**
	 * @param paper
	 *            获得作业
	 */
	public Drawable getDrawable(byte[] paper) {
		ByteArrayOutputStream outPut = new ByteArrayOutputStream();
		Bitmap bitmap = BitmapFactory.decodeByteArray(paper, 0, paper.length);
		bitmap.compress(CompressFormat.PNG, 100, outPut);
		BitmapDrawable bd = new BitmapDrawable(bitmap);
		return bd;
	}

	/**
	 * 初始化每个名次按钮的状态
	 */
	// public void initEvaluateBtn() {
	// button_one.setBackgroundResource(R.drawable.number_1);
	// button_two.setBackgroundResource(R.drawable.number_2);
	// button_three.setBackgroundResource(R.drawable.number_3);
	// button_four.setBackgroundResource(R.drawable.number_4);
	// button_five.setBackgroundResource(R.drawable.number_5);
	// }

	/**
	 * @param btn
	 *            改变被点击的名次按钮颜色为灰色
	 */
	// public void changeEvaluateBtnBg(Button btn) {
	// initEvaluateBtn();
	// btn.setBackgroundResource(R.drawable.number_1);// 设置点击按钮变灰色
	// switch (btn.getId()) {
	// case R.id.number_one:
	// quizList.get(mCurrentViewID).setSelectNumber(1);
	// break;
	// case R.id.number_two:
	// quizList.get(mCurrentViewID).setSelectNumber(2);
	// break;
	// case R.id.number_three:
	// quizList.get(mCurrentViewID).setSelectNumber(3);
	// break;
	// case R.id.number_four:
	// quizList.get(mCurrentViewID).setSelectNumber(4);
	// break;
	// case R.id.number_five:
	// quizList.get(mCurrentViewID).setSelectNumber(5);
	// break;
	// default:
	// break;
	// }
	// }

}
