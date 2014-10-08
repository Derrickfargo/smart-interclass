package cn.com.incito.server.config;

import java.io.File;

import cn.com.incito.server.utils.FileUtils;

/**
 * Created with IntelliJ IDEA. User: popoy Date: 14-8-15 Time: 下午3:12 To change
 * this template use File | Settings | File Templates.
 */
public class Constants {

	// 文件锁存储位置
	public static final String LOC_FILE = "lock.txt";
	public static final String PROPERTIES_FILE = "parameter.properties";
	public static final String MESSAGE_QUIZ = "<html><font size='6' face='arial' color='#FFFFFF'><strong>%d</strong></font><font size='4'> / </font><font size='4' face='arial' color='#09689d'><strong>%d</strong></font></html>";
	// 随堂练习存储参数
	public final static String PAPER_PATH = FileUtils.getProjectPath() + File.separator + "paper";
	// public static int STATE_NORMAL = 0; //0正常
	public static int STATE_QUIZING = 1; // 1课堂练习中
	public static int STATE_GROUPING = 2; // 2分组中
	public static int STATE_SCREEN_LOCKING = 3; // 3 锁屏中
	public static int STATE_WAITING = 4; // 3 准备上课
	public static int STATE_PROCESSING = 5; // 5 正在上课
}