package cn.com.incito.socket.handler;

import java.util.List;

import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.classroom.vo.Group;
import cn.com.incito.classroom.vo.Student;
import cn.com.incito.common.utils.AndroidUtil;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.ConnectionManager;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.socket.core.MultiCastSocket;

import com.alibaba.fastjson.JSON;
/**
 * 登陆处理hanlder
 * Created by liushiping on 2014/7/28.
 */
public class LoginHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+"LoginHandler.class:收到登陆回复：" + data.toJSONString());
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+"连接建立成功,开始启动心跳!");
		String server_ip = data.getString("server_ip");
		String server_port = data.getString("server_port");
		Constants.setSERVER_IP(server_ip);
		Constants.setSERVER_PORT(server_port);
		
		//TODO 有可能这台pad没有绑定学生，
		Student student = data.getObject("student", Student.class);
		if(student==null){
			UIHelper.getInstance().getWifiSelectorActivity().showToast();
		}else{
//			MultiCastSocket.getInstance().start();//建立广播socket
			MyApplication.getInstance().setStudent(student);
			
			//判断学生状态跳转至不同的界面
			int state =data.getIntValue("state");
			MyApplication.Logger.debug("返回状态值:" + state);
			
			if(state == 1 ){
				MyApplication.Logger.debug("返回状态值进入准备上课界面");
				if(!AppManager.getAppManager().currentActivity().getClass().getSimpleName().equals("ClassReadyActivity")){
					AppManager.getAppManager().currentActivity().finish();
					UIHelper.getInstance().showClassReadyActivity();
				}
				
			}else if(state == 2){
				MyApplication.Logger.debug("返回状态值进入分组界面");
				//判断该学生是在已经提交的组还是未提交的组还是没有分组 
				List<Group> tempGrou = JSON.parseArray(data.getString("group"), Group.class);
				List<Group> groupConfirm = JSON.parseArray(data.getString("groupConfirm"), Group.class);
				
				if(tempGrou != null && tempGrou.size() > 0){
					//判断当前学生是否是未提交小组的成员 
					for(int i = 0; i < tempGrou.size(); i++){
						Group group = tempGrou.get(i);
						if(student.getId() == group.getCaptainId()){//如果是组长则进入等待其他小组成员界面
							MyApplication.getInstance().setGroup(group);
							UIHelper.getInstance().showConfirmGroupActivity(data.getString("group"));
							
						}else{//判断是否是未提交小组成员
							List<Student> students = 	group.getStudents();
							for(int j = 0; j < students.size(); j++){
								Student s = students.get(j);
								if(s.getId() == student.getId()){
									MyApplication.getInstance().setGroup(group);
									UIHelper.getInstance().showConfirmGroupActivity(data.getString("group"));
								}else{
									UIHelper.getInstance().showGroupSelect(data.getString("group"));
								}
							}
						}
					}
				}else if(groupConfirm != null && groupConfirm.size() > 0){
					//判断当前学生是否是已经提交的小组成员 
					for(int i = 0; i < groupConfirm.size(); i++){
						Group group = groupConfirm.get(i);
						if(student.getId() == group.getCaptainId()){
							MyApplication.getInstance().setGroup(group);
							UIHelper.getInstance().showClassingActivity();
						}else{
							List<Student> students = 	group.getStudents();
							for(int j = 0; j < students.size(); j++){
								Student s = students.get(j);
								if(s.getId() == student.getId()){
									MyApplication.getInstance().setGroup(group);
									UIHelper.getInstance().showClassingActivity();
								}else{
									UIHelper.getInstance().showGroupSelect(data.getString("group"));
								}
							}
						}
					}
				}else{
					UIHelper.getInstance().showGroupSelect(data.getString("group"));
				}
			
				AppManager.getAppManager().currentActivity().finish();
			}else if(state == 3){
				MyApplication.Logger.debug("返回状态值进入做作业界面");
				if(!AppManager.getAppManager().currentActivity().getClass().getSimpleName().equals("DrawBoxActivity")){
					byte[] imageByte = data.getBytes("quiz");
					UIHelper.getInstance().showDrawBoxActivity(imageByte);
					AppManager.getAppManager().currentActivity().finish();
				}
				
			}else if(state == 4){
				MyApplication.Logger.debug("返回状态值进入开始上课界面");
				if(!AppManager.getAppManager().currentActivity().getClass().getSimpleName().equals("ClassingActivity")){
					UIHelper.getInstance().showClassingActivity();
					AppManager.getAppManager().currentActivity().finish();
				}
				
			}else{
				MyApplication.Logger.debug("没有返回值");
				UIHelper.getInstance().showClassReadyActivity();
				AppManager.getAppManager().currentActivity().finish();
			}
			//启动心跳检测
			ConnectionManager.getInstance(message.getChannel());
		}
	}

}
