package cn.com.incito.socket.handler;

import java.nio.ByteBuffer;

import android.app.Dialog;
import android.content.Context;
import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.classroom.ui.widget.FtpReconnectDialog;
import cn.com.incito.classroom.utils.FTPUtils;
import cn.com.incito.common.utils.AndroidUtil;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.socket.utils.BufferUtils;

/**
 * 试卷分发 hanlder Created by popoy on 2014/7/28.
 */
public class DistributePaperHandler extends MessageHandler {


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
		if ("true".equals(isContainsPic)) {//有图片,利用ftp去下载图片 
			FTPUtils.getInstance();
			if(FTPUtils.downLoadFile(Constants.FILE_PATH, Constants.FILE_NAME))	{//下载成功
		
			}else{//图片下载失败
				Dialog dialog=new FtpReconnectDialog((Context)AppManager.getAppManager().currentActivity(),"作业收取失败","重新收取",1);
				dialog.show();
			}
			
			// 获取图片信息
//			byte[] imageSize = new byte[4];// int
//			buffer.get(imageSize);
//			int pictureLength = (int) BufferUtils.decodeIntLittleEndian(imageSize, 0, imageSize.length);
//			imageByte = new byte[pictureLength];
//			buffer.get(imageByte);
		};
		handleMessage();
	}

	@Override
	protected void handleMessage() {
		MyApplication.getInstance().lockScreen(false);
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+"收到作业");
		UIHelper.getInstance().showDrawBoxActivity();
	}

	/**
	 * @param buffer
	 * @return 获得返回数据
	 */
	public String getInfo(ByteBuffer buffer) {
		byte[] intSize = new byte[4];// int
		buffer.get(intSize);
		long idLength = BufferUtils.decodeIntLittleEndian(intSize, 0, intSize.length);
		byte[] idByte = new byte[(int) idLength];
		buffer.get(idByte);
		return BufferUtils.readUTFString(idByte);
	}
}
