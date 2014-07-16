package cn.com.incito.server.utils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 
 *ByteBuffer工具类
 */
public final class BufferUtils
{
    private static final int TWO = 2;
    
    private static final int THREE = 3;
    
    private static final int FOUR = 4;
    
    private static final int EIGHT = 8;
    
    private static final int OXFF = 0XFF;
    
    private static final int SIXTEEN = 16;
    
    private static final int TWENTY_FOUR = 24;
    
    /**
     * 默认字符集
     */
    private static final String CHARSET = "UTF-8";
    
    
    /**
     * 低字节序传输。即低位在前，高位在后的协议传输方式，将一个long或者int或者short等64位以内的数据转化到指定长度的byte数组以内
     * @param dst 接收的byte数组
     * @param val 需要转化的整数，若需要将int或者short型数据填入4字节和2字节的byte数组中，则需强转为long类型数据再调用此方法
     * @param offset 默认设置为0
     * @param size 结束位置，根据数据类型来定。long型则设置为8，int类型设置为4，short类型设置为2
     */
    public static void encodeIntLittleEndian(byte[] dst, long val, int offset, int size){
        for (int i = 0; i < size; i++){
            dst[offset++] = (byte)(val >> (i * Byte.SIZE));
        }
    }
    
    /**
     * 低字节序传输。即低位在前，高位在后的解码方式，将一个byte数组转化为相应的long，int或者short的数据类型
     * @param val 需要解码的byte数组
     * @param offset 默认设置为0
     * @param size 结束位置，根据数据类型来定。long型则设置为8，int类型设置为4，short类型设置为2
     * @return 解码val后所得的数据，若解码时为int则可强赚为int，若为short则可强转为short
     */
    public static long decodeIntLittleEndian(byte[] val, int offset, int size){
        long rtn = 0;
        for (int i = 0; i < size; i++){
            rtn = (rtn << Byte.SIZE) | ((long)val[size - i - 1] & OXFF);
        }
        return rtn;
    }
    
    /**
     * 将int类型的数据转换为byte数组 原理：将int数据中的四个byte取出，分别存储
     * @param n 需要转为byte数组的整数
     * @return 返回byte数组
     */
    public static byte[] intToBytes2(int n)
    {
        byte[] b = new byte[FOUR];
        for (int i = 0; i < FOUR; i++)
        {
            b[THREE - i] = (byte)(n >> (TWENTY_FOUR - i * EIGHT));
        }
        return b;
    }
    
    /**
     * 将4个字节的byte数组转换为int数据
     * @param b 被解析的byte数组
     * @param offset ，相对为止，一般默认设置为0
     * @return 解析后获得的整数
     */
    public static int byteToInt2(byte[] b, int offset)
    {
        int ch4 = b[offset] & OXFF;
        int ch3 = b[offset + 1] & OXFF;
        int ch2 = b[offset + TWO] & OXFF;
        int ch1 = b[offset + THREE] & OXFF;
        if ((ch1 | ch2 | ch3 | ch4) < 0)
        {
            return -1;
        }
        return (ch1 << TWENTY_FOUR) + (ch2 << SIXTEEN) + (ch3 << EIGHT) + (ch4 << 0);
    }
    
    /**
     * byte转换为int
     * @param b Byte类型
     * @return int 整数
     */
    public static int byteToInt(Byte b)
    {
        return b & OXFF;
    }
    
    /**
     * 将数组转换为字符串
     * @param bytes 数组
     * @return String 结果字符串
     */
    public static String readUTFString(byte[] bytes)
    {
        if (bytes != null && bytes.length != 0)
        {
            try
            {
                return new String(bytes, CHARSET).trim();
            }
            catch (UnsupportedEncodingException e)
            {
                System.out.println("数组转字符串失败!");
            }
        }
        return "";
    }
    
    /**
     * 将byte数组转为相应的数据类型的数据
     * @param type 数据类型对应3种数据类型：
     *              String 对应OrderContent.STRING，
     *              int    对应OrderContent.INT，
     *              long   对应OrderContent.LONG,
     *              byte   对应OrderContent.BYTE 
     * @param bytes byte数组
     * @return 返回相应数据类型
     */
//    public static Object getBytesToObject(int type, byte[] bytes)
//    {
//        
//    }
    
    /**
     * 根据长度分配相应大小的ByteBuffer
     * @param size 字节长度
     * @return buffer 准备好读和写的buffer
     */
    public static ByteBuffer prepareToReadOrPut(int size)
    {
        ByteBuffer buffer = ByteBuffer.allocate(size);
        //设置为LITTLE_ENDIAN的顺序
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        // prepare to read or put
        buffer.clear();
        return buffer;
    }
    
    /**
     * 将指定的字符串填充到byte数组中
     * @param str 指定的字符串
     * @return 填充的byte数组
     */
    public static byte[] writeUTFString(String str)
    {
        byte[] byts = null;
        if (str == null)
        {
            str = "";
        }
        try
        {
            byts = str.getBytes(CHARSET);
        }
        catch (UnsupportedEncodingException e)
        {
        	System.out.println("不支持字符编码:" + e.getMessage());
        }
        return byts;
    }
    
}
