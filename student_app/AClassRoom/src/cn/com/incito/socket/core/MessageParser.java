package cn.com.incito.socket.core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import cn.com.incito.socket.utils.BufferUtils;
import cn.com.incito.wisdom.sdk.log.WLog;

/**
 * 消息解析器
 *
 * @author 刘世平
 */
public class MessageParser {

    /**
     * fake id长度为两个字节
     */
    private static final int FAKE_ID_LENGTH = 2;

    /**
     * 消息头的长度
     */
    private static final int HEADER_LENGTH = 7;

    private Message message;
    private SocketChannel channel = null;

    public void bindHandler(MessageHandler messageHandler) {
        message.setHandler(messageHandler);
    }

    /**
     * 得到read事件，建立与pc的socket通讯连接并进行处理
     *
     * @param key 注册了acceptable的相应通道的key，可由此key得到通道
     */
    public void parseMessage(SelectionKey key) {
        // 创建一个headerBuffer用来接收消息头前6个字节
        ByteBuffer headerBuffer = BufferUtils.prepareToReadOrPut(HEADER_LENGTH);
        channel = (SocketChannel) key.channel();
        try {
            // 从通道中读取消息头,如果该通道已到达流的末尾，则返回 -1
            if (channel.read(headerBuffer) == -1) {
                // 关闭通道
                channel.close();
                return;
            }
        } catch (IOException e) {
            WLog.e(MessageParser.class, "获取MessageHandler出错:" + e.getMessage());
            return;
        }
        headerBuffer.flip();
        WLog.i(MessageParser.class, "开始解析消息...");
        // 消息头fakeId是否正确
        if (parseFakeId(headerBuffer)) {
            // 获取消息头中有用的信息,msgId,msgSize
            parseMsgHeader(headerBuffer);
            // 解析消息体和命令
            if (parseMsgBody()) {
            	message.setChannel(channel);
                // 执行消息命令
                message.executeMessage();
            }
        }
    }

    /**
     * 解析消息头的Fake Id
     *
     * @param buffer 消息头（6个字节）
     * @return boolean 解析消息的fakeId是否为0xFAFB
     */
    private boolean parseFakeId(ByteBuffer buffer) {
        // 创建一个Byte数组来接收fake id的内容
        byte[] fakeIdByte = new byte[FAKE_ID_LENGTH];
        // 报文总长度
        int fakeId = 0;
        try {
            // 读取ByteBuffer的内容到fakeIdByte
            buffer.get(fakeIdByte);
            // 获取fake id的值
            fakeId = Integer.parseInt(BufferUtils.decodeIntLittleEndian(fakeIdByte, 0, fakeIdByte.length) + "");
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            WLog.e(MessageParser.class, "ilegal Number parser");
            return false;
        } catch (Exception e) {
            WLog.e(MessageParser.class, "unknow fake Id parser failed");
            return false;
        }

        // 如果消息的fakeId与定义的fakeId值不符，则丢弃掉该条消息
        if (Message.MESSAGE_FAKE_ID != fakeId) {
            WLog.e(MessageParser.class, "unknow fake Id parser failed");
            return false;
        }
        return true;
    }

    /**
     * 解析消息头得到extSize和msgSize
     *
     * @param buffer 消息体字节数组
     */
    private void parseMsgHeader(ByteBuffer buffer) {
        message = new Message();
        //获取消息id（1个byte）
        message.setMsgID(buffer.get());
        //获取消息体的长度（4个byte）
        message.setMsgSize(buffer.getInt());
    }

    /**
     * 解析消息体
     *
     * @param *消息体内容
     * @return true，解析成功，false表示为其他类型包无需压入消息队列
     */
    private boolean parseMsgBody() {
        int bodySize = message.getMsgSize();
        ByteBuffer bodyBuffer = BufferUtils.prepareToReadOrPut(bodySize);
        try {
            while (bodyBuffer.position() < bodyBuffer.capacity())
                channel.read(bodyBuffer);
            message.setBodyBuffer(bodyBuffer);
            return true;
        } catch (IOException e) {
            WLog.e(MessageParser.class, "failed to fetch message body :" + e.getMessage());
            return false;
        }
    }

}
