package cn.com.incito.classroom.ui.activity;

import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.canvas.PaletteViewWidget;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class DrawBoxActivity extends BaseActivity implements OnClickListener {
	private PaletteViewWidget iv;
	private Button mbackcolorBtn;
	private ImageView mWhiteIco;
	private ImageView mCanaryYellowIco;
	private ImageView mEarise;
	private ImageView mLemonYellow;
	private ImageView mSalmonOrange;
	private ImageView mSpanishOrange;
	private ImageView mMineralOrange;
	private ImageView mMagenta;
	private ImageView mHotInk;
	private ImageView mCarmineRed;
	private ImageView mPowderBlue;
	private ImageView mTurquoise;
	private ImageView mPeacpckBlue;
	private ImageView mDarkPurple;
	private ImageView mDarkGreen;
	private ImageView mMossGreen;
	private ImageView mParrotGreen;
	private ImageView mChocolate;
	private ImageView mBlack;
	private ImageView mGrey;
	private RelativeLayout mRelativeLayout;
	private boolean isBlack = true;
	private LinearLayout mLinearLayout;
	private RelativeLayout mBigRelativeLayout;
	private RelativeLayout mMidRelativeLayout;
	private RelativeLayout mSmallRelativeLayout;
	private ImageView mBigCheckIco;
	private ImageView mMiddleCheckIco;
	private ImageView mSmallCheckIco;
	private ImageView mImageView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.draw_box);
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		iv.setmLoop(true);// 重新进去界面后 重启线程进行绘图
	}

	public void initView() {
		mLinearLayout = (LinearLayout) findViewById(R.id.line);
		mRelativeLayout = (RelativeLayout) findViewById(R.id.main);
		this.iv = (PaletteViewWidget) this.findViewById(R.id.iv);

		// 背景色选择按钮
		mbackcolorBtn = (Button) findViewById(R.id.bg_select_btn);
		mbackcolorBtn.setOnClickListener(this);

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

		// 橡皮擦
		mEarise = (ImageView) findViewById(R.id.earise);
		mEarise.setOnClickListener(this);

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

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return iv.onTouchEvent(event);

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
			iv.setCurrentPaintTool(0);
			iv.setCurrentColor(getResources().getColor(android.R.color.white));
			break;
		case R.id.color_canary_yellow:
			Translate(mCanaryYellowIco, -0.25f);
			iv.setCurrentPaintTool(0);
			iv.setCurrentColor(getResources().getColor(R.color.canary_yellow));
			break;
		case R.id.ico_lemon_yellow:
			Translate(mLemonYellow, -0.25f);
			iv.setCurrentPaintTool(0);
			iv.setCurrentColor(getResources().getColor(R.color.lemon_yellow));
			break;
		case R.id.ico_salmon_orange:
			Translate(mSalmonOrange, -0.25f);
			iv.setCurrentPaintTool(0);
			iv.setCurrentColor(getResources().getColor(R.color.salmon_orange));
			break;
		case R.id.ico_spanish_orange:
			Translate(mSpanishOrange, -0.25f);
			iv.setCurrentPaintTool(0);
			iv.setCurrentColor(getResources().getColor(R.color.spanish_orange));
			break;
		case R.id.ico_mineral_orange:
			Translate(mMineralOrange, -0.25f);
			iv.setCurrentPaintTool(0);
			iv.setCurrentColor(getResources().getColor(R.color.mineral_orange));
			break;
		case R.id.ico_carmine_red:
			Translate(mCarmineRed, -0.25f);
			iv.setCurrentPaintTool(0);
			iv.setCurrentColor(getResources().getColor(R.color.carmine_red));
			break;
		case R.id.ico_hot_ink:
			Translate(mHotInk, -0.25f);
			iv.setCurrentPaintTool(0);
			iv.setCurrentColor(getResources().getColor(R.color.hot_ink));
			break;
		case R.id.ico_magenta:
			Translate(mMagenta, -0.25f);
			iv.setCurrentPaintTool(0);
			iv.setCurrentColor(getResources().getColor(R.color.magenta));
			break;
		case R.id.ico_powder_blue:
			Translate(mPowderBlue, -0.25f);
			iv.setCurrentPaintTool(0);
			iv.setCurrentColor(getResources().getColor(R.color.powder_blue));
			break;
		case R.id.ico_turquoise:
			Translate(mTurquoise, -0.25f);
			iv.setCurrentPaintTool(0);
			iv.setCurrentColor(getResources().getColor(R.color.turquoise));
			break;
		case R.id.ico_peacpck_blue:
			Translate(mPeacpckBlue, -0.25f);
			iv.setCurrentPaintTool(0);
			iv.setCurrentColor(getResources().getColor(R.color.peacpck_blue));
			break;
		case R.id.ico_dark_purple:
			Translate(mDarkPurple, -0.25f);
			iv.setCurrentPaintTool(0);
			iv.setCurrentColor(getResources().getColor(R.color.dark_purple));
			break;
		case R.id.ico_dark_green:
			Translate(mDarkGreen, -0.25f);
			iv.setCurrentPaintTool(0);
			iv.setCurrentColor(getResources().getColor(R.color.dark_green));
			break;
		case R.id.ico_moss_green:
			Translate(mMossGreen, -0.25f);
			iv.setCurrentPaintTool(0);
			iv.setCurrentColor(getResources().getColor(R.color.moss_green));
			break;
		case R.id.ico_parrot_green:
			Translate(mParrotGreen, -0.25f);
			iv.setCurrentPaintTool(0);
			iv.setCurrentColor(getResources().getColor(R.color.parrot_green));
			break;
		case R.id.ico_chocolate:
			Translate(mChocolate, -0.25f);
			iv.setCurrentPaintTool(0);
			iv.setCurrentColor(getResources().getColor(R.color.chocolate));
			break;
		case R.id.ico_black:
			Translate(mBlack, -0.25f);
			iv.setCurrentPaintTool(0);
			iv.setCurrentColor(getResources().getColor(R.color.black));
			break;
		case R.id.ico_grey:
			Translate(mGrey, -0.25f);
			iv.setCurrentPaintTool(0);
			iv.setCurrentColor(getResources().getColor(R.color.grey));
			break;
		case R.id.earise:// 橡皮擦
			iv.setCurrentPaintTool(1);
			break;
		case R.id.bg_select_btn:// 背景选择
			if (isBlack) {
				mRelativeLayout.setBackgroundResource(R.drawable.bg_white);
				iv.setBgBitmap(((BitmapDrawable) (getResources()
						.getDrawable(R.drawable.bg_white))).getBitmap());
				mbackcolorBtn.setBackgroundResource(R.drawable.bg_cgbg_bb);
				isBlack = false;
			} else {
				mRelativeLayout.setBackgroundResource(R.drawable.bg);
				iv.setBgBitmap(((BitmapDrawable) (getResources()
						.getDrawable(R.drawable.bg))).getBitmap());
				mbackcolorBtn.setBackgroundResource(R.drawable.bg_cgbg_white);
				isBlack = true;
			}
			break;

		case R.id.big:// 粗笔
			iv.setCurrentSize(5);
			mBigCheckIco.setVisibility(View.VISIBLE);
			mMiddleCheckIco.setVisibility(View.GONE);
			mSmallCheckIco.setVisibility(View.GONE);
			break;
		case R.id.middle:// 中等笔
			iv.setCurrentSize(3);
			mBigCheckIco.setVisibility(View.GONE);
			mMiddleCheckIco.setVisibility(View.VISIBLE);
			mSmallCheckIco.setVisibility(View.GONE);
			break;
		case R.id.small:// 细笔
			iv.setCurrentSize(1);
			mBigCheckIco.setVisibility(View.GONE);
			mMiddleCheckIco.setVisibility(View.GONE);
			mSmallCheckIco.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
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

}