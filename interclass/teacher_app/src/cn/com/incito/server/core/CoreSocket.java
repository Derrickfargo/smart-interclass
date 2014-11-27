package cn.com.incito.server.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import cn.com.incito.interclass.po.Student;
import cn.com.incito.server.api.ApiClient;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.exception.AppExceptionHandler;

/**
 * 服务端Socket
 * @author 刘世平
 *
 */
public final class CoreSocket extends Thread {
	static boolean flag = true;
	private static final int BAND_PORT = 9001;
	private static CoreSocket instance = null;
	private static Selector selector = null;
	private ServerSocketChannel ssc = null;
	private Logger logger = Logger.getLogger(CoreSocket.class.getName());
	
	private CoreSocket(){
//		setDefaultUncaughtExceptionHandler(new AppExceptionHandler());
	}
	
	public static CoreSocket getInstance() {
		if (instance == null) {
			instance = new CoreSocket();
		}
		return instance;
	}

	/**
	 * 关闭所有通道
	 */
	public void closeChannel() {
		Application app = Application.getInstance();
		Map<String, SocketChannel> clientChannel = app.getClientChannel();
		Collection<SocketChannel> channels =  clientChannel.values();
		for(SocketChannel channel : channels){
			if (channel != null) {
				try {
					if (channel != null && channel.isOpen()) {
						channel.close();
					}
					if (selector != null && selector.isOpen()) {
						selector.close();
					}
				} catch (IOException ex) {
					logger.fatal("关闭通道异常：" + ex.getMessage());
				}
				channel = null;
				selector = null;
			}
		}
		
	}

	/**
	 * 启动服务器端，配置为非阻塞，绑定端口，注册ACCEPT事件：当服务端收到客户端连接请求时，触发该事件
	 */
	private void init() throws IOException {
		logger.info("通讯线程启动");
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);
		ServerSocket serverSocket = serverSocketChannel.socket();
		serverSocket.setSoTimeout(15000);//超时时长为15秒
		serverSocket.bind(new InetSocketAddress(BAND_PORT));
		selector = Selector.open();
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
	}
	
	/**
	 * 服务器端轮询监听，select方法会一直阻塞直到有相关事件发生或超时
	 */
	private void listen() {
		while (true) {
			try {
				selector.select();// 返回值为本次触发的事件数
				Set<SelectionKey> selectionKeys = selector.selectedKeys();
				for (SelectionKey key : selectionKeys) {
					handle(key);
				}
				selectionKeys.clear();// 清除处理过的事件
			} catch (IOException e) {
				logger.fatal("CoreSocket监听异常，异常信息:\n" , e);
				ApiClient.uploadErrorLog(e.toString());
			}
		}
	}
	
	/**
	 * 处理不同的事件
	 */
	private void handle(SelectionKey selectionKey) throws IOException {
		ServerSocketChannel server = null;
		//客户端请求连接事件 ，为该客户端建立连接，将此socket注册READ事件，监听客户端输入
		if (selectionKey.isAcceptable()) {
			server = (ServerSocketChannel) selectionKey.channel();
			final SocketChannel client = server.accept();
			client.configureBlocking(false);
			//注册READ事件：当客户端发来数据，并已被服务器控制线程正确读取时，触发该事件
			client.register(selector, SelectionKey.OP_READ);
		} else if (selectionKey.isReadable()) {// 若为可读的事件，则进行消息解析
			// 得到消息协议解析对象
			MessageParser messageParser = new MessageParser();
			// 解析消息
			messageParser.parseMessage(selectionKey);
		}
	}
	
	@Override
	public void run() {
		try {
			init();
			listen();
		} catch (IOException e) {
			logger.fatal("CoreSocket异常IOException:\n" + e.getMessage());
		}
		// 调用关闭服务端Socket的方法
		closeServerSocketChannel();
	}

	/**
	 * 关闭ServerSocketChannel
	 */
	private void closeServerSocketChannel() {
		try {
			if (ssc != null) {
				ssc.close();
			}
		} catch (IOException e) {
			logger.fatal("关闭服务端socket异常:\n" + e.getMessage());
		}
	}

	/**
	 * 启动线程将消息发往所有客户端
	 * @param data
	 */
	public void sendMessage(final byte[] data){
		Application app = Application.getInstance();
		final Collection<SocketChannel> channels = app.getClientChannel().values();
		new Thread() {
			@Override
			public void run() {
				try {
					ByteBuffer buffer = ByteBuffer.allocate(data.length);
					for(SocketChannel channel: channels){
						if(channel != null && channel.isConnected()){
							// 输出到通道
							buffer.clear();
							buffer.put(data);
							buffer.flip();
							channel.write(buffer);
						}
					}
				} catch (IOException e) {
					logger.fatal("发送消息异常:\n" + e.getMessage());
				}
			};
		}.start();
	}
	
	/**
	 * 启动线程将消息发往所有客户端
	 * @param data
	 */
	public void sendMessageToStudents(final byte[] data){
		final Application app = Application.getInstance();
		Set<Entry<String, SocketChannel>> clients = app.getClientChannel().entrySet();
		final Iterator<Entry<String, SocketChannel>> it = clients.iterator();
		new Thread() {
			@Override
			public void run() {
				try {
					ByteBuffer buffer = ByteBuffer.allocate(data.length);
					while (it.hasNext()) {
						Entry<String, SocketChannel> entry = it.next();
						String imei = entry.getKey();
						Student students = app.getStudentByImei(imei);
						//记录有学生登陆的Pad
						if (students != null) {
							SocketChannel channel = entry.getValue();
							if (channel != null && channel.isConnected()) {
								// 输出到通道
								buffer.clear();
								buffer.put(data);
								buffer.flip();
								channel.write(buffer);
								app.addQuizIMEI(imei);//已发送的IMEI
							}
						}
					}
				} catch (IOException e) {
					logger.error("发送消息异常:\n", e);
				}
			};
		}.start();
	}
}
