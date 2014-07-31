/*
 * @(#) NSSAESDecoder.java Oct 24, 2012 
 *
 * Copyright 2012 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package cn.com.incito.classroom.transition;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.jboss.netty.buffer.BigEndianHeapChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.PowerManager;

import cn.com.incito.classroom.base.MyApplication;

/**
 * @author Hovan Yoo
 */
public class ChannelDecoder extends FrameDecoder {
    Cipher mCipher;
    public static String[] byteToHex;

    static {
        byteToHex = new String[Byte.MAX_VALUE - Byte.MIN_VALUE + 1];
        for (int i = Byte.MIN_VALUE; i <= Byte.MAX_VALUE; i++) {
            byteToHex[i - Byte.MIN_VALUE] = String.format("%x", (byte) i);
        }
    }

    public ChannelDecoder() {
        super();
        try {
            mCipher = Cipher.getInstance("aes/cbc/pkcs5padding");
        } catch (NoSuchAlgorithmException e) {
            // 无法发生，发生错误
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            // 无法发生，发生错误
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void printPacket(byte[] packet) {
        StringBuffer sb = new StringBuffer();
        sb.append("received = ");
        for (byte bt : packet) {
            sb.append(byteToHex[bt - Byte.MIN_VALUE]);
            sb.append(" ");
        }

    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws JSONException {

        PowerManager.WakeLock wakeLock = WakeLockWrapper.getWakeLockInstance(MyApplication.getApplication(), ChannelDecoder.class.getSimpleName());
        wakeLock.acquire();

        try {
            // 信件读到数据到达的检查
            if (buffer.readableBytes() < 4) {
                return null;
            }
            int nBodySize = buffer.getInt(0); // 读长音

            if (buffer.readableBytes() < nBodySize + 4) {
                return null;
            }

            // decoding
            buffer.skipBytes(4);

            byte[] recvBuff = new byte[nBodySize - 1];
            buffer.readBytes(recvBuff, 0, nBodySize - 1);
            buffer.skipBytes(1);

            byte[] decryptMessage;
            try {
                decryptMessage = mCipher.doFinal(recvBuff);
            } catch (IllegalBlockSizeException e) {
                printPacket(recvBuff);
                channel.close();
                throw new RuntimeException("decrypt fail");
            } catch (BadPaddingException e) {
                printPacket(recvBuff);
                channel.close();
                throw new RuntimeException("decrypt fail");
            }

            if (decryptMessage.length < 4) {
                channel.close();
                throw new RuntimeException("decripted message should longer than 4 bytes");
            }

            ChannelBuffer cb = new BigEndianHeapChannelBuffer(decryptMessage);
            // int jsonLen = (decryptMessage[0] << 24) + (decryptMessage[1] <<
            // 16) +
            // (decryptMessage[2] << 8) + (decryptMessage[3]);
            int jsonLen = cb.readInt();

            String jsonString;
            try {
                jsonString = new String(decryptMessage, 4, jsonLen, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                // 无法发生，发生错误
                throw new RuntimeException(e);
            }


            return new JSONObject(jsonString);
        } finally {
            wakeLock.release();
        }
    }
}
