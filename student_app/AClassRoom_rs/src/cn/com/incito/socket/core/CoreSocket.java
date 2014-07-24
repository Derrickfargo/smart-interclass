package cn.com.incito.socket.core;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.popoy.common.TAApplication;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;

import cn.com.incito.classroom.constants.Constant;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;

/**
 * 客户端Socket
 *
 * @author 刘世平
 */
public final class CoreSocket extends Thread {
    private static CoreSocket instance = null;

    private boolean isConnection;
    private Selector selector;
    private SocketChannel channel;

    public static CoreSocket getInstance() {
        if (instance == null) {
            instance = new CoreSocket();
        }
        return instance;
    }
    private CoreSocket(){
        getInstance().start();
    }
    private void handle(SelectionKey selectionKey) throws IOException {
        if (selectionKey.isConnectable()) {//连接建立事件，已成功连接至服务器
            channel = (SocketChannel) selectionKey.channel();
            if (channel.isConnectionPending()) {
                channel.finishConnect();
                //发送握手消息
                byte[] loginData = getHandShakeMessage();
                ByteBuffer buffer = ByteBuffer.allocate(loginData.length);
                buffer.put(loginData);
                buffer.flip();
                channel.write(buffer);// 发送握手消息至服务器
            }
            channel.register(selector, SelectionKey.OP_READ);// 注册读事件
        } else if (selectionKey.isReadable()) {// 若为可读的事件，则进行消息解析
            MessageParser messageParser = new MessageParser();
            messageParser.parseMessage(selectionKey);
        }
    }

    private byte[] getHandShakeMessage() {
        MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_HAND_SHAKE);
        TelephonyManager tm = (TelephonyManager) TAApplication.getApplication().getSystemService(Context.TELEPHONY_SERVICE);
        messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(tm.getDeviceId()));
        return messagePacking.pack().array();
    }

    /**
     * 客户端往服务端发送消息
     *
     * @param packing
     */
    public void sendMessage(MessagePacking packing) {
        byte[] message = packing.pack().array();
        ByteBuffer buffer = ByteBuffer.allocate(message.length);
        buffer.put(message);
        buffer.flip();
        try {
                channel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void stopConnection() {
        try {
            if (channel != null) {
                channel.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            //客户端向服务器端发起建立连接请求
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            socketChannel.connect(new InetSocketAddress(Constant.IP, Constant.PORT));
            while (true) {//轮询监听客户端上注册事件的发生
                selector.select();
                Set<SelectionKey> keySet = selector.selectedKeys();
                for (final SelectionKey key : keySet) {
                    handle(key);
                }
                keySet.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        if (channel == null) {
            return false;
        }
        return channel.isConnected();
    }
}
