package cn.com.incito.socket.handler;

import java.nio.ByteBuffer;

import android.app.ExecRootCmd;
import android.content.ContentResolver;
import android.provider.Settings;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.socket.utils.BufferUtils;
import cn.com.incito.wisdom.sdk.log.WLog;

/**
 * 试卷分发 hanlder Created by popoy on 2014/7/28.
 */
public class DistributePaperHandler extends MessageHandler {
	private byte[] imageByte;
	private String isContainsPic;

	@Override
	public void handleMessage(Message msg) {
		this.message = msg;
		ByteBuffer buffer = msg.getBodyBuffer();
		buffer.flip();
		// 获取id数据
		String uuid = getInfo(buffer);
		MyApplication.getInstance().setQuizID(uuid);
		// 获取是否还有图片
		isContainsPic = getInfo(buffer);
		if ("true".equals(isContainsPic)) {
			// 获取图片信息
			byte[] imageSize = new byte[4];// int
			buffer.get(imageSize);
			int pictureLength = (int) BufferUtils.decodeIntLittleEndian(
					imageSize, 0, imageSize.length);
			imageByte = new byte[pictureLength];
			buffer.get(imageByte);
		}
		;
		handleMessage();
	}

	@Override
	protected void handleMessage() {
		if (MyApplication.getInstance().isLockScreen()) {
			WLog.i(DistributePaperHandler.class, "收到作业是否有图片：" + isContainsPic);
			ContentResolver mContentResolver = UIHelper.getInstance()
					.getWaitingActivity().getApplicationContext()
					.getContentResolver();
			ExecRootCmd execRootCmd = new ExecRootCmd();
			boolean ret1 = Settings.Global.putInt(mContentResolver,
					"disable_powerkey", 0); // 打开电源按钮唤醒功能
			execRootCmd.powerkey();
			MyApplication.getInstance().setLockScreen(false);
		}
		MyApplication.getInstance().setLockScreen(false);// 屏幕解锁状态
		UIHelper.getInstance().showDrawBoxActivity(imageByte);
	}

	/**
	 * @param buffer
	 * @return 获得返回数据
	 */
	public String getInfo(ByteBuffer buffer) {
		byte[] intSize = new byte[4];// int
		buffer.get(intSize);
		long idLength = BufferUtils.decodeIntLittleEndian(intSize, 0,
				intSize.length);
		byte[] idByte = new byte[(int) idLength];
		buffer.get(idByte);
		return BufferUtils.readUTFString(idByte);
	}
}
