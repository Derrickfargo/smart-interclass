package cn.com.incito.server.handler;

import com.alibaba.fastjson.JSONObject;

import cn.com.incito.interclass.main.Main;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.message.MessagePacking;

public class VersionUpdateHandle extends MessageHandler{

	@Override
	protected void handleMessage() {
		JSONObject json = new JSONObject();
		json.put("serverVersionCode", Main.VERSION_CODE);
		json.put("fileName",Main.updatePath+"/互动课堂_"+Main.VERSION_CODE+".apk" );
		MessagePacking msg = new MessagePacking(Message.MESSAGE_APK_UPDATE);
		msg.putBodyData(null, json.toJSONString().getBytes());
		sendMessage(msg);
	}

	private void sendMessage(MessagePacking msg){
		ctx.writeAndFlush(msg);
	}
}
