package cn.com.incito.socket.handler;

import java.util.List;

import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.vo.Group;
import cn.com.incito.common.utils.ToastHelper;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

import com.alibaba.fastjson.JSON;

/**
 * 加入小组后的返回处理器
 * 
 * @author john
 * 
 */
public class JoinGroupHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		MyApplication.Logger.debug("加入小组返回信息:" + data.getString("data"));
		int code = data.getInteger("code");// 加入小组返回代码
		if (code == 0) {
			//如果在等待其他小组成员加入界面，刷新界面数据
			if ((("WaitForOtherMembersActivity").equals(AppManager.getAppManager().currentActivity().getClass().getSimpleName()))) {
				UIHelper.getInstance().getWaitForOtherMembersActivity().setTextName(data.getString("data"));
			} else if (isNewAdd(data.getString("data"))) { // 如果没有在该界面而且是新加入的则进行跳转
				UIHelper.getInstance().showConfirmGroupActivity(data.getString("data"));
			} else {
				// 前两者都不是则只刷新界面
				UIHelper.getInstance().getmSelectGroupActivity().setData(JSON.parseArray(data.getString("data"),Group.class));
			}
		} else {// 不进行跳转
			MyApplication.Logger.debug("加入小组失败不跳转");
			ToastHelper.showCustomToast(AppManager.getAppManager().currentActivity(), "该小组不存在,请重新加入!");
		}

	}

	/**
	 * 判断是否跳转界面的条件
	 * 
	 * @param data
	 * @return
	 */
	private boolean isNewAdd(String data) {
		List<Group> groupList = JSON.parseArray(data, Group.class);
		for (int i = 0; i < groupList.size(); i++) {
			Group group = groupList.get(i);
			if (group.getStudents().contains(MyApplication.getInstance().getStudent())) {
				
				MyApplication.getInstance().setGroup(group);
				return true;
			}
		}
		return false;
	}
}
