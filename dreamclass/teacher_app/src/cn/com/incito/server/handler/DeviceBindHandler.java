package cn.com.incito.server.handler;

import java.util.List;

import org.apache.log4j.Logger;

import cn.com.incito.interclass.po.Device;
import cn.com.incito.interclass.po.Table;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.core.SocketServiceCore;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;
import cn.com.incito.server.utils.JSONUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 设备绑定处理器
 * @author 刘世平
 *
 */
public class DeviceBindHandler extends MessageHandler {
	private Logger logger = Logger.getLogger(getClass().getName());
	@Override
	public void handleMessage() {
		logger.info("收到设备绑定消息:" + data.toJSONString());
		String imei = data.getString("imei");
		int number = data.getIntValue("number");
		Application app = Application.getInstance();
		Table table = app.getTableNumberMap().get(number);
		List<Device> deviceList = app.getTableDevice().get(table.getId());
		if (table!= null && deviceList != null) {
			if (deviceList.size() == 4) {
				sendResponse(JSONUtils.renderJSONString(-1));
				return;
			}
		}
		
		String result = service.deviceBind(imei, number);
		int groupId = getGroupId(result);
		if (groupId > 0) {
			Application.getInstance().addSocketChannel(groupId, ctx);
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
		logger.info("回复设备绑定消息:" + json);
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_DEVICE_BIND);
        messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(json));
        SocketServiceCore.getInstance().sendMsg(messagePacking, ctx);
	}
}
