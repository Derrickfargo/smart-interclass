package cn.com.incito.socket.handler;

import java.util.List;

import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.vo.Group;
import cn.com.incito.classroom.vo.Student;
import cn.com.incito.common.utils.AndroidUtil;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

import com.alibaba.fastjson.JSON;

/**
 * 分组提交修改hanlder Created by bianshijian on 2014/7/28.
 */
public class GroupSubmitHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime() +"：GroupSubmitHandler：收到分组创建信息:" +data.getString("data") );
		
		int code = data.getIntValue("code");
		if (code == 0) {
			MyApplication app = MyApplication.getInstance();
			Student student = app.getStudent();
			List<Group> groupList = JSON.parseArray(data.getString("data"),
					Group.class);
			for (Group group : groupList) {
				if (group.getCaptainid() == student.getId()) {
					
					//如果不是刚创建的小组组长而是其他小组的组长则不进行跳转
					if(!"WaitForOtherMembersActivity".equals(AppManager.getAppManager().currentActivity().getClass().getSimpleName())){
						MyApplication.getInstance().setGroup(group);
						AppManager.getAppManager().currentActivity().finish();
						UIHelper.getInstance().showConfirmGroupActivity(data.getString("data"));
						return;
					}
					
					
				}
			}
			UIHelper.getInstance().getmSelectGroupActivity().setData(groupList);
		} else {
			
		}

	}

}
