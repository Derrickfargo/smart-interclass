package cn.com.incito.server.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

import cn.com.incito.server.api.Application;

/**
 * 服务端Socket
 * @author 刘世平
 *
 */
public final class CoreSocket extends Thread implements ISocket {
	static boolean flag = true;
	private static final int BAND_PORT = 9001;
	private static CoreSocket instance = null;
	private static Selector selector = null;
	private ServerSocketChannel ssc = null;
	private MessageParser messageParser = null;

	private CoreSocket(){
		Application.getInstance().setCoreSocket(this);
	}
	
	public static CoreSocket getInstance() {
		if (instance == null) {
			instance = new CoreSocket();
		}
		return instance;
	}

	/**
	 * TODO 关闭所有通道
	 */
	private void closeChannel() {
//		Application app = Application.getInstance();
//		Map<Student, SocketChannel> clientChannel = app.getClientChannel();
//		Collection<SocketChannel> channels =  clientChannel.values();
//		for(SocketChannel channel : channels){
//			if (channel != null) {
//				try {
//					channel.close();
//				} catch (IOException ex) {
//					System.out.println("关闭通道异常：" + ex.getMessage());
//				}
//				
//				try {
//					if (selector != null) {
//						selector.close();
//					}
//				} catch (IOException e) {
//					System.out.println("关闭通道异常：" + e.getMessage());
//				}
//				channel = null;
//				selector = null;
//			}
//		}
	}

	/**
	 * 开始建立连接
	 */
	@Override
	public void startConnection() {
		getInstance().start();
	}

	/**
	 * 启动服务器端，配置为非阻塞，绑定端口，注册ACCEPT事件：当服务端收到客户端连接请求时，触发该事件
	 */
	private void init() throws IOException {
		System.out.println("通讯线程启动");
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);
		ServerSocket serverSocket = serverSocketChannel.socket();
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
			} catch (Exception e) {
				e.printStackTrace();
				break;
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

	/**
	 *TODO 
	 * 把消息推送到其他客户端
	 * 启动线程将消息发往所有客户端端
	 * 不用注册WRITE事件，因为大部分时间channel都是可以写
	 * @param message
	 */
	public void sendMessage(final byte[] data){
//		new Thread() {
//			@Override
//			public void run() {
//				try {
//					ByteBuffer buffer = ByteBuffer.allocate(data.length);
//					Application app = Application.getInstance();
//					Map<Student, SocketChannel> clientChannel = app.getClientChannel();
//					for (Map.Entry<Student, SocketChannel> entry : clientChannel.entrySet()) {
//						SocketChannel client = entry.getValue();
//						// 输出到通道
//						buffer.clear();
//						buffer.put(data);
//						buffer.flip();
//						client.write(buffer);
//					}
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			};
//		}.start();
	}
	
	@Override
	public void run() {
		try {
			init();
			listen();
		} catch (IOException e) {
			System.out.println("CoreSocket异常IOException:\n" + e.getMessage());
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
			System.out.println("CoreSocket异常IOException:\n" + e.getMessage());
		}
	}

	/**
	 * 关闭连接
	 */
	@Override
	public void stopConnection() {
		closeChannel();
	}

}