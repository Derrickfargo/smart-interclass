package cn.com.incito.socket.handler;

import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.vo.Group;
import cn.com.incito.classroom.vo.Student;
import cn.com.incito.common.utils.AndroidUtil;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class DeleteGroupHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + ":DeleteGroupHandler:收到删除或者退出小组信息:" + data.getString("data"));
		String currentActivityName = AppManager.getAppManager().currentActivity().getClass().getSimpleName();
		if(!"ClassingActivity".equals(currentActivityName)&&!"GroupCreatActivity".equals(currentActivityName)){
			if (0 == data.getIntValue("code")) {
				if (isGrouper(data)) {// 组长删除小组
					AppManager.getAppManager().currentActivity().finish();
					MyApplication.getInstance().setGroup(null);  //清楚该组成员中 application中保存的小组
					UIHelper.getInstance().getmSelectGroupActivity().setData(JSON.parseArray(data.getString("data"),Group.class));
				} else if (isSelf(data)) {// 该pad的小组成员退出
					AppManager.getAppManager().currentActivity().finish();
					MyApplication.getInstance().setGroup(null);  //成员退出该小组时  application中的小组也应该清楚 
					UIHelper.getInstance().getmSelectGroupActivity().setData(JSON.parseArray(data.getString("data"),Group.class));
				} else {// 其他成员
					if (currentActivityName.equals("WaitForOtherMembersActivity")) {
						UIHelper.getInstance().getWaitForOtherMembersActivity().setTextName(data.getString("data"));
					} else {
						UIHelper.getInstance().getmSelectGroupActivity().setData(JSON.parseArray(data.getString("data"),Group.class));
					}
				}
			}
		}
		
		
	}

	/**
	 * 判断是否是组长
	 * 
	 * @param data
	 * @return
	 */
	private boolean isGrouper(JSONObject data) {

		Group group = MyApplication.getInstance().getGroup();

		if (group != null && data.getInteger("studentId") == group.getCaptainId()) {
			return true;
		}

		return false;
	}

	/**
	 * 判读是否是自己
	 * 
	 * @param data
	 * @return
	 */
	private boolean isSelf(JSONObject data) {
		Student student = MyApplication.getInstance().getStudent();

		if (student.getId() == data.getInteger("studentId")) {
			return true;
		}

		return false;
	}

}
