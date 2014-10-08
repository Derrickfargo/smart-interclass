package cn.com.incito.classroom.constants;

import java.util.ArrayList;

import android.graphics.Bitmap;
import cn.com.incito.classroom.widget.canvas.Action;
import cn.com.incito.classroom.widget.canvas.ISketchPadTool;

/**
 * Created by JOHN on 2014/7/23.
 */
public class Constant {
    public static final int PORT = 9001;
    public static final String IP = "192.168.30.135";
    /**
     * 小组相关的intent传值名
     */
    public static final String GROUP_INTENT_BUNDLE_NAME = "groupInfo" ;
    // 绘画板常量
 	public static ArrayList<String> LIST = null;
 	public static ArrayList<Action> actionList = new ArrayList<Action>();
 	public static ArrayList<ISketchPadTool> m_undoStack = new ArrayList<ISketchPadTool>();
 	public static Bitmap bitmap;
 	public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

 	public final static int LARGE_PEN_WIDTH = 15;
 	public final static int MIDDLE_PEN_WIDTH = 10;
 	public final static int SMALL_PEN_WIDTH = 5;
    
}
