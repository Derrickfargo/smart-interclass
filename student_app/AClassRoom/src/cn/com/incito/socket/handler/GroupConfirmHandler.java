package cn.com.incito.socket.handler;

import com.alibaba.fastjson.JSONObject;

import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

public class GroupConfirmHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		int code = data.getIntValue("code");
		if(code == 0){
			JSONObject json = data.getJSONObject("data");
			UIHelper.getInstance().showConfirmGroupActivity(json);
		} else {
			// TODO showMessage
		}
		
	}

}
