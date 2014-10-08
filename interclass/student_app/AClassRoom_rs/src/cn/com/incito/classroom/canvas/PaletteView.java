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

public class PaletteView extends SurfaceView implements Runnable,
        SurfaceHolder.Callback {

    private Paint mPaint = null;
    // 画板的坐标以及宽高
    private int bgBitmapX = 85;
    private int bgBitmapY = 15;
    private int bgBitmapHeight = 285;
    private int bgBitmapWidth = 223;
    // 画笔选项工具栏的坐标,宽高,每行和每列小框个数
    private int toolsX = 5;
    private int toolsY = 20;
    private int toolsSide = 34;
    private int toolsHeightNum = 4;
    private int toolsWidthNum = 2;
    // 笔粗细大小工具栏坐标，宽高
    private int sizeX = toolsX;
    private int sizeY = toolsY + toolsSide * toolsHeightNum + 20;
    private int sizeWidth = toolsSide * 2;
    private int sizeHeight = toolsSide;
    private int sizeNum = 3;
    // 已选颜色框坐标，正方形框的边长
    private int colorSelectedX = sizeX;
    private int colorSelectedY = sizeY + sizeHeight * sizeNum + 40;
    private int colorSelectedSide = 50;
    // 颜色选择板的坐标，每个正方形小框的边长,每行和每列小框个数
    private int colorX = colorSelectedX + colorSelectedSide;
    private int colorY = colorSelectedY;
    private int colorSide = colorSelectedSide / 2;
    private int colorWidthNum = 5;
    private int colorHeightNum = 2;
    // 后退工具框坐标,大小
    private int backX = colorX + colorWidthNum * colorSide + 20;
    private int backY = colorY + colorSide / 2;
    private int backWidth = 50;
    private int backHeight = colorSide;
    // 前进工具框坐标,大小
    private int forwardX = backX + backWidth;
    private int forwardY = backY;
    private int forwardWidth = backWidth;
    private int forwardHeight = backHeight;
    // 当前的已经选择的画笔参数
    private int currentPaintTool = 0; // 0~6
    private int currentColor = Color.BLACK;
    private int currentSize = 3; // 1,3,5
    private int currentPaintIndex = -1;
    private boolean isBackPressed = false;
    private boolean isForwardPressed = false;
    // 已选选择单元背景色
    private int selectedCellColor = 0xffADD8E6;
    // 存储所有的动作
    private ArrayList<Action> actionList = null;
    // 当前的画笔实例
    private Action curAction = null;
    // 线程结束标志位
    boolean mLoop = true;
    SurfaceHolder mSurfaceHolder = null;
    // 绘图区背景图片
    Bitmap bgBitmap = null;
    // 临时画板用来显示之前已经绘制过的图像
    Bitmap newbit = null;

    public PaletteView(Context context, AttributeSet arr) {
        super(context, arr);

        mPaint = new Paint();
        actionList = new ArrayList<Action>();
        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);
        this.setFocusable(true);
        mLoop = true;

        bgBitmap = ((BitmapDrawable) (getResources()
                .getDrawable(R.drawable.pic1))).getBitmap();
        newbit = Bitmap.createBitmap(bgBitmapWidth, bgBitmapHeight,
                Config.ARGB_4444);
        new Thread(this).start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return super.onKeyDown(keyCode, event);
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
            // 检测点击点是否在画笔选择区
            testTouchToolsPanel(touchX, touchY);
            // 检测点击点是否在画笔大小选择区
            testTouchSizePanel(touchX, touchY);
            // 检测点击点是否在颜色选择区
            testTouchColorPanel(touchX, touchY);
            // 检测点击点是否在按钮上
            testTouchButton(touchX, touchY);
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
            isBackPressed = false;
            isForwardPressed = false;
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
        canvas.drawColor(Color.WHITE);
        // 画主画板
        drawMainPallent(canvas);
        // 画工具栏
        drawToolsPanel(canvas);

        mSurfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void run() {
        while (mLoop) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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

    // 检测点击事件，是否在按钮上
    public boolean testTouchButton(float x, float y) {
        if (x > backX + 2 && y > backY + 2 && x < backX + backWidth - 2
                && y < backY + backHeight - 2) {
            if (isBackPressed) {
                return false;
            }
            if (currentPaintIndex >= 0) {
                currentPaintIndex--;
            }
            isBackPressed = true;
            return true;
        }
        if (x > forwardX + 2 && y > forwardY + 2
                && x < forwardX + forwardWidth - 2
                && y < forwardY + forwardHeight - 2) {
            if (isForwardPressed) {
                return false;
            }
            if ((currentPaintIndex + 1) < actionList.size()) {
                currentPaintIndex++;
            }
            isForwardPressed = true;
            return true;
        }

        return false;
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

    // 检测点击事件，是否在画笔类型选择区域
    public boolean testTouchToolsPanel(float x, float y) {
        if (x > toolsX && y > toolsY && x < toolsX + toolsSide * toolsWidthNum
                && y < toolsY + toolsSide * toolsHeightNum) {

            // 得到选择的画笔坐标
            int tx = (int) ((x - toolsX) / toolsSide);
            int ty = (int) ((y - toolsY) / toolsSide);
            // 设置当前画笔
            currentPaintTool = tx + ty * toolsWidthNum;
            return true;
        }

        return false;
    }

    // 检测点击事件，是否在画笔大小选择区域private int bgBitmapX;
    public boolean testTouchSizePanel(float x, float y) {
        if (x > sizeX && y > sizeY && x < sizeX + sizeWidth
                && y < sizeY + sizeHeight * sizeNum) {

            // 得到选择的画笔大小索引
            int index = (int) ((y - sizeY) / sizeHeight);
            // 设置当前画笔大小
            switch (index) {
                case 0:
                    currentSize = 1;
                    break;
                case 1:
                    currentSize = 3;
                    break;
                case 2:
                    currentSize = 5;
                    break;
                default:
                    currentSize = 1;
            }
            return true;
        }

        return false;
    }

    // 检测点击事件，是否在画笔大小选择区域
    public boolean testTouchColorPanel(float x, float y) {
        if (x > colorX && y > colorY && x < colorX + colorSide * colorWidthNum
                && y < colorY + colorSide * colorHeightNum) {

            // 得到选择的画笔颜色索引坐标
            int tx = (int) ((x - colorX) / colorSide);
            int ty = (int) ((y - colorY) / colorSide);
            int index = ty * colorWidthNum + tx;

            switch (index) {
                case 0:
                    currentColor = 0xff8B4513;
                    break;
                case 1:
                    currentColor = Color.RED;
                    break;
                case 2:
                    currentColor = Color.GREEN;
                    break;
                case 3:
                    currentColor = Color.BLUE;
                    break;
                case 4:
                    currentColor = Color.YELLOW;
                    break;
                case 5:
                    currentColor = Color.BLACK;
                    break;
                case 6:
                    currentColor = Color.MAGENTA;
                    break;
                case 7:
                    currentColor = Color.CYAN;
                    break;
                case 8:
                    currentColor = 0xffB0E0E6;
                    break;
                case 9:
                    currentColor = 0xffADFF2F;
                    break;
                default:
                    currentColor = Color.BLACK;
            }

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
                curAction = new MyLine(x, y, currentSize, currentColor);
                break;
            case 2:
                curAction = new MyRect(x, y, currentSize, currentColor);
                break;
            case 3:
                curAction = new MyCircle(x, y, currentSize, currentColor);
                break;
            case 4:
                curAction = new MyFillRect(x, y, currentSize, currentColor);
                break;
            case 5:
                curAction = new MyFillCircle(x, y, currentSize, currentColor);
                break;
            case 6:
                curAction = new MyEraser(x, y, currentSize, currentColor);
                break;
        }
    }

    // 画工具栏
    private void drawToolsPanel(Canvas canvas) {

        // 绘制画笔选项的工具栏边框
        drawPaintToolsPanel(canvas);

        // 绘制画笔粗细选项工具栏
        drawPaintSizePanel(canvas);

        // 绘制颜色
        drawPaintColorPanel(canvas);

        // 绘制前进后退按钮
        drawBackForwardPanel(canvas);
    }

    // 绘制前进后退按钮
    private void drawBackForwardPanel(Canvas canvas) {
        Paint paint = new Paint();

        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);

        int cellX = backX;
        int cellY = backY;
        int cellBX = backX + backWidth;
        int cellBY = backY + backHeight;
        // 绘制边框
        canvas.drawRect(cellX, cellY, cellBX, cellBY, paint);
        // 绘制按钮被点击时背景
        if (isBackPressed) {
            paint.setColor(selectedCellColor);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(cellX + 2, cellY + 2, cellBX - 2, cellBY - 2, paint);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
        }
        // 绘制边框中内容
        drawCellText(canvas, cellX, cellY, cellBX, cellBY, "后退");

        cellX = forwardX;
        cellY = forwardY;
        cellBX = forwardX + forwardWidth;
        cellBY = forwardY + forwardHeight;

        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        // 绘制边框
        canvas.drawRect(cellX, cellY, cellBX, cellBY, paint);
        // 绘制按钮被点击时背景
        if (isForwardPressed) {
            paint.setColor(selectedCellColor);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(cellX + 2, cellY + 2, cellBX - 2, cellBY - 2, paint);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
        }
        // 绘制边框中内容
        drawCellText(canvas, cellX, cellY, cellBX, cellBY, "前进");

    }

    // 绘制画笔选项的工具栏
    private void drawPaintToolsPanel(Canvas canvas) {
        Paint paint = new Paint();

        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);

        // 绘制画笔选项的工具栏边框
        for (int i = 0; i < toolsWidthNum; i++)
            for (int j = 0; j < toolsHeightNum; j++) {
                int cellX = toolsX + i * toolsSide;
                int cellY = toolsY + j * toolsSide;
                int cellBX = toolsX + (i + 1) * toolsSide;
                int cellBY = toolsY + (j + 1) * toolsSide;
                int paintTool = j * toolsWidthNum + i;
                // 绘制边框
                canvas.drawRect(cellX, cellY, cellBX, cellBY, paint);
                // 绘制已选工具背景
                if (paintTool == currentPaintTool) {
                    paint.setColor(selectedCellColor);
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawRect(cellX + 2, cellY + 2, cellBX - 2,
                            cellBY - 2, paint);
                    paint.setColor(Color.BLACK);
                    paint.setStyle(Paint.Style.STROKE);
                }
                // 绘制边框中内容
                drawToolsText(canvas, cellX, cellY, cellBX, cellBY, paintTool);
            }
    }

    // 绘制画笔大小选项的工具栏
    private void drawPaintSizePanel(Canvas canvas) {
        Paint paint = new Paint();

        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        // 绘制画笔粗细选项工具栏
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        for (int i = 0; i < sizeNum; i++) {
            int cellX = sizeX;
            int cellY = sizeY + i * sizeHeight;
            int cellBX = sizeX + sizeWidth;
            int cellBY = sizeY + (i + 1) * sizeHeight;
            int toolSize = i * 2 + 1;
            // 绘制边框
            canvas.drawRect(cellX, cellY, cellBX, cellBY, paint);
            // 绘制已选单元背景
            if (toolSize == currentSize) {
                paint.setColor(selectedCellColor);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawRect(cellX + 2, cellY + 2, cellBX - 2, cellBY - 2,
                        paint);
                paint.setColor(Color.BLACK);
                paint.setStyle(Paint.Style.STROKE);
            }
            // 绘制内容
            drawSizeText(canvas, cellX, cellY, cellBX, cellBY, toolSize);
        }
    }

    // 绘制画笔颜色选项的工具栏
    private void drawPaintColorPanel(Canvas canvas) {
        Paint paint = new Paint();

        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);

        // 绘制已选颜色边框
        canvas.drawRect(colorSelectedX, colorSelectedY, colorSelectedX
                + colorSelectedSide, colorSelectedY + colorSelectedSide, paint);
        // 绘制已选颜色
        paint.setColor(currentColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(colorSelectedX + 10, colorSelectedY + 10,
                colorSelectedX + colorSelectedSide - 10, colorSelectedY
                        + colorSelectedSide - 10, paint
        );
        // 绘制备选颜色边框以及其中颜色
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        for (int i = 0; i < colorWidthNum; i++)
            for (int j = 0; j < colorHeightNum; j++) {
                int cellX = colorX + i * colorSide;
                int cellY = colorY + j * colorSide;
                int cellBX = colorX + (i + 1) * colorSide;
                int cellBY = colorY + (j + 1) * colorSide;
                int paintColor = i + j * colorWidthNum;
                // 绘制单元边框
                canvas.drawRect(cellX, cellY, cellBX, cellBY, paint);
                // 绘制单元中颜色
                drawColorText(canvas, cellX, cellY, cellBX, cellBY, paintColor);
            }
    }

    // 绘制画笔选项的工具栏边框中内容
    private void drawToolsText(Canvas canvas, int cellX, int cellY, int cellBX,
                               int cellBY, int paintTool) {
        switch (paintTool) {
            case 0:
                drawCellText(canvas, cellX, cellY, cellBX, cellBY, "铅");
                break;
            case 1:
                drawCellText(canvas, cellX, cellY, cellBX, cellBY, "直");
                break;
            case 2:
                drawCellText(canvas, cellX, cellY, cellBX, cellBY, "方");
                break;
            case 3:
                drawCellText(canvas, cellX, cellY, cellBX, cellBY, "圆");
                break;
            case 4:
                drawCellText(canvas, cellX, cellY, cellBX, cellBY, "块");
                break;
            case 5:
                drawCellText(canvas, cellX, cellY, cellBX, cellBY, "饼");
                break;
            case 6:
                drawCellText(canvas, cellX, cellY, cellBX, cellBY, "橡");
                break;
        }
    }

    // 绘制画笔大小选项的工具栏边框中内容
    private void drawSizeText(Canvas canvas, int cellX, int cellY, int cellBX,
                              int cellBY, int toolSize) {
        switch (toolSize) {
            case 1:
                drawCellText(canvas, cellX, cellY, cellBX, cellBY, "1dip");
                break;
            case 3:
                drawCellText(canvas, cellX, cellY, cellBX, cellBY, "3dip");
                break;
            case 5:
                drawCellText(canvas, cellX, cellY, cellBX, cellBY, "5dip");
                break;
        }
    }

    // 绘制画笔大小选项的工具栏边框中内容
    private void drawColorText(Canvas canvas, int cellX, int cellY, int cellBX,
                               int cellBY, int paintColor) {
        switch (paintColor) {
            case 0:
                drawCellColor(canvas, cellX, cellY, cellBX, cellBY, 0xff8B4513);
                break;
            case 1:
                drawCellColor(canvas, cellX, cellY, cellBX, cellBY, Color.RED);
                break;
            case 2:
                drawCellColor(canvas, cellX, cellY, cellBX, cellBY, Color.GREEN);
                break;
            case 3:
                drawCellColor(canvas, cellX, cellY, cellBX, cellBY, Color.BLUE);
                break;
            case 4:
                drawCellColor(canvas, cellX, cellY, cellBX, cellBY, Color.YELLOW);
                break;
            case 5:
                drawCellColor(canvas, cellX, cellY, cellBX, cellBY, Color.BLACK);
                break;
            case 6:
                drawCellColor(canvas, cellX, cellY, cellBX, cellBY, Color.MAGENTA);
                break;
            case 7:
                drawCellColor(canvas, cellX, cellY, cellBX, cellBY, Color.CYAN);
                break;
            case 8:
                drawCellColor(canvas, cellX, cellY, cellBX, cellBY, 0xffB0E0E6);
                break;
            case 9:
                drawCellColor(canvas, cellX, cellY, cellBX, cellBY, 0xffADFF2F);
                break;
            default:
                drawCellColor(canvas, cellX, cellY, cellBX, cellBY, Color.BLACK);
                break;
        }
    }

    // 绘制单元格中的文字
    private void drawCellText(Canvas canvas, int cellX, int cellY, int cellBX,
                              int cellBY, String text) {
        Paint paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);
        paint.setTextSize((cellBY - cellY) / 4 * 3);
        int textX = cellX + (cellBX - cellX) / 5;
        int textY = cellBY - (cellBY - cellY) / 5;
        canvas.drawText(text, textX, textY, paint);
    }

    // 绘制单元格中的颜色
    private void drawCellColor(Canvas canvas, int cellX, int cellY, int cellBX,
                               int cellBY, int color) {
        Paint paint = new Paint();
        // 绘制备选颜色边框以及其中颜色
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(cellX + 4, cellY + 4, cellBX - 4, cellBY - 4, paint);
    }

    // 画主画板
    private void drawMainPallent(Canvas canvas) {
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

    public void setBgBitmap(Bitmap bgBitmap) {
        Matrix mMatrix = new Matrix();
        mMatrix.reset();
        float btHeight = bgBitmap.getWidth();
        float btWidth = bgBitmap.getHeight();
        float xScale = bgBitmapWidth / btHeight;
        float yScale = bgBitmapHeight / btWidth;
        mMatrix.postScale(xScale, yScale);
        this.bgBitmap = Bitmap.createBitmap(bgBitmap, 0, 0, bgBitmapWidth,
                bgBitmapHeight, mMatrix, true);
    }

    // 后退前进完成后，缓存的动作
    private void clearSpareAction() {
        for (int i = actionList.size() - 1; i > currentPaintIndex; i--) {
            actionList.remove(i);
        }
    }
}
