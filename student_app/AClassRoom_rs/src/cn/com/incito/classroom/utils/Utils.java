package cn.com.incito.classroom.utils;

import java.io.InputStream;
import java.util.Map;
import java.util.Random;

import cn.com.incito.classroom.vo.Customer;

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

    public static byte[] getCustomerInfo(Map<String, Customer> v) throws Exception {
        StringBuffer sb = null;
        if (v != null && v.size() > 0) {
            sb = new StringBuffer();
            Customer c;
            int count = 0;
            for (String key : v.keySet()) {
                c = v.get(key);
                sb.append(c.toString());
                if ((count++) != v.size() - 1) {
                    sb.append(CUSTOMER_MARK);
                }
            }
        }
        return sb.toString().getBytes();
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
}
