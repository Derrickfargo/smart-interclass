package cn.com.incito.socket.handler;

import java.util.List;

import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.vo.Group;
import cn.com.incito.classroom.vo.Student;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

import com.alibaba.fastjson.JSON;

/**
 * 分组提交修改hanlder Created by bianshijian on 2014/7/28.
 */
public class GroupSubmitHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		int code = data.getIntValue("code");
		if (code == 0) {
			MyApplication app = MyApplication.getInstance();
			Student student = app.getStudent();
			List<Group> groupList = JSON.parseArray(data.getString("data"),
					Group.class);
			for (Group group : groupList) {
				if (group.getCaptainid() == student.getId()) {
					MyApplication.getInstance().setGroup(group);
					UIHelper.getInstance().showConfirmGroupActivity(
							data.getString("data"));
					return;
				}
			}
			UIHelper.getInstance().getmSelectGroupActivity().setData(groupList);
		} else {

		}

	}

}
