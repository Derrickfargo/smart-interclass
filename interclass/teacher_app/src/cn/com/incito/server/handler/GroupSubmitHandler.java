//package cn.com.incito.server.handler;
//
//import java.io.IOException;
//import java.nio.ByteBuffer;
//import java.nio.channels.SocketChannel;
//import java.util.List;
//
//import org.apache.log4j.Logger;
//
//import com.alibaba.fastjson.JSONObject;
//
//import cn.com.incito.server.api.Application;
//import cn.com.incito.server.core.Message;
//import cn.com.incito.server.core.MessageHandler;
//import cn.com.incito.server.message.DataType;
//import cn.com.incito.server.message.MessagePacking;
//import cn.com.incito.server.utils.BufferUtils;
//
//public class GroupSubmitHandler extends MessageHandler {
//	private Logger logger = Logger.getLogger(GroupSubmitHandler.class.getName());
//	
//	@Override
//	public void handleMessage() {
//		logger.info("收到分组信息提交确认消息:" + data);
//		
//		int id = data.getIntValue("id");
//		Application.getInstance().getTempGroup().put(id, data);
//		List<SocketChannel> channels = service.getGroupSocketChannelByGroupId(id);
//		JSONObject result = new JSONObject();
//		result.put("code", 0);
//		result.put("data", data);
//		sendResponse(result.toString(), channels);
//	}
//
//	private void sendResponse(String json, List<SocketChannel> channels) {
//		logger.info("回复分组信息提交确认消息:" + json);
//		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_GROUP_CONFIRM);
//        messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(json));
//        byte[] messageData = messagePacking.pack().array();
//        ByteBuffer buffer = ByteBuffer.allocate(messageData.length);
//        for(SocketChannel channel: channels){
//			buffer.clear();
//			buffer.put(messageData);
//			buffer.flip();
//			try {
//				channel.write(buffer);
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        }
//		}
//	}
//}
