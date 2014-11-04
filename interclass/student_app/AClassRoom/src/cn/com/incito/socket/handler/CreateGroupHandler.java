package cn.com.incito.socket.handler;

import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

/**
 * 创建小组
 * @author john
 *
 */
public class CreateGroupHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		if(data.getIntValue("code") == 0){
//			JSONObject jsonObject = data.getJSONObject("data");
			UIHelper.getInstance().showWaitOtherMembers(data.toString());
		}
	}

}
