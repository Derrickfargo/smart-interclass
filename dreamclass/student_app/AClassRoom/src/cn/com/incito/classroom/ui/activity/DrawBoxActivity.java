package cn.com.incito.classroom.ui.activity;

import java.io.ByteArrayOutputStream;
import java.util.Timer;
import java.util.TimerTask;

import com.alibaba.fastjson.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.classroom.utils.BitmapUtil;
import cn.com.incito.classroom.utils.Utils;
import cn.com.incito.classroom.widget.canvas.ISketchPadCallback;
import cn.com.incito.classroom.widget.canvas.SketchPadView;
import cn.com.incito.common.utils.ToastHelper;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.CoreSocket;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;
import cn.com.incito.wisdom.uicomp.widget.dialog.ProgressiveDialog;

/**
 * 绘画板activity Created by liguangming on 2014/7/28.
 */
public class DrawBoxActivity extends BaseActivity implements OnClickListener,
		ISketchPadCallback {
	public static final String TAG=DrawBoxActivity.class.getSimpleName();
	private ImageView cleanBtn;
	private ImageButton delAllBtn;
	private FrameLayout line;
	private LinearLayout line1;
	private SketchPadView m_sketchPad = null;
	private Button changeBtn;
	private ImageView color_white;
	private ImageView color_canary_yellow;
	private ImageView ico_lemon_yellow;
	private ImageView ico_spanish_orange;
	private ImageView mMineralOrange;
	private ImageView mCarmineRed;
	private ImageView mHotInk;
	private ImageView ico_salmon_orange;
	private ImageView mMagenta;
	private ImageView ico_powder_blue;
	private ImageView ico_turquoise;
	private ImageView ico_peacpck_blue;
	private ImageView ico_dark_purple;
	private ImageView mDarkGreen;
	private ImageView mMossGreen;
	private ImageView mParrotGreen;
	private ImageView mChocolate;
	private ImageView mGrey;
	private ImageView mBlack;
	private ImageView mImageView = null;
	private RelativeLayout mBigRelativeLayout;
	private RelativeLayout mMidRelativeLayout;
	private RelativeLayout mSmallRelativeLayout;
	private ImageView mBigCheckIco;
	private ImageView mMiddleCheckIco;
	private ImageView mSmallCheckIco;
	private int PEN_WIDTH = 10;
	private boolean isBlack = true;
	private Button mSubmitButton;
	private PopupWindow mPopupWindow;
	private ImageButton earise_big;
	private ImageButton earise_middle;
	private ImageButton earise_small;
	private Bitmap bitmap;
	private Timer timeTimer;
	private TimerTask timeTimerTask;
	private ProgressiveDialog mProgressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.getWindow().setFlags(Constants.FLAG_HOMEKEY_DISPATCHED,
				Constants.FLAG_HOMEKEY_DISPATCHED);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.draw_box);
		UIHelper.getInstance().setDrawBoxActivity(this);
		initViews();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initViews() {
		// 颜色选择按钮
		color_white = (ImageView) findViewById(R.id.color_white);
		color_white.setOnClickListener(this);
		color_canary_yellow = (ImageView) findViewById(R.id.color_canary_yellow);
		color_canary_yellow.setOnClickListener(this);
		ico_lemon_yellow = (ImageView) findViewById(R.id.ico_lemon_yellow);
		ico_lemon_yellow.setOnClickListener(this);
		ico_salmon_orange = (ImageView) findViewById(R.id.ico_salmon_orange);
		ico_salmon_orange.setOnClickListener(this);
		ico_spanish_orange = (ImageView) findViewById(R.id.ico_spanish_orange);
		ico_spanish_orange.setOnClickListener(this);
		mMineralOrange = (ImageView) findViewById(R.id.ico_mineral_orange);
		mMineralOrange.setOnClickListener(this);
		mCarmineRed = (ImageView) findViewById(R.id.ico_carmine_red);
		mCarmineRed.setOnClickListener(this);
		mHotInk = (ImageView) findViewById(R.id.ico_hot_ink);
		mHotInk.setOnClickListener(this);
		mMagenta = (ImageView) findViewById(R.id.ico_magenta);
		mMagenta.setOnClickListener(this);
		ico_powder_blue = (ImageView) findViewById(R.id.ico_powder_blue);
		ico_powder_blue.setOnClickListener(this);
		ico_turquoise = (ImageView) findViewById(R.id.ico_turquoise);
		ico_turquoise.setOnClickListener(this);
		ico_peacpck_blue = (ImageView) findViewById(R.id.ico_peacpck_blue);
		ico_peacpck_blue.setOnClickListener(this);
		ico_dark_purple = (ImageView) findViewById(R.id.ico_dark_purple);
		ico_dark_purple.setOnClickListener(this);
		mDarkGreen = (ImageView) findViewById(R.id.ico_dark_green);
		mDarkGreen.setOnClickListener(this);
		mMossGreen = (ImageView) findViewById(R.id.ico_moss_green);
		mMossGreen.setOnClickListener(this);
		mParrotGreen = (ImageView) findViewById(R.id.ico_parrot_green);
		mParrotGreen.setOnClickListener(this);
		mChocolate = (ImageView) findViewById(R.id.ico_chocolate);
		mChocolate.setOnClickListener(this);
		mGrey = (ImageView) findViewById(R.id.ico_grey);
		mGrey.setOnClickListener(this);
		mBlack = (ImageView) findViewById(R.id.ico_black);
		mBlack.setOnClickListener(this);
		changeBtn = (Button) findViewById(R.id.bg_select_btn);// 背景选择按钮
		changeBtn.setOnClickListener(this);
		// saveBtn.setOnClickListener(this);
		cleanBtn = (ImageView) findViewById(R.id.earise);
		cleanBtn.setOnClickListener(this);
		
		line = (FrameLayout) findViewById(R.id.line);
		// 画笔大小选择
		mBigRelativeLayout = (RelativeLayout) findViewById(R.id.big);
		mMidRelativeLayout = (RelativeLayout) findViewById(R.id.middle);
		mSmallRelativeLayout = (RelativeLayout) findViewById(R.id.small);
		mBigRelativeLayout.setOnClickListener(this);
		mMidRelativeLayout.setOnClickListener(this);
		mSmallRelativeLayout.setOnClickListener(this);
		mBigCheckIco = (ImageView) findViewById(R.id.ico_big_checked);
		mMiddleCheckIco = (ImageView) findViewById(R.id.ico_middle_checked);
		mSmallCheckIco = (ImageView) findViewById(R.id.ico_small_checked);
		mSubmitButton = (Button) findViewById(R.id.submit_button);
		mSubmitButton.setOnClickListener(this);
		m_sketchPad = (SketchPadView) findViewById(R.id.sketchpad);
		m_sketchPad.setCallback(DrawBoxActivity.this);
		mProgressDialog = new ProgressiveDialog(this);
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.setMessage(R.string.sendpaper_notice);
		if (getIntent().getExtras() != null) {
			byte[] paper = getIntent().getExtras().getByteArray("paper");
			if (paper != null) {
				ByteArrayOutputStream outPut = new ByteArrayOutputStream();
				bitmap = BitmapFactory.decodeByteArray(paper, 0, paper.length);
				bitmap.compress(CompressFormat.PNG, 100, outPut);
				changeBtn.setClickable(false);
			}
		} else {
			bitmap = BitmapFactory
					.decodeResource(getResources(), R.drawable.bg);
			changeBtn.setClickable(true);
		}
		initPaint(bitmap);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() != R.id.bg_select_btn && v.getId() != R.id.big
				&& v.getId() != R.id.middle && v.getId() != R.id.small) {// 选择笔的粗细时
			// 跳出的笔不缩回
			Translate(mImageView, 0);// 跳出的笔缩回到原来位置
		}
		switch (v.getId()) {
		case R.id.color_white:
			Translate(color_white, -0.25f);
			m_sketchPad.setStrokeType(SketchPadView.STROKE_PEN);
			m_sketchPad.setStrokeColor(getResources().getColor(
					android.R.color.white));
			break;
		case R.id.color_canary_yellow:
			Translate(color_canary_yellow, -0.25f);
			m_sketchPad.setStrokeType(SketchPadView.STROKE_PEN);
			m_sketchPad.setStrokeColor(getResources().getColor(
					R.color.canary_yellow));
			break;
		case R.id.ico_lemon_yellow:
			Translate(ico_lemon_yellow, -0.25f);
			m_sketchPad.setStrokeType(SketchPadView.STROKE_PEN);
			m_sketchPad.setStrokeColor(getResources().getColor(
					R.color.lemon_yellow));
			break;
		case R.id.ico_salmon_orange:
			Translate(ico_salmon_orange, -0.25f);
			m_sketchPad.setStrokeType(SketchPadView.STROKE_PEN);
			m_sketchPad.setStrokeColor(getResources().getColor(
					R.color.salmon_orange));
			break;
		case R.id.ico_spanish_orange:
			Translate(ico_spanish_orange, -0.25f);
			m_sketchPad.setStrokeType(SketchPadView.STROKE_PEN);
			m_sketchPad.setStrokeColor(getResources().getColor(
					R.color.spanish_orange));
			break;
		case R.id.ico_mineral_orange:
			Translate(mMineralOrange, -0.25f);
			m_sketchPad.setStrokeType(SketchPadView.STROKE_PEN);
			m_sketchPad.setStrokeColor(getResources().getColor(
					R.color.mineral_orange));
			break;
		case R.id.ico_carmine_red:
			Translate(mCarmineRed, -0.25f);
			m_sketchPad.setStrokeType(SketchPadView.STROKE_PEN);
			m_sketchPad.setStrokeColor(getResources().getColor(
					R.color.carmine_red));
			break;
		case R.id.ico_hot_ink:
			Translate(mHotInk, -0.25f);
			m_sketchPad.setStrokeType(SketchPadView.STROKE_PEN);
			m_sketchPad
					.setStrokeColor(getResources().getColor(R.color.hot_ink));
			break;
		case R.id.ico_magenta:
			Translate(mMagenta, -0.25f);
			m_sketchPad.setStrokeType(SketchPadView.STROKE_PEN);
			m_sketchPad
					.setStrokeColor(getResources().getColor(R.color.magenta));
			break;
		case R.id.ico_powder_blue:
			Translate(ico_powder_blue, -0.25f);
			m_sketchPad.setStrokeType(SketchPadView.STROKE_PEN);
			m_sketchPad.setStrokeColor(getResources().getColor(
					R.color.powder_blue));
			break;
		case R.id.ico_turquoise:
			Translate(ico_turquoise, -0.25f);
			m_sketchPad.setStrokeType(SketchPadView.STROKE_PEN);
			m_sketchPad.setStrokeColor(getResources().getColor(
					R.color.turquoise));
			break;
		case R.id.ico_peacpck_blue:
			Translate(ico_peacpck_blue, -0.25f);
			m_sketchPad.setStrokeType(SketchPadView.STROKE_PEN);
			m_sketchPad.setStrokeColor(getResources().getColor(
					R.color.peacpck_blue));
			break;
		case R.id.ico_dark_purple:
			Translate(ico_dark_purple, -0.25f);
			m_sketchPad.setStrokeType(SketchPadView.STROKE_PEN);
			m_sketchPad.setStrokeColor(getResources().getColor(
					R.color.dark_purple));
			break;
		case R.id.ico_dark_green:
			Translate(mDarkGreen, -0.25f);
			m_sketchPad.setStrokeType(SketchPadView.STROKE_PEN);
			m_sketchPad.setStrokeColor(getResources().getColor(
					R.color.dark_green));
			break;
		case R.id.ico_moss_green:
			Translate(mMossGreen, -0.25f);
			m_sketchPad.setStrokeType(SketchPadView.STROKE_PEN);
			m_sketchPad.setStrokeColor(getResources().getColor(
					R.color.moss_green));
			break;
		case R.id.ico_parrot_green:
			Translate(mParrotGreen, -0.25f);
			m_sketchPad.setStrokeType(SketchPadView.STROKE_PEN);
			m_sketchPad.setStrokeColor(getResources().getColor(
					R.color.parrot_green));
			break;
		case R.id.ico_chocolate:
			Translate(mChocolate, -0.25f);
			m_sketchPad.setStrokeType(SketchPadView.STROKE_PEN);
			m_sketchPad.setStrokeColor(getResources().getColor(
					R.color.chocolate));
			break;
		case R.id.ico_black:
			Translate(mBlack, -0.25f);
			m_sketchPad.setStrokeType(SketchPadView.STROKE_PEN);
			m_sketchPad.setStrokeColor(getResources().getColor(R.color.black));
			break;
		case R.id.ico_grey:
			Translate(mGrey, -0.25f);
			m_sketchPad.setStrokeType(SketchPadView.STROKE_PEN);
			m_sketchPad.setStrokeColor(getResources().getColor(R.color.grey));
			break;

		case R.id.big:// 粗笔
			PEN_WIDTH = 15;
			m_sketchPad.setStrokeSize(PEN_WIDTH, SketchPadView.STROKE_PEN);
			;
			mBigCheckIco.setVisibility(View.VISIBLE);
			mMiddleCheckIco.setVisibility(View.GONE);
			mSmallCheckIco.setVisibility(View.GONE);
			break;
		case R.id.middle:// 中等笔
			PEN_WIDTH = 10;
			m_sketchPad.setStrokeSize(PEN_WIDTH, SketchPadView.STROKE_PEN);
			;
			mBigCheckIco.setVisibility(View.GONE);
			mMiddleCheckIco.setVisibility(View.VISIBLE);
			mSmallCheckIco.setVisibility(View.GONE);
			break;
		case R.id.small:// 细笔
			PEN_WIDTH = 3;
			m_sketchPad.setStrokeSize(PEN_WIDTH, SketchPadView.STROKE_PEN);
			;
			mBigCheckIco.setVisibility(View.GONE);
			mMiddleCheckIco.setVisibility(View.GONE);
			mSmallCheckIco.setVisibility(View.VISIBLE);
			break;
		// 橡皮擦(擦出)
		case R.id.earise:
			// view.isDel = true;
			if(mPopupWindow==null){
				initPopwindow();
				showPopwindow();
			}else{
				if(mPopupWindow.isShowing()){
					mPopupWindow.dismiss();
				}else{
					showPopwindow();
				}
			}
			
			// 全擦出
			// view.clean();
			break;
			
	//设置相别擦大小
		case R.id.earise_big:
			  m_sketchPad.setStrokeSize(40,SketchPadView.STROKE_ERASER);
			  m_sketchPad.setStrokeType(SketchPadView.STROKE_ERASER);
			  cleanBtn.setBackgroundResource(R.drawable.ico_eraser_big);
			  mPopupWindow.dismiss();
			  
			break;
		case R.id.earise_middle:
			  m_sketchPad.setStrokeSize(20,SketchPadView.STROKE_ERASER);
			  m_sketchPad.setStrokeType(SketchPadView.STROKE_ERASER);
			  cleanBtn.setBackgroundResource(R.drawable.ico_eraser_mid);
			  mPopupWindow.dismiss();
			break;
		case R.id.earise_small:
			  m_sketchPad.setStrokeSize(10,SketchPadView.STROKE_ERASER);
			  m_sketchPad.setStrokeType(SketchPadView.STROKE_ERASER);
			  cleanBtn.setBackgroundResource(R.drawable.ico_eraser_sml);
			  mPopupWindow.dismiss();
			break;
		case R.id.submit_button:
			showDialog();
			break;
		case R.id.cleanAllBtn:
			// view.clean();
			m_sketchPad.clearAllStrokes();
			mPopupWindow.dismiss();
			break;
		case R.id.bg_select_btn:
			if (isBlack) {
				m_sketchPad.setBkBitmap(((BitmapDrawable) (getResources()
						.getDrawable(R.drawable.bg_white))).getBitmap());
				changeBtn.setBackgroundResource(R.drawable.bg_cgbg_bb);
				isBlack = false;
			} else {
				m_sketchPad.setBkBitmap(((BitmapDrawable) (getResources()
						.getDrawable(R.drawable.bg))).getBitmap());
				changeBtn.setBackgroundResource(R.drawable.bg_cgbg_white);
				isBlack = true;
			}
			break;
		}
	}

	/**
	 * 初始化画笔
	 *
	 * @author hh
	 */
	public void initPaint(Bitmap bitmap) {
		if(m_sketchPad==null){
			m_sketchPad = new SketchPadView(DrawBoxActivity.this, null);
		}
		m_sketchPad.setCallback(DrawBoxActivity.this);
		line.removeAllViews();
		line.addView(m_sketchPad);
		m_sketchPad.setBkBitmap(bitmap);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		m_sketchPad.clearAllStrokes();
		if (bitmap != null) {
			if (!bitmap.isRecycled()) {
				bitmap.recycle();
				System.gc();
			}
		}
		BitmapUtil.setFree();
	}

	/**
	 * @param imageView
	 *            图片动画效果
	 */
	public void Translate(ImageView imageView, float index) {
		if (imageView == null) {
			return;
		}
		AnimationSet animationSet = new AnimationSet(true);
		TranslateAnimation translateAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, // X轴的开始位置
				Animation.RELATIVE_TO_SELF, 0f, // X轴的结束位置
				Animation.RELATIVE_TO_SELF, 0f, // Y轴的开始位置
				Animation.RELATIVE_TO_SELF, index); // Y轴的结束位置
		translateAnimation.setDuration(200);
		animationSet.addAnimation(translateAnimation);
		animationSet.setFillAfter(true);
		animationSet.setFillBefore(false);
		if (index < 0) {
			mImageView = imageView;
		}
		imageView.startAnimation(animationSet);
	}

	@Override
	public void onTouchDown(SketchPadView obj, MotionEvent event) {

	}

	@Override
	public void onTouchUp(SketchPadView obj, MotionEvent event) {

	}

	/**
	 * @return 保存图片
	 */
	private Bitmap getBitMap() {// 老师收作业的时候调用此方法保存图片 然后将图片传到服务器
		Bitmap bmBitmap = m_sketchPad.getCanvasSnapshot();
		m_sketchPad.cleanDrawingCache();
		return bmBitmap;
	}

	@Override
	public void onDestroy(SketchPadView obj) {

	}

	public void showDialog() {
		new AlertDialog.Builder(this).setTitle("确认").setMessage("确定提交作业吗？")
				.setPositiveButton("是", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						sendPaperRequest();
					}
				})
				.setNegativeButton("否", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
	}

	
	public void sendPaper(){
		handler.sendEmptyMessage(0);	
	}
	/**
	 * 提交作业
	 */
	public void submitPaper() {
		MessagePacking messagePacking = new MessagePacking(
				Message.MESSAGE_SAVE_PAPER);
		// 测试ID
		messagePacking.putBodyData(DataType.INT, BufferUtils
				.writeUTFString(MyApplication.getInstance().getQuizID()));
		// 设备ID
		messagePacking.putBodyData(DataType.INT, BufferUtils
				.writeUTFString(MyApplication.getInstance().getDeviceId()));
		// 图片
		Logger.debug(Utils.getTime()+TAG+"启动作业提交...");
		messagePacking.putBodyData(DataType.INT,BitmapUtil.bmpToByteArray(getBitMap(), true));
		Logger.debug(Utils.getTime()+TAG+"打包成功");
		CoreSocket.getInstance().sendMessage(messagePacking);
		Logger.debug(Utils.getTime()+TAG+"提交作业后锁定屏幕");
		startTask();
	}
	public void initPopwindow(){
		 line1=(LinearLayout) findViewById(R.id.line1);
		 LayoutInflater layoutInflater = LayoutInflater.from(this); 
	     View popupWindow = layoutInflater.inflate(R.layout.popup_window, null); 
	     mPopupWindow = new PopupWindow(popupWindow,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
	     mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
	     mPopupWindow.setOutsideTouchable(true);
	     earise_big=(ImageButton)popupWindow.findViewById(R.id.earise_big);
	     earise_big.setOnClickListener(this);
	     earise_middle=(ImageButton)popupWindow.findViewById(R.id.earise_middle);
	     earise_middle.setOnClickListener(this);
	     earise_small=(ImageButton)popupWindow.findViewById(R.id.earise_small);
	     earise_small.setOnClickListener(this);
	     delAllBtn = (ImageButton)popupWindow.findViewById(R.id.cleanAllBtn); // 清屏按钮
	     delAllBtn.setOnClickListener(this);
	}
	public void showPopwindow(){
		 int[] location = new int[2];
	     cleanBtn.getLocationOnScreen(location);
	     mPopupWindow.showAtLocation(cleanBtn, Gravity.NO_GRAVITY, location[0]-160, 570);
	   
	}
	
	
	/**
	 * 发送作业请求
	 */
	public void sendPaperRequest() {
		mProgressDialog.show();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("imei", MyApplication.deviceId);
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_SEND_PAPER);
		messagePacking.putBodyData(DataType.INT,BufferUtils.writeUTFString(jsonObject.toJSONString()));
		CoreSocket.getInstance().sendMessage(messagePacking);
		startTask();
	}
	
	
	public void closeProgressDialog(){
		if(mProgressDialog!=null&&mProgressDialog.isShowing()){
				mProgressDialog.dismiss();
		}
	}
	/**
	 * 进度条
	 */
	public void startTask(){
		 timeTimer = new Timer();
		 timeTimerTask = new TimerTask() {
			@Override
			public void run() {
					handler.sendEmptyMessage(1);
			}
		};
		timeTimer.schedule(timeTimerTask,10 * 1000);
	}
	public void cancleTask(){
		if(timeTimer!=null)
			timeTimer.cancel();
	}
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if(mProgressDialog!=null){
					mProgressDialog.show();
				}else{
					mProgressDialog = new ProgressiveDialog(DrawBoxActivity.this);
					mProgressDialog.setMessage(R.string.sendpaper_notice);
					mProgressDialog.show();
				}
				submitPaper();
				break;
			case 1:
				if(timeTimer!=null){
					timeTimer.cancel();
					mProgressDialog.dismiss();
					ToastHelper.showCustomToast(DrawBoxActivity.this, "作业提交失败，请重试");
				}
				break;
			default:
				break;
			}
		};
	};
}
