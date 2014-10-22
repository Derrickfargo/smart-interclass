package cn.com.incito.socket.handler;

import android.util.Log;

import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;

import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.utils.Utils;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

/**
 * 分组修改hanlder Created by bianshijian on 2014/7/28.
 */
public class GroupEditHandler extends MessageHandler {
	private static final String TAG=GroupEditHandler.class.getSimpleName();
	public static final Logger Logger = LoggerFactory.getLogger();
	@Override
	protected void handleMessage() {
		int groupID = data.getIntValue("id");
		Logger.debug(Utils.getTime()+TAG+":收到修改分组...组ID：" + groupID);
		Log.i("GroupEditHandler", "收到修改分组...组ID：" + groupID);
		UIHelper.getInstance().showEditGroupActivity(groupID);
		MyApplication.getInstance().lockScreen(false);
	}

}
