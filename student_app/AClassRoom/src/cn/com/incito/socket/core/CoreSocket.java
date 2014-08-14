package cn.com.incito.socket.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 客户端Socket
 *
 * @author 刘世平
 */
public final class CoreSocket extends Thread {
    private static CoreSocket instance = null;
    private static final int TIME_OUT = 300;
    private static final int ACT_TIME = 10 * 1000;//10秒钟心跳一次
    private static final int RECONN_TIME = 3 * 1000;//间隔3秒重连
    private boolean isRunning = false;
    private TimerTask actTask;
    private Selector selector;
    private SocketChannel channel;
    
    public static CoreSocket getInstance() {
        if (instance == null) {
            instance = new CoreSocket();
        }
        return instance;
    }

    private CoreSocket() {

    }

    private void handle(SelectionKey selectionKey) throws IOException {
        if (selectionKey.isConnectable()) {//连接建立事件，已成功连接至服务器
            channel = (SocketChannel) selectionKey.channel();
            if (channel.isConnectionPending()) {
                channel.finishConnect();
                //启动心跳
                startHeartBeatThread();
                //发送设备登陆消息
                sendDeviceLoginMessage();
            }
            channel.register(selector, SelectionKey.OP_READ);// 注册读事件
        } else if (selectionKey.isReadable()) {// 若为可读的事件，则进行消息解析
            MessageParser messageParser = new MessageParser();
            messageParser.parseMessage(selectionKey);
        }
    }
    
    //开启心跳线程
    private void startHeartBeatThread() {
    	actTask = new TimerTask() {
			@Override
			public void run() {
				MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_HEART_BEAT);
		        JSONObject jsonObject = new JSONObject();
		        jsonObject.put("imei", MyApplication.deviceId);
		        messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(jsonObject.toJSONString()));
		        byte[] actData = messagePacking.pack().array();
		        ByteBuffer buffer = ByteBuffer.allocate(actData.length);
		        buffer.put(actData);
		        buffer.flip();
		        try {
					channel.write(buffer);
				} catch (IOException e) {
					restart();
				}
			}
		};
		new Timer().schedule(actTask, ACT_TIME, ACT_TIME);
	}
    
    //心跳自动重连机制
    private void restart(){
		isRunning = false;
		new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				actTask.cancel();
				CoreSocket.this.start();
			}
		}, RECONN_TIME);
    }
    
    //TODO 该方法  发送握手消息至服务器
    private void sendDeviceLoginMessage() {
        MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_HAND_SHAKE);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("imei", MyApplication.deviceId);
        messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(jsonObject.toJSONString()));
        byte[] handSharkData = messagePacking.pack().array();
        ByteBuffer buffer = ByteBuffer.allocate(handSharkData.length);
        buffer.put(handSharkData);
        buffer.flip();
        try {
			channel.write(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    /**
     * 客户端往服务端发送消息
     *
     * @param packing
     */
    public void sendMessage(final MessagePacking packing) {
        new Thread() {
            public void run() {
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
        }.start();

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
        	isRunning = true;
            //客户端向服务器端发起建立连接请求
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            socketChannel.connect(new InetSocketAddress(Constants.IP, Constants.PORT));
            while (isRunning) {//轮询监听客户端上注册事件的发生
                selector.select(TIME_OUT);
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

}
