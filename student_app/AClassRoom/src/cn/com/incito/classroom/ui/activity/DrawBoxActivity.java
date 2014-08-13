package cn.com.incito.classroom.ui.activity;

import java.io.ByteArrayOutputStream;





import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.classroom.utils.BitmapUtil;
import cn.com.incito.classroom.widget.canvas.ISketchPadCallback;
import cn.com.incito.classroom.widget.canvas.SketchPadView;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.CoreSocket;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;
import cn.com.incito.wisdom.sdk.utils.BitmapUtils;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class DrawBoxActivity extends BaseActivity implements OnClickListener,
		ISketchPadCallback {

	private ImageView cleanBtn;
	private Button delAllBtn;
	private FrameLayout line;
	private SketchPadView m_sketchPad = null;
	private Button changeBtn;
	private ImageView mWhiteIco;
	private ImageView mCanaryYellowIco;
	private ImageView mLemonYellow;
	private ImageView mSpanishOrange;
	private ImageView mMineralOrange;
	private ImageView mCarmineRed;
	private ImageView mHotInk;
	private ImageView mSalmonOrange;
	private ImageView mMagenta;
	private ImageView mPowderBlue;
	private ImageView mTurquoise;
	private ImageView mPeacpckBlue;
	private ImageView mDarkPurple;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.getWindow().setFlags(Constants.FLAG_HOMEKEY_DISPATCHED,
				Constants.FLAG_HOMEKEY_DISPATCHED);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.draw_box);
		UIHelper.getInstance().setDrawBoxActivity(this);
		findView();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	/**
	 * 找到控件
	 * 
	 * @author hefeng
	 * @version 创建时间：2013-10-11 上午10:59:59
	 */
	private void findView() {
		// 颜色选择按钮
		mWhiteIco = (ImageView) findViewById(R.id.color_white);
		mWhiteIco.setOnClickListener(this);
		mCanaryYellowIco = (ImageView) findViewById(R.id.color_canary_yellow);
		mCanaryYellowIco.setOnClickListener(this);
		mLemonYellow = (ImageView) findViewById(R.id.ico_lemon_yellow);
		mLemonYellow.setOnClickListener(this);
		mSalmonOrange = (ImageView) findViewById(R.id.ico_salmon_orange);
		mSalmonOrange.setOnClickListener(this);
		mSpanishOrange = (ImageView) findViewById(R.id.ico_spanish_orange);
		mSpanishOrange.setOnClickListener(this);
		mMineralOrange = (ImageView) findViewById(R.id.ico_mineral_orange);
		mMineralOrange.setOnClickListener(this);
		mCarmineRed = (ImageView) findViewById(R.id.ico_carmine_red);
		mCarmineRed.setOnClickListener(this);
		mHotInk = (ImageView) findViewById(R.id.ico_hot_ink);
		mHotInk.setOnClickListener(this);
		mMagenta = (ImageView) findViewById(R.id.ico_magenta);
		mMagenta.setOnClickListener(this);
		mPowderBlue = (ImageView) findViewById(R.id.ico_powder_blue);
		mPowderBlue.setOnClickListener(this);
		mTurquoise = (ImageView) findViewById(R.id.ico_turquoise);
		mTurquoise.setOnClickListener(this);
		mPeacpckBlue = (ImageView) findViewById(R.id.ico_peacpck_blue);
		mPeacpckBlue.setOnClickListener(this);
		mDarkPurple = (ImageView) findViewById(R.id.ico_dark_purple);
		mDarkPurple.setOnClickListener(this);
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
		delAllBtn = (Button) findViewById(R.id.cleanAllBtn); // 清屏按钮
		delAllBtn.setOnClickListener(this);
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
		mSubmitButton=(Button)findViewById(R.id.submit_button);
		mSubmitButton.setOnClickListener(this);
		m_sketchPad = (SketchPadView) findViewById(R.id.sketchpad);
		m_sketchPad.setCallback(DrawBoxActivity.this);
		Bitmap bitmap = null;
		byte[] paper = getIntent().getExtras().getByteArray("paper");
		if (paper != null) {
			ByteArrayOutputStream outPut = new ByteArrayOutputStream();
			bitmap = BitmapFactory.decodeByteArray(paper, 0, paper.length);
			bitmap.compress(CompressFormat.PNG, 100, outPut);
			changeBtn.setClickable(false);
		} else {
			 bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.bg);
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
			Translate(mWhiteIco, -0.25f);
			m_sketchPad.setStrokeType(SketchPadView.STROKE_PEN);
			m_sketchPad.setStrokeColor(getResources().getColor(
					android.R.color.white));
			break;
		case R.id.color_canary_yellow:
			Translate(mCanaryYellowIco, -0.25f);
			m_sketchPad.setStrokeType(SketchPadView.STROKE_PEN);
			m_sketchPad.setStrokeColor(getResources().getColor(
					R.color.canary_yellow));
			break;
		case R.id.ico_lemon_yellow:
			Translate(mLemonYellow, -0.25f);
			m_sketchPad.setStrokeType(SketchPadView.STROKE_PEN);
			m_sketchPad.setStrokeColor(getResources().getColor(
					R.color.lemon_yellow));
			break;
		case R.id.ico_salmon_orange:
			Translate(mSalmonOrange, -0.25f);
			m_sketchPad.setStrokeType(SketchPadView.STROKE_PEN);
			m_sketchPad.setStrokeColor(getResources().getColor(
					R.color.salmon_orange));
			break;
		case R.id.ico_spanish_orange:
			Translate(mSpanishOrange, -0.25f);
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
			Translate(mPowderBlue, -0.25f);
			m_sketchPad.setStrokeType(SketchPadView.STROKE_PEN);
			m_sketchPad.setStrokeColor(getResources().getColor(
					R.color.powder_blue));
			break;
		case R.id.ico_turquoise:
			Translate(mTurquoise, -0.25f);
			m_sketchPad.setStrokeType(SketchPadView.STROKE_PEN);
			m_sketchPad.setStrokeColor(getResources().getColor(
					R.color.turquoise));
			break;
		case R.id.ico_peacpck_blue:
			Translate(mPeacpckBlue, -0.25f);
			m_sketchPad.setStrokeType(SketchPadView.STROKE_PEN);
			m_sketchPad.setStrokeColor(getResources().getColor(
					R.color.peacpck_blue));
			break;
		case R.id.ico_dark_purple:
			Translate(mDarkPurple, -0.25f);
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
			m_sketchPad.setStrokeType(SketchPadView.STROKE_ERASER);
			// 全擦出
			// view.clean();
			break;
		case R.id.submit_button:
			
			showDialog();
			break;
		case R.id.cleanAllBtn:
			// view.clean();
			m_sketchPad.clearAllStrokes();
			break;
		case R.id.bg_select_btn:
			if (isBlack) {
				m_sketchPad.setBkBitmap(((BitmapDrawable) (getResources().getDrawable(R.drawable.bg_white))).getBitmap());
				changeBtn.setBackgroundResource(R.drawable.bg_cgbg_bb);
				isBlack = false;
			} else {
				m_sketchPad.setBkBitmap(((BitmapDrawable) (getResources().getDrawable(R.drawable.bg))).getBitmap());
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
	 * 
	 */
	public void initPaint(Bitmap bitmap) {
		m_sketchPad = new SketchPadView(DrawBoxActivity.this, null);
		m_sketchPad.setCallback(DrawBoxActivity.this);
		line.removeAllViews();
		line.addView(m_sketchPad);
		m_sketchPad.setBkBitmap(bitmap);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		m_sketchPad.clearAllStrokes();
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

	@Override
	public void onDestroy(SketchPadView obj) {

	}
	
	public void showDialog(){
		new AlertDialog.Builder(this) .setTitle("确认")
	 	.setMessage("确定提交作业吗？")
	 	.setPositiveButton("是", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				submitPaper();
			}
		})
	 	.setNegativeButton("否", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
	 	}).show();
	}
	/**
	 * @return 保存图片
	 */
	private Bitmap getBitMap() {// 老师收作业的时候调用此方法保存图片 然后将图片传到服务器
		Bitmap bmBitmap = m_sketchPad.getCanvasSnapshot();
		BitmapUtil.saveBitmapToSdCard(bmBitmap, "/mnt/sdcard/aa.jpg");
		m_sketchPad.cleanDrawingCache();
		return bmBitmap;
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
		// 小组姓名
		messagePacking.putBodyData(
				DataType.INT,
				BufferUtils.writeUTFString(MyApplication.getInstance()
						.getLoginResVo().getName()));
		// 图片
		messagePacking.putBodyData(DataType.INT,
				BitmapUtils.bmpToByteArray(getBitMap(), true));
		CoreSocket.getInstance().sendMessage(messagePacking);
		this.finish();
	}
}
