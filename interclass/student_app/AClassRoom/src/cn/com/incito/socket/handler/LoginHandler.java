package cn.com.incito.socket.handler;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.classroom.vo.Student;
import cn.com.incito.common.utils.AndroidUtil;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.ConnectionManager;
import cn.com.incito.socket.core.MessageHandler;

/**
 * 登陆处理hanlder Created by liushiping on 2014/7/28.
 */
public class LoginHandler extends MessageHandler {

	private int state;

	@Override
	protected void handleMessage() {
		//1 等待老师上课，2分组中，3做作业，4锁屏
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime()
				+ "LoginHandler.class:收到登陆回复：");
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime()
				+ "连接建立成功,开始启动心跳!");
		String server_ip = data.getString("server_ip");
		String server_port = data.getString("server_port");
		Constants.setSERVER_IP(server_ip);
		Constants.setSERVER_PORT(server_port);
		// 启动心跳检测
		ConnectionManager.getInstance(message.getChannel());
					
		// TODO 有可能这台pad没有绑定学生，
		Student student = data.getObject("student", Student.class);
		if (student == null) {
			UIHelper.getInstance().getSelectWifiActivity().showToast();
		}else{
			// MultiCastSocket.getInstance().start();//建立广播socket
			MyApplication.getInstance().setStudent(student);
			// 判断学生状态跳转至不同的界面
			state = data.getIntValue("state");
			MyApplication.Logger.debug("返回状态值:" + state);
			if (state == 1) {
				MyApplication.Logger.debug("返回状态值1");
//				AppManager.getAppManager().currentActivity().finish();
//				UIHelper.getInstance().showClassReadyActivity();
				if (!AppManager.getAppManager().currentActivity().getClass().getSimpleName().equals("ClassReadyActivity")) {
					MyApplication.Logger.debug("当前界面不是等待上课界面，进入准备上课界面，");
					UIHelper.getInstance().showClassReadyActivity();
					MyApplication.Logger.debug("返回状态值进入准备上课界面成功");
				}else{
					MyApplication.Logger.debug("当前界面是等待上课界面，进入准备上课界面，");
					//正在等待上课界面,不做任何处理
				}
			} else if (state == 3) {
				if (!AppManager.getAppManager().currentActivity().getClass().getSimpleName().equals("DrawBoxActivity")) {
					MyApplication.Logger.debug("返回状态值进入做作业界面,当前界面不是画板界面");
//					byte[] imageByte;
//					if (isFileExists()) {
//						MyApplication.Logger.debug("返回状态值进入做作业界面,当前界面不是画板界面,存在以前的图片");
//						imageByte = bmpToByteArray(BitmapFactory.decodeFile("/sdcard/temp.jpg"),true);
//					} else {
//						imageByte = data.getBytes("quiz");
//					}
					UIHelper.getInstance().showDrawBoxActivity(null);
//					if (!"ClassingActivity".equals(AppManager.getAppManager().currentActivity().getClass().getSimpleName())) {
//						AppManager.getAppManager().currentActivity().finish();
//					}
				}else{
					MyApplication.Logger.debug("返回状态值进入做作业界面,当前界面是绘画板界面");
//					if (isFileExists()) {
//						UIHelper.getInstance().getDrawBoxActivity().setBackGround(BitmapFactory.decodeFile("/sdcard/temp.jpg"));
//					} else {
//						//在原来界面,不做操作
//					}
				}
			} else if (state == 4) {
//				if (isFileExists()) {
//					byte[] imageByte = bmpToByteArray(BitmapFactory.decodeFile("/sdcard/temp.jpg"),true);
//					UIHelper.getInstance().showDrawBoxActivity(imageByte);
//				} else {
					MyApplication.Logger.debug("返回状态值进入开始上课界面");
					if (!AppManager.getAppManager().currentActivity().getClass().getSimpleName().equals("ClassingActivity")) {
						AppManager.getAppManager().currentActivity().finish();
						UIHelper.getInstance().showClassingActivity();
						MyApplication.Logger.debug("返回状态值4,当前界面不是上课界面 ,进入开始上课界面成功");
					}else{
						MyApplication.Logger.debug("返回状态值4,当前界面是上课界面 ，不做任何处理");
					}
//				}
			}else{
				MyApplication.Logger.debug("没有返回值");
				AppManager.getAppManager().currentActivity().finish();
				UIHelper.getInstance().showClassReadyActivity();
			}
		}
	}

	/**
	 * @param bmp
	 * @param needRecycle
	 * @return bitmap  转byte[]
	 */
	public byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.JPEG, 40, output);
		if (needRecycle) {
			bmp.recycle();
		}
		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean isFileExists() {
		File f = new File("/sdcard/", "temp.jpg");
		if (f.exists()&&f.length()>0) {
			return true;
		} else {
			return false;
		}
	}
	
	//以下注释 为分组状态同步
//	} else if (state == 2) {
//	MyApplication.Logger.debug("返回状态值进入分组界面");
//	// 判断该学生是在已经提交的组还是未提交的组还是没有分组
//	List<Group> tempGrou = JSON.parseArray(data.getString("group"),
//			Group.class);
//	List<Group> groupConfirm = JSON.parseArray(
//			data.getString("groupConfirm"), Group.class);
//
//	if (tempGrou != null && tempGrou.size() > 0) {
//		// 判断当前学生是否是未提交小组的成员
//		for (int i = 0; i < tempGrou.size(); i++) {
//			Group group = tempGrou.get(i);
//			if (student.getId() == group.getCaptainId()) {// 如果是组长则进入等待其他小组成员界面
//				MyApplication.getInstance().setGroup(group);
//				UIHelper.getInstance().showConfirmGroupActivity(
//						data.getString("group"));
//			} else {// 判断是否是未提交小组成员
//				List<Student> students = group.getStudents();
//				for (int j = 0; j < students.size(); j++) {
//					Student s = students.get(j);
//					if (s.getId() == student.getId()) {
//						MyApplication.getInstance().setGroup(group);
//						UIHelper.getInstance()
//								.showConfirmGroupActivity(
//										data.getString("group"));
//					} else {
//						UIHelper.getInstance().showGroupSelect(
//								data.getString("group"));
//					}
//				}
//			}
//		}
//	} else if (groupConfirm != null && groupConfirm.size() > 0) {
//		// 判断当前学生是否是已经提交的小组成员
//		for (int i = 0; i < groupConfirm.size(); i++) {
//			Group group = groupConfirm.get(i);
//			if (student.getId() == group.getCaptainId()) {
//				MyApplication.getInstance().setGroup(group);
//				UIHelper.getInstance().showClassingActivity();
//			} else {
//				List<Student> students = group.getStudents();
//				for (int j = 0; j < students.size(); j++) {
//					Student s = students.get(j);
//					if (s.getId() == student.getId()) {
//						MyApplication.getInstance().setGroup(group);
//						UIHelper.getInstance()
//								.showClassingActivity();
//					} else {
//						UIHelper.getInstance().showGroupSelect(
//								data.getString("group"));
//					}
//				}
//			}
//		}
//	} else {
//		UIHelper.getInstance().showGroupSelect(
//				data.getString("group"));
//	}
//	AppManager.getAppManager().currentActivity().finish();
	
	
}
