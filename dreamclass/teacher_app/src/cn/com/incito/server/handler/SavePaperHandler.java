package cn.com.incito.server.handler;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;
import cn.com.incito.server.utils.JSONUtils;

/**
 * 获取分组消息处理器
 * 
 * @author 刘世平
 * 
 */
public class SavePaperHandler extends MessageHandler {
	private Logger logger = Logger.getLogger(SavePaperHandler.class.getName());
	private String imei;
	private String quizid;

	@Override
	public void handleMessage(Message msg) {
		this.message = msg;
		logger.info("收到作业提交消息");
		ByteBuffer buffer = msg.getBodyBuffer();
		buffer.flip();
		// 获取考试id号
		quizid = getInfo(buffer);
		logger.info("quizid：" + quizid);
		// 获取imei
		imei = getInfo(buffer);
		logger.info("imei：" + imei);
		// 获取图片信息
		byte[] imageSize = new byte[4];// int
		buffer.get(imageSize);
		int pictureLength = (int) BufferUtils.decodeIntLittleEndian(imageSize,
				0, imageSize.length);
		logger.info("作业图片总大小：" + pictureLength + "字节.");
		byte[] imageByte = new byte[pictureLength];
		buffer.get(imageByte);
		handleMessage(imageByte);
	}

	public void handleMessage(byte[] imageByte) {
		// 需要给组中所以的设备发送
		service.SavePaper(imei, quizid, Application.getInstance()
				.getLessionid(), imageByte, message.getChannel());
		sendResponse(JSONUtils.renderJSONString(0));
	}

	private void sendResponse(String json) {
		logger.info("回复作业提交消息：" + json);
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
	private String getInfo(ByteBuffer buffer) {
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
