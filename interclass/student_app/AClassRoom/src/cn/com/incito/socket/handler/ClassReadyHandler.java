package cn.com.incito.socket.handler;

import java.util.List;

import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.vo.Group;
import cn.com.incito.common.utils.AndroidUtil;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

public class ClassReadyHandler  extends MessageHandler{

	@Override
	protected void handleMessage() {
		
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + ":ClassReadyHandler:收到学生确认小组信息或者老师点击开始上课信息:" + data.getString("data"));
		if(0 == data.getIntValue("code")){
			//如果当前学生是在选择小组界面则刷新界面数据
			if("SelectGroupActivity".equals(AppManager.getAppManager().currentActivity().getClass().getSimpleName())){
				UIHelper.getInstance().getmSelectGroupActivity().setData(JSON.parseArray(data.getString("data"), Group.class));
				return;
			}
			if("GroupCreatActivity".equals(AppManager.getAppManager().currentActivity().getClass().getSimpleName())){
				UIHelper.getInstance().getmSelectGroupActivity().setData(JSON.parseArray(data.getString("data"), Group.class));
			}
			if(!"ClassingActivity".equals(AppManager.getAppManager().currentActivity().getClass().getSimpleName())){
				
				if("WaitForOtherMembersActivity".equals(AppManager.getAppManager().currentActivity().getClass().getSimpleName())){
					if(!isContainGroup(JSONArray.parseArray(data.getString("data"), Group.class))){
						//如果是该小组的成员则进行界面跳转 
						UIHelper.getInstance().showClassingActivity();
						return;
					}
				}
				
			}
		}
	}
	
	/**
	 * 当前学生所属的小组是否是在返回的小组列表中
	 * @param groupList
	 * @return
	 */
	private boolean isContainGroup(List<Group> groupList){
		
		Group myGroup = MyApplication.getInstance().getGroup();
		
		if(groupList != null && groupList.size() > 0){
			for(int i = 0; i < groupList.size(); i++){
				Group temp = groupList.get(i);
				if(temp.getCaptainId() == myGroup.getCaptainId()){
					return true;
				}
			}
		}
		return false;
	}
}
