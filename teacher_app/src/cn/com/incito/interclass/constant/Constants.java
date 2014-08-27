package cn.com.incito.interclass.constant;

/**
 * Created with IntelliJ IDEA.
 * User: popoy
 * Date: 14-8-15
 * Time: 下午3:12
 * To change this template use File | Settings | File Templates.
 */
public class Constants {

	//文件锁存储位置
    public static final String LOC_FILE = "lock.txt";
    public static final String PROPERTIES_FILE = "parameter.properties";

//    public static int STATE_NORMAL = 0;     //0正常
    public static int STATE_QUIZING = 1;      //1课堂练习中
    public static int STATE_GROUPING = 2;     //2分组中
    public static int STATE_SCREEN_LOCKING = 3;     //3 锁屏中
    public static int STATE_WAITING = 4;     //3 准备上课
    public static int STATE_PROCESSING = 5;     //5 正在上课
}