package cn.com.incito.classroom.canvas;

import java.util.ArrayList;

import cn.com.incito.classroom.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PaletteViewWidget extends SurfaceView implements Runnable,
		SurfaceHolder.Callback {
	private static final int EARISE_SIZE = 30;// 橡皮擦大小
	public Paint mPaint = null;
	// 画板的坐标以及宽高
	public int bgBitmapX = 0;
	public int bgBitmapY = 0;
	public int bgBitmapHeight = 800;
	public int bgBitmapWidth = 1280;
	// 当前的已经选择的画笔参数
	public int currentPaintTool = 0; // 画笔类型分为0和1 0为画线，1为橡皮擦
	public int currentColor = Color.BLACK;//默认画笔颜色
	public int currentSize = 3; // 1,3,5 画笔粗细
	public int currentPaintIndex = -1;
	// 存储所有的动作
	public ArrayList<Action> actionList = null;
	// 当前的画笔实例
	public Action curAction = null;
	// 线程结束标志位
	public boolean mLoop = true;

	SurfaceHolder mSurfaceHolder = null;
	// 绘图区背景图片
	Bitmap bgBitmap = null;

	// 临时画板用来显示之前已经绘制过的图像
	Bitmap newbit = null;

	public PaletteViewWidget(Context context, AttributeSet arr) {
		super(context, arr);
		mLoop = true;
		mPaint = new Paint();
		actionList = new ArrayList<Action>();
		mSurfaceHolder = this.getHolder();
		mSurfaceHolder.addCallback(this);
		this.setFocusable(true);
		bgBitmap = ((BitmapDrawable) (getResources().getDrawable(R.drawable.bg)))
				.getBitmap();
		newbit = Bitmap.createBitmap(bgBitmapWidth, bgBitmapHeight,
				Config.ARGB_4444);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Draw();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int antion = event.getAction();
		if (antion == MotionEvent.ACTION_CANCEL) {
			return false;
		}

		float touchX = event.getX();
		float touchY = event.getY();

		// 点击时
		if (antion == MotionEvent.ACTION_DOWN) {
			// 检测点击点是否在主绘图区
			if (testTouchMainPallent(touchX, touchY)) {
				setCurAction(getRealX(touchX), getRealY(touchY));
				clearSpareAction();
			}
		}
		// 拖动时
		if (antion == MotionEvent.ACTION_MOVE) {
			if (curAction != null) {
				curAction.move(getRealX(touchX), getRealY(touchY));
			}
		}
		// 抬起时
		if (antion == MotionEvent.ACTION_UP) {
			if (curAction != null) {
				curAction.move(getRealX(touchX), getRealY(touchY));
				actionList.add(curAction);
				currentPaintIndex++;
				curAction = null;
			}
			// isBackPressed = false;
			// isForwardPressed = false;
		}
		return super.onTouchEvent(event);
	}

	// 绘图
	protected void Draw() {
		Canvas canvas = mSurfaceHolder.lockCanvas();
		if (mSurfaceHolder == null || canvas == null) {
			return;
		}
		// 填充背景
		// canvas.drawColor(Color.GREEN);
		// 画主画板
		drawMainPallent(canvas);
		mSurfaceHolder.unlockCanvasAndPost(canvas);
	}

	@Override
	public void run() {
		while (mLoop) {
//			try {
//				Thread.sleep(50);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
			synchronized (mSurfaceHolder) {
				Draw();
			}
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		new Thread(this).start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mLoop = false;
	}

	// 检测点击事件，是否在主绘图区
	public boolean testTouchMainPallent(float x, float y) {
		if (x > bgBitmapX + 2 && y > bgBitmapY + 2
				&& x < bgBitmapX + bgBitmapWidth - 2
				&& y < bgBitmapY + bgBitmapHeight - 2) {
			return true;
		}

		return false;
	}

	// 得到当前画笔的类型，并进行实例
	public void setCurAction(float x, float y) {
		switch (currentPaintTool) {
		case 0:
			curAction = new MyPath(x, y, currentSize, currentColor);
			break;
		case 1:
			curAction = new MyEraser(x, y, EARISE_SIZE, currentColor);
			break;
		}
	}

	// 画主画板
	public void drawMainPallent(Canvas canvas) {
		// 设置画笔没有锯齿，空心
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.STROKE);
		// 画板绘图区背景图片
		canvas.drawBitmap(bgBitmap, bgBitmapX, bgBitmapY, null);
		newbit = Bitmap.createBitmap(bgBitmapWidth, bgBitmapHeight,
				Config.ARGB_4444);
		Canvas canvasTemp = new Canvas(newbit);
		canvasTemp.drawColor(Color.TRANSPARENT);
		for (int i = 0; i <= currentPaintIndex; i++) {
			actionList.get(i).draw(canvasTemp);
		}
		// 画当前画笔痕迹
		if (curAction != null) {
			curAction.draw(canvasTemp);
		}

		// 在主画板上绘制临时画布上的图像
		canvas.drawBitmap(newbit, bgBitmapX, bgBitmapY, null);
		// 画板绘图区边框
		mPaint.setColor(Color.BLACK);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(3);
		canvas.drawRect(bgBitmapX - 2, bgBitmapY - 2, bgBitmapX + bgBitmapWidth
				+ 2, bgBitmapY + bgBitmapHeight + 2, mPaint);
	}

	// 根据接触点x坐标得到画板上对应x坐标
	public float getRealX(float x) {

		return x - bgBitmapX;
	}

	// 根据接触点y坐标得到画板上对应y坐标
	public float getRealY(float y) {

		return y - bgBitmapY;
	}

	public void setBgBitmap(Bitmap bgBitmap) {//设置背景图片
		Matrix mMatrix = new Matrix();
		mMatrix.reset();
		float btWidth = bgBitmap.getWidth();
		if(btWidth>1280){
			btWidth=1280;
		}
		float btHeight = bgBitmap.getHeight();
		if(btHeight>800){
			btHeight=800;
		}
		float xScale = bgBitmapWidth / btWidth;
		float yScale = bgBitmapHeight / btHeight;
		mMatrix.postScale(xScale, yScale);
		this.bgBitmap = Bitmap.createBitmap(bgBitmap, 0, 0, (int)btWidth,
				(int)btHeight, mMatrix, true);
	}

	// 后退前进完成后，缓存的动作
	public void clearSpareAction() {
		for (int i = actionList.size() - 1; i > currentPaintIndex; i--) {
			actionList.remove(i);
		}
	}

	public void setCurrentSize(int currentSize) {
		this.currentSize = currentSize;
	}

	public void setCurrentPaintTool(int currentPaintTool) {
		this.currentPaintTool = currentPaintTool;
	}

	public void setCurrentColor(int currentColor) {
		this.currentColor = currentColor;
	}

	public void setmLoop(boolean mLoop) {
		this.mLoop = mLoop;
	}
}
