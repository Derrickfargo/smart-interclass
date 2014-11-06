package cn.com.incito.socket.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.wisdom.sdk.log.WLog;

/**
 * 分组提交修改hanlder Created by bianshijian on 2014/7/28.
 */
public class GroupSubmitHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		int code = data.getIntValue("code");
		if (code == 0) {
			JSONArray json = data.getJSONArray("data");
			System.out.println(json);
			UIHelper.getInstance().showConfirmGroupActivity(json.toJSONString());
		} else {
			
		}

	}

}
