package cn.com.incito.socket.handler;

//import cn.com.incito.classroom.vo.GroupVo;

import com.alibaba.fastjson.JSON;

import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.vo.Group;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

/**
 * 获取组成员列表hanlder Created by liushiping on 2014/7/28.
 */
public class GroupListHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		MyApplication.Logger.debug(System.currentTimeMillis()+"收到分组列表消息：" + data);
		if(0 == data.getIntValue("code")){
			//如果该学生的activity是等待老师分组的activity则将这个activity finish
			if("ClassReadyActivity".equals(AppManager.getAppManager().currentActivity().getClass().getSimpleName())){
				AppManager.getAppManager().currentActivity().finish();
			}
			
			if(AppManager.getAppManager().currentActivity().getComponentName().getClassName().equals("WaitForOtherMembersActivity")){
				UIHelper.getInstance().getWaitForOtherMembersActivity().setTextName(data.getString("data"));
			} else {
				if(UIHelper.getInstance().getmSelectGroupActivity()==null){
					UIHelper.getInstance().showGroupSelect(data.getString("data"));
				}else{
					UIHelper.getInstance().getmSelectGroupActivity().setData(JSON.parseArray(data.getString("data"), Group.class));
				}
				
			}
		}
		
	}
}
