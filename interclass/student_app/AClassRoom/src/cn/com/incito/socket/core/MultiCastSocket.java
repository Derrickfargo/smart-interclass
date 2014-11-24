package cn.com.incito.socket.core;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;

import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.utils.ApiClient;
import cn.com.incito.socket.utils.BufferUtils;
import cn.com.incito.wisdom.sdk.log.WLog;

/**
 * 客户端组播Socket，不发送数据，只接收数据
 * @author 刘世平
 *
 */
public final class MultiCastSocket extends Thread{
	static boolean flag = true;
	private static final int BAND_PORT = 9000;//组播端口
	private static MultiCastSocket instance = null;
	private static final int BUFFER_SIZE = 1024 * 1024;
	/**
     * fake id长度为两个字节
     */
    private static final int FAKE_ID_LENGTH = 2;

    /**
     * 消息头的长度
     */
    private static final int HEADER_LENGTH = 7;
    
	private InetAddress group = null; // 组播组地址.
	private MulticastSocket socket = null; // 组播套接字.
	
	private MultiCastSocket(){
		try {
			group = InetAddress.getByName("224.0.0.251"); // 设置组播地址为224.0.0.251。
			socket = new MulticastSocket(BAND_PORT); // 组播套接字将在9000端口组播。
//			socket.setTimeToLive(1); // 组播套接字发送数据报范围为本地网络。
			socket.joinGroup(group); // 加入组播组,加入group后,socket发送的数据报，可以被加入到group中的成员接收到。
			MyApplication.Logger.info("创建组播成功!");
		} catch (IOException e) {
			MyApplication.Logger.error("创建组播Socket失败", e);
		}
	}
	
	public static MultiCastSocket getInstance() {
		if (instance == null) {
			instance = new MultiCastSocket();
		}
		return instance;
	}

	/**
	 * 关闭所有通道
	 */
	public void close() {
		socket.close();
	}

	@Override
	public void run() {
		MyApplication.Logger.info("组播接收端开启，开始接收组播消息...");
		while (true) {
			byte data[] = new byte[BUFFER_SIZE];
			DatagramPacket packet = new DatagramPacket(data, data.length, group, BAND_PORT); // 待接收的数据包。
			try {
				socket.receive(packet);
				MyApplication.Logger.info("收到组播消息...");
				ByteBuffer buffer = ByteBuffer.wrap(packet.getData(), 0, packet.getLength());
				parseMessage(buffer);
			} catch (IOException e) {
				MyApplication.Logger.error("接收组播消息失败", e);
			}
		}
	}
	
	private void parseMessage(final ByteBuffer buffer){
		new Thread(){
			private Message message;
			@Override
			public void run() {
				byte[] header = new byte[HEADER_LENGTH];
				buffer.get(header);
				ByteBuffer headerBuffer = ByteBuffer.wrap(header);
		        WLog.i(getClass(), "开始解析消息...");
		        // 消息头fakeId是否正确
		        if (parseFakeId(headerBuffer)) {
		            // 获取消息头中有用的信息,msgId,msgSize
		            parseMsgHeader(headerBuffer);
		            // 解析消息体和命令
		            if (parseMsgBody(buffer)) {
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
		        	ApiClient.uploadErrorLog(ex.getMessage());
		            ex.printStackTrace();
		            WLog.e(getClass(), "illegal Number parser");
		            return false;
		        } catch (Exception e) {
		        	ApiClient.uploadErrorLog(e.getMessage());
		            WLog.e(getClass(), "unknow fake Id parser failed");
		            return false;
		        }

		        // 如果消息的fakeId与定义的fakeId值不符，则丢弃掉该条消息
		        if (Message.MESSAGE_FAKE_ID != fakeId) {
		            WLog.e(getClass(), "unknow fake Id parser failed");
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
		     * @param 消息体内容
		     * @return true，解析成功，false表示为其他类型包无需压入消息队列
		     */
		    private boolean parseMsgBody(ByteBuffer buffer) {
		        int bodySize = message.getMsgSize();
		        try {
		        	//获取body数据
		        	byte[] body = new byte[bodySize];
		        	buffer.get(body);
		            message.setBodyBuffer(ByteBuffer.wrap(body));
		            return true;
		        } catch (Exception e) {
		        	ApiClient.uploadErrorLog(e.getMessage());
		            WLog.e(MessageParser.class, "failed to fetch message body :", e);
		            return false;
		        }
		    }
		}.start();
	}
	
}
