package cn.com.incito.server.handler;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

import cn.com.incito.interclass.main.Main;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.core.SocketServiceCore;
import cn.com.incito.server.message.MessagePacking;

public class VersionUpdateHandler extends MessageHandler{

	Logger log = Logger.getLogger(VersionUpdateHandler.class.getName());
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
		SocketServiceCore.getInstance().sendMsg(msg, ctx);
		log.info("设备更新命令已发出"+ctx.channel().remoteAddress());
	}
}
