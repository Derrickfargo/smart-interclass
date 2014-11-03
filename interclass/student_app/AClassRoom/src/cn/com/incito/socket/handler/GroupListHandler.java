package cn.com.incito.socket.handler;


//import cn.com.incito.classroom.vo.GroupVo;

import com.alibaba.fastjson.JSONObject;

import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;
/**
 * 获取组成员列表hanlder
 * Created by liushiping on 2014/7/28.
 */
public class GroupListHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		
		if(0 == data.getIntValue("code")){
			JSONObject json = data.getJSONObject("data");
			UIHelper.getInstance().showSelectGroup(json.toString());
		}
		
//		UIHelper.getInstance().showGroupListActivity();
	}

}
