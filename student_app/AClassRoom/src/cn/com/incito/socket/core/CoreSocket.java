package cn.com.incito.socket.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;

import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;
import cn.com.incito.wisdom.sdk.log.WLog;

import com.alibaba.fastjson.JSONObject;

/**
 * 客户端Socket
 *
 * @author 刘世平
 */
public final class CoreSocket implements Runnable {
	private static CoreSocket instance = null;
	private boolean isRunning = false;
	private boolean isConnected = false;
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

	public boolean isConnected() {
		return isConnected;
	}

	private void handle(SelectionKey selectionKey) {
		if (selectionKey.isConnectable()) {// 连接建立事件，已成功连接至服务器
			channel = (SocketChannel) selectionKey.channel();
			try {
				if (channel.isConnectionPending()) {
					channel.finishConnect();
					// 发送设备登陆消息
					sendDeviceLoginMessage();
				}
				isConnected = true;
				channel.register(selector, SelectionKey.OP_READ);// 注册读事件
			} catch (IOException e) {
				isConnected = false;
				WLog.e(CoreSocket.class, "" + e.getMessage());
			}
		} else if (selectionKey.isReadable()) {// 若为可读的事件，则进行消息解析
			MessageParser messageParser = new MessageParser();
			messageParser.parseMessage(selectionKey);
		}
	}

	// 发送设备登陆消息至服务器
	private void sendDeviceLoginMessage() throws IOException {
		MessagePacking messagePacking = new MessagePacking(
				Message.MESSAGE_HAND_SHAKE);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("imei", MyApplication.deviceId);
		messagePacking.putBodyData(DataType.INT,
				BufferUtils.writeUTFString(jsonObject.toJSONString()));
		byte[] handSharkData = messagePacking.pack().array();
		ByteBuffer buffer = ByteBuffer.allocate(handSharkData.length);
		buffer.put(handSharkData);
		buffer.flip();
		channel.write(buffer);
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
					if (channel != null && channel.isConnected()) {
						channel.write(buffer);
					}
				} catch (IOException e) {
					WLog.e(CoreSocket.class, "" + e.getMessage());
				}
			}
		}.start();

	}

	public void stopConnection() {
		new Thread() {
			public void run() {
				if (channel != null) {
					MessagePacking messagePacking = new MessagePacking(
							Message.MESSAGE_DEVICE_LOGOUT);
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("imei", MyApplication.deviceId);
					messagePacking.putBodyData(DataType.INT, BufferUtils
							.writeUTFString(jsonObject.toJSONString()));
					byte[] logoutData = messagePacking.pack().array();
					ByteBuffer buffer = ByteBuffer.allocate(logoutData.length);
					buffer.put(logoutData);
					buffer.flip();
					try {
						if (channel.isConnected()) {
							channel.write(buffer);
							ConnectionManager.getInstance().close(true);
							// channel.close();
						}
					} catch (IOException e) {
						WLog.e(CoreSocket.class, "" + e.getMessage());
					}
				}
			}
		}.start();
	}

	public void restartConnection() {
		disconnect();
		new Thread(this).start();
	}

	public void disconnect() {
		isRunning = false;
	}

	@Override
	public void run() {
		try {
			isRunning = true;
			// 客户端向服务器端发起建立连接请求
			SocketChannel socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);
			socketChannel.socket().setSoTimeout(3000);
			selector = Selector.open().wakeup();
			socketChannel.register(selector, SelectionKey.OP_CONNECT);
			socketChannel.connect(new InetSocketAddress(Constants.IP,
					Constants.PORT));
			while (isRunning) {// 轮询监听客户端上注册事件的发生
				selector.select(300);
				Set<SelectionKey> keySet = selector.selectedKeys();
				for (final SelectionKey key : keySet) {
					handle(key);
				}
				keySet.clear();
			}
		} catch (IOException e) {
			WLog.e(CoreSocket.class, "" + e.getMessage());
		}
	}

}
