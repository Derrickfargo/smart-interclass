package cn.com.incito.socket.handler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.classroom.vo.Group;
import cn.com.incito.classroom.vo.Student;
import cn.com.incito.common.utils.AndroidUtil;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.ConnectionManager;
import cn.com.incito.socket.core.CoreSocket;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.socket.core.MultiCastSocket;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;

import com.alibaba.fastjson.JSON;

/**
 * 登陆处理hanlder Created by liushiping on 2014/7/28.
 */
public class LoginHandler extends MessageHandler {

	private int state;
	@Override
	protected void handleMessage() {
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime()
				+ "LoginHandler.class:收到登陆回复：");
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime()
				+ "连接建立成功,开始启动心跳!");
		String server_ip = data.getString("server_ip");
		String server_port = data.getString("server_port");
		Constants.setSERVER_IP(server_ip);
		Constants.setSERVER_PORT(server_port);

		// TODO 有可能这台pad没有绑定学生，
		Student student = data.getObject("student", Student.class);
		if (student == null) {
			UIHelper.getInstance().getSelectWifiActivity().showToast();
		} else {
			// MultiCastSocket.getInstance().start();//建立广播socket
			MyApplication.getInstance().setStudent(student);
			// 判断学生状态跳转至不同的界面
			 state = data.getIntValue("state");
			MyApplication.Logger.debug("返回状态值:" + state);
			File f = new File("/sdcard/", "temp.PNG");
			if (f.exists()) {
				sendPaper();
			}
			if (state == 1) {
				MyApplication.Logger.debug("返回状态值进入准备上课界面");
				if (!AppManager.getAppManager().currentActivity().getClass()
						.getSimpleName().equals("ClassReadyActivity")) {
					AppManager.getAppManager().currentActivity().finish();
					UIHelper.getInstance().showClassReadyActivity();
				}

			} else if (state == 2) {
				MyApplication.Logger.debug("返回状态值进入分组界面");
				// 判断该学生是在已经提交的组还是未提交的组还是没有分组
				List<Group> tempGrou = JSON.parseArray(data.getString("group"),
						Group.class);
				List<Group> groupConfirm = JSON.parseArray(
						data.getString("groupConfirm"), Group.class);

				if (tempGrou != null && tempGrou.size() > 0) {
					// 判断当前学生是否是未提交小组的成员
					for (int i = 0; i < tempGrou.size(); i++) {
						Group group = tempGrou.get(i);
						if (student.getId() == group.getCaptainId()) {// 如果是组长则进入等待其他小组成员界面
							MyApplication.getInstance().setGroup(group);
							UIHelper.getInstance().showConfirmGroupActivity(
									data.getString("group"));

						} else {// 判断是否是未提交小组成员
							List<Student> students = group.getStudents();
							for (int j = 0; j < students.size(); j++) {
								Student s = students.get(j);
								if (s.getId() == student.getId()) {
									MyApplication.getInstance().setGroup(group);
									UIHelper.getInstance()
											.showConfirmGroupActivity(
													data.getString("group"));
								} else {
									UIHelper.getInstance().showGroupSelect(
											data.getString("group"));
								}
							}
						}
					}
				} else if (groupConfirm != null && groupConfirm.size() > 0) {
					// 判断当前学生是否是已经提交的小组成员
					for (int i = 0; i < groupConfirm.size(); i++) {
						Group group = groupConfirm.get(i);
						if (student.getId() == group.getCaptainId()) {
							MyApplication.getInstance().setGroup(group);
							UIHelper.getInstance().showClassingActivity();
						} else {
							List<Student> students = group.getStudents();
							for (int j = 0; j < students.size(); j++) {
								Student s = students.get(j);
								if (s.getId() == student.getId()) {
									MyApplication.getInstance().setGroup(group);
									UIHelper.getInstance()
											.showClassingActivity();
								} else {
									UIHelper.getInstance().showGroupSelect(
											data.getString("group"));
								}
							}
						}
					}
				} else {
					UIHelper.getInstance().showGroupSelect(
							data.getString("group"));
				}

				AppManager.getAppManager().currentActivity().finish();
			} else if (state == 3) {
				MyApplication.Logger.debug("返回状态值进入做作业界面");
				if (!AppManager.getAppManager().currentActivity().getClass()
						.getSimpleName().equals("DrawBoxActivity")) {
					byte[] imageByte = data.getBytes("quiz");
					UIHelper.getInstance().showDrawBoxActivity(imageByte);

					if (!"ClassingActivity".equals(AppManager.getAppManager()
							.currentActivity().getClass().getSimpleName())) {
						AppManager.getAppManager().currentActivity().finish();
					}

				}

			} else if (state == 4) {
				MyApplication.Logger.debug("返回状态值进入开始上课界面");
				if (!AppManager.getAppManager().currentActivity().getClass()
						.getSimpleName().equals("ClassingActivity")) {
					UIHelper.getInstance().showClassingActivity();
					AppManager.getAppManager().currentActivity().finish();
				}

			} else {
				MyApplication.Logger.debug("没有返回值");
				UIHelper.getInstance().showClassReadyActivity();
				AppManager.getAppManager().currentActivity().finish();
			}
			// 启动心跳检测
			ConnectionManager.getInstance(message.getChannel());
		}
	}

	public void sendPaper() {
		MessagePacking messagePacking = new MessagePacking(
				Message.MESSAGE_SAVE_PAPER);
		// 测试ID
		messagePacking.putBodyData(DataType.INT, BufferUtils
				.writeUTFString(MyApplication.getInstance().getQuizID()));
		// 设备ID
		messagePacking.putBodyData(DataType.INT, BufferUtils
				.writeUTFString(MyApplication.getInstance().getDeviceId()));
		// 图片
		messagePacking.putBodyData(DataType.INT,
				bmpToByteArray(fileToBitmap("/sdcard/temp.png",800,1280), true));
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime()
				+ "开始提交以前未提交的作业");
		CoreSocket.getInstance().sendMessage(messagePacking);
		state=4;
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + "启动作业提交...");
		
		// MyApplication.getInstance().setSubmitPaper(true);
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + "提交作业后锁定屏幕");
	}

	public Bitmap fileToBitmap(String path,int w,int h) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		// 设置为ture只获取图片大小
		opts.inJustDecodeBounds = true;
		opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
		// 返回为空
		 BitmapFactory.decodeFile(path, opts);
		int width = opts.outWidth;
		int height = opts.outHeight;
		float scaleWidth = 0.f, scaleHeight = 0.f;
		if (width > w || height > h) {
			// 缩放
			scaleWidth = ((float) width) / w;
			scaleHeight = ((float) height) / h;
		}
		opts.inJustDecodeBounds = false;
		float scale = Math.max(scaleWidth, scaleHeight);
		opts.inSampleSize = (int) scale;
		WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
		return Bitmap.createScaledBitmap(weak.get(), w, h, true);
	}
	public byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
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
}
