package cn.com.incito.server.handler;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.core.SocketServiceCore;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;
import cn.com.incito.server.utils.JSONUtils;

/**
 * 获取分组消息处理器
 * 
 * @author 刘世平
 */
public class SavePaperHandler extends MessageHandler {

	private Logger logger = Logger.getLogger(SavePaperHandler.class.getName());

	private String imei;

	private String quizid;

	private String file;

	@Override
	public void handleMessage(MessagePacking msg) {
		logger.info("收到作业提交消息");
		JSONObject json = msg.getJsonObject();
		// 获取考试id号
		quizid = json.getString("quizId");
		logger.info("quizid：" + quizid);
		// 获取imei
		imei = json.getString("deviceId");
		logger.info("imei：" + imei);
		// 获得图片路径
		file = json.getString("file");
		logger.info("路径：" + file);
		service.SavePaper(imei, quizid, file, ctx);
		// 需要给组中所以的设备发送
		sendResponse(JSONUtils.renderJSONString(0));
	}

	private void sendResponse(String json) {
		logger.info("回复作业提交消息：" + json);
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_SAVE_PAPER_RESULT);
		messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(json));
		SocketServiceCore.getInstance().sendMsg(messagePacking, ctx);
	}

	@Override
	protected void handleMessage() {
		// TODO Auto-generated method stub

	}

}
