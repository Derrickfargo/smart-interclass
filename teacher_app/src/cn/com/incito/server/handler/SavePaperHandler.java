package cn.com.incito.server.handler;

import java.io.IOException;
import java.nio.ByteBuffer;

import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;

/**
 * 获取分组消息处理器
 * 
 * @author 刘世平
 * 
 */
public class SavePaperHandler extends MessageHandler {
	private String imei;
	private String id;
	private String name;
	@Override
	public void handleMessage(Message msg) {
		this.message = msg;
		ByteBuffer buffer = msg.getBodyBuffer();
		buffer.flip();
		// 获取id号
		 id = getInfo(buffer);
		//获取imei
		 imei = getInfo(buffer);
		//获取小组姓名
		 name = getInfo(buffer);
		// 获取图片信息
		byte[] imageSize = new byte[4];// int
		buffer.get(imageSize);
		int pictureLength = (int)BufferUtils.decodeIntLittleEndian(imageSize, 0,
				imageSize.length);
		byte[] imageByte = new byte[pictureLength];
		buffer.get(imageByte);
		handleMessage(imageByte);
	}
	public void handleMessage(byte[] imageByte) {
		System.out.println("消息类型为获取作业:" );
		//需要给组中所以的设备发送
		String result = service.SavePaper(imei,id, imageByte);
//		sendResponse(result);
	}

	private void sendResponse(String json) {
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_SAVE_PAPER_RESULT);
        messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(json));
        byte[] messageData = messagePacking.pack().array();
        ByteBuffer buffer = ByteBuffer.allocate(messageData.length);
        buffer.put(messageData);
        buffer.flip();
        try {
        	message.getChannel().write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	/**
	 * @param buffer
	 * @return 获得android端传过来的数据
	 */
	private String getInfo(ByteBuffer buffer){
		byte[] intSize = new byte[4];// int
		buffer.get(intSize);
		long idLength = BufferUtils.decodeIntLittleEndian(intSize, 0,
				intSize.length);
		byte[] idByte = new byte[(int) idLength];
		buffer.get(idByte);
		return BufferUtils.readUTFString(idByte);
	}
	@Override
	protected void handleMessage() {
		// TODO Auto-generated method stub
		
	}
	
	
}
