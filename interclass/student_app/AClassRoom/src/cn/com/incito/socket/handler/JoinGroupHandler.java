package cn.com.incito.socket.handler;

import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.common.utils.ToastHelper;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

/**
 * 加入小组后的返回处理器
 * @author john
 *
 */
public class JoinGroupHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		int code = data.getInteger("code");//加入小组返回代码 
		if(code == 0){//加入成功进行跳转
			MyApplication.Logger.debug("加入小组成功跳转进等待小组其他成员:" + data.getString("data"));
			UIHelper.getInstance().showConfirmGroupActivity(data.getString("data"));
			return;
		}
		else{//不进行跳转
			MyApplication.Logger.debug("加入小组失败不跳转");
			ToastHelper.showCustomToast(AppManager.getAppManager().currentActivity(), "该小组不存在,请重新加入!");
			return;
		}

	}

}
