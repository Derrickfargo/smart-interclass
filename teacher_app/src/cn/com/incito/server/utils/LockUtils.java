package cn.com.incito.server.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * Created with IntelliJ IDEA.
 * User: popoy
 * Date: 14-8-15
 * Time: 下午12:25
 * To change this template use File | Settings | File Templates.
 */
public class LockUtils {
    public static final String LOC_DIR = "E:\\";
    public static final String LOC_FILE = "lock.txt";

    /**
     * 判定文件是否处在加锁状态
     *
     * @return
     */
    public static boolean isFileLocked() {
        File locFile = new File(LOC_DIR, LOC_FILE);
        FileOutputStream raf;
        FileLock fl = null;
        boolean islocked = false;
        try {
            if (!locFile.exists()) {
                locFile.createNewFile();
            }
            raf = new FileOutputStream(locFile);
            FileChannel fc = raf.getChannel(); //获取FileChannel对象
            fl = fc.tryLock();  //or fc.lock();
            if (fl != null && fl.isValid()) {
                islocked = false;
                fl.release(); //释放文件锁  注意：释放锁要在文件写操作之前，否则会出异常

            } else {
                islocked = true;

            }
            raf.close();  //关闭文件写操作
            return islocked;
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return true;
        }
    }

    /**
     * 取消文件锁
     *
     * @return
     */
    public static boolean unlockFile() {
        File locFile = new File(LOC_DIR, LOC_FILE);
        FileOutputStream raf;
        FileLock fl = null;
        try {
            if (!locFile.exists()) {
                locFile.createNewFile();
                return true;
            }
            raf = new FileOutputStream(locFile);
            FileChannel fc = raf.getChannel(); //获取FileChannel对象
            fl = fc.tryLock();  //or fc.lock();
            if (fl != null&&fl.isValid()) {
                fl.release(); //释放文件锁  注意：释放锁要在文件写操作之前，否则会出异常
                return true;
            }
            raf.close();  //关闭文件写操作
            return true;
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return false;
        }
    }
}
