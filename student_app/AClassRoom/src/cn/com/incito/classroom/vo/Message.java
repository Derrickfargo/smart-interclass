package cn.com.incito.classroom.vo;

import cn.com.incito.classroom.utils.Utils;

public class Message {

    /**
     * ����� *
     */
    public static final byte PROTOCOL_FF = (byte) 0xff;

    /**
     * ��¼ data=id|nickname|img *
     */
    public static final byte PROTOCOL_00 = 0;

    /**
     * ��Ϣ���� data=nickname|message *
     */
    public static final byte PROTOCOL_01 = 1;

    /**
     * �б���� data=id|nickname|img *
     */
    public static final byte PROTOCOL_02 = 2;

    /**
     * �˳� data=id *
     */
    public static final byte PROTOCOL_03 = 3;

    /**
     * ���� *
     */
    public static final byte PROTOCOL_04 = 4;

    /**
     * ����ͼƬ *
     */
    public static final byte PROTOCOL_05 = 5;

    /**
     * ������*
     */
    public static final byte PROTOCOL_06 = 6;

    /**
     * ��ݰ�� ռ4byte *
     */
    private int size;

    /**
     * Э�����1byte *
     */
    private byte code;

    /**
     * �������ݡ���С���� *
     */
    private byte[] data;

    public void setSize(int size) {
        this.size = size;
    }

    public void setCode(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public Message() {
        super();
    }

    public Message(byte code, byte[] data) {
        this.code = code;
        this.data = data;
        this.size = data.length;
    }

    public byte[] write() {
        byte[] d = new byte[this.data.length + Utils.HEAD_LEN];
        System.arraycopy(Utils.intTobyte(size), 0, d, 0, 4);
        System.arraycopy(new byte[]{code}, 0, d, 4, 1);
        System.arraycopy(this.data, 0, d, Utils.HEAD_LEN, this.data.length);
        return d;
    }

    public static Message read(byte d[]) {
        Message msg = new Message();
        msg.setSize(Utils.byteToint(copyOfRange(d, 0, 4)));
        msg.setCode(copyOfRange(d, 4, 5)[0]);
        msg.setData(copyOfRange(d, 5, d.length));
        return msg;
    }

    public static byte[] copyOfRange(byte[] original, int from, int to) {
        int newLength = to - from;
        if (newLength < 0)
            throw new IllegalArgumentException(from + " > " + to);
        byte[] copy = new byte[newLength];
        System.arraycopy(original, from, copy, 0,
                Math.min(original.length - from, newLength));
        return copy;
    }
}
