package cn.com.incito.server.handler;

import cn.com.incito.interclass.po.Group;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.MessageHandler;

public class GroupCreatHandler extends MessageHandler{

	@Override
	protected void handleMessage() {
		Group group=(Group) data.get("group");
		Application.getInstance().addGroup(group);
	}

}
