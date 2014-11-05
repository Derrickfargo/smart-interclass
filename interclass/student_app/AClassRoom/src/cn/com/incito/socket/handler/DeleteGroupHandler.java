package cn.com.incito.socket.handler;

import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

import com.alibaba.fastjson.JSONArray;

public class DeleteGroupHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		if(0 == data.getIntValue("code")){
			JSONArray jsonArray = data.getJSONArray("data");
				UIHelper.getInstance().showGroupSelect(jsonArray.toJSONString());
		}

	}

}
