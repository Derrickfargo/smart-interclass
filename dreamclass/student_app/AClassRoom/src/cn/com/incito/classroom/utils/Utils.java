package cn.com.incito.classroom.utils;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

public class Utils {

    public static final int MARK_INT = 0xFF;
    public static int PORT = 9001;
    public static String ip = "localhost";
    public static final char MARK = '|';
    public static final char CUSTOMER_MARK = '&';
    public static final int BUFF_SIZE = 1024;
    public static final int HEAD_LEN = 5;

    /**
     * int to byte[] *
     */
    public static byte[] intTobyte(int num) {
        byte intByte[] = new byte[4];
        intByte[0] = (byte) (num & MARK_INT);
        intByte[1] = (byte) ((num >> 8) & MARK_INT);
        intByte[2] = (byte) ((num >> 16) & MARK_INT);
        intByte[3] = (byte) ((num >> 24) & MARK_INT);
        return intByte;
    }

    public static int byteToint(byte[] num) {
        int x = 0;
        for (int i = num.length - 1; i >= 0; i--) {
            x <<= 8;
            x |= num[i] & MARK_INT;
        }
        return x;
    }

    public static byte[] readData(InputStream input) throws Exception {
        byte intLen[] = new byte[4];
        byte data[] = null;
        while (input.available() == -1) {
            Thread.sleep(500);
            continue;
        }
        int size = 0, len = 0, count = 4;
        size = input.read(intLen, 0, 4);
        len = byteToint(intLen) + HEAD_LEN;
        data = new byte[len];
        System.arraycopy(intLen, 0, data, 0, 4);
        while (true) {
            size = input.read(data, count, Math.min(BUFF_SIZE, (len - count)));
            count += size;
            if (count == len)
                break;
        }
        return data;
    }

    public static String getRandomNum(int len) {
        String s = "";
        while (s.length() <= len) {
            s = s.concat(String.valueOf(new Random().nextInt(10)));
        }
        return s;
    }

    public static void main(String args[]) {
        byte[] b = Utils.intTobyte(1000);
        for (byte bb : b) {
            System.out.println(bb);
        }
        System.out.println(Utils.byteToint(b));
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();

		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565;
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		drawable.draw(canvas);
		return bitmap;
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

	public static Drawable getGroupIconByName(TypedArray iconSource,String[] iconsName, String iconName){
		if(iconName != null && !"".equals(iconName)
				&& iconSource != null && iconSource.length() > 0
				&& iconsName != null && iconsName.length > 0){
			for(int i=0;i<iconsName.length;i++){
				if(iconName.equals(iconsName[i])){
					return iconSource.getDrawable(i);
				}
			}
		}
		return null;
	}
	
	/**
	 * 匹配行嘛是否只是中文与英文
	 * @param str  输入的字符串
	 * @return     是返回true   否返回false
	 */
	public static boolean isNumberOrChinese(String str){
		return Pattern.compile("[\u4e00-\u9fa5_a-zA-Z]*?").matcher(str).matches();
	}
	
	/**
	 * @return 获得当前格式化后的日期
	 */
	public static String getTime(){
		Date d = new Date();  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);  
       return  sdf.format(d)+"---";  
	}
	
	/**
	 * @param str
	 * @return 判断字符串是否为空
	 */
	public static boolean isEmpty(String str){
		 return str == null || str.trim().length() == 0;
	}
}
