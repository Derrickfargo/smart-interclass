package cn.com.incito.server.handler;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.incito.interclass.po.Table;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;
import cn.com.incito.server.utils.JSONUtils;

/**
 * 设备绑定处理器
 * @author 刘世平
 *
 */
public class DeviceBindHandler extends MessageHandler {
	 
	@Override
	public void handleMessage() {
		String imei = data.getString("imei");
		int number = data.getIntValue("number");
		Application app = Application.getInstance();
		Table table = app.getTableNumberMap().get(number);
		if (table!= null && app.getTableDevice().get(table.getId()).size() == 4) {
			sendResponse(JSONUtils.renderJSONString(-1));
			return;
		}
		
		String result = service.deviceBind(imei, number);
		int groupId = getGroupId(result);
		if (groupId > 0) {
			Application.getInstance().addSocketChannel(groupId, message.getChannel());
		}
		service.refreshGroupList();
		sendResponse(JSONUtils.renderJSONString(JSONUtils.SUCCESS));
	}

	private Integer getGroupId(String json){
		JSONObject jsonObject = JSON.parseObject(json);
		if(jsonObject.getIntValue("code") != 0){
			return -1;
		}
		String data = jsonObject.getString("data");
		JSONObject dataObject = JSON.parseObject(data);
		return dataObject.getInteger("id");
	}
	
	private void sendResponse(String json) {
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_DEVICE_BIND);
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
}
