package cn.com.incito.socket.handler;

import com.alibaba.fastjson.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.classroom.ui.activity.DrawBoxActivity;
import cn.com.incito.classroom.ui.widget.FtpReconnectDialog;
import cn.com.incito.classroom.utils.FTPUtils;
import cn.com.incito.common.utils.LogUtil;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.socket.core.NCoreSocket;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;

/**
 * 试卷分发 hanlder Created by popoy on 2014/7/28.
 */
public class DistributePaperHandler extends MessageHandler {
	private String isContainsPic;

	@Override
	protected void handleMessage() {
		LogUtil.d("收到做作业命令!");
		String uuid = data.getString("uuid");
		MyApplication.getInstance().setQuizID(uuid);
		isContainsPic = data.getString("isContainsPic");
		MyApplication.getInstance().lockScreen(false);
		
		LogUtil.d("发送回执收到作业回执");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("imei", MyApplication.deviceId);
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_RECIVE_PAER);
		messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(jsonObject.toJSONString()));
		NCoreSocket.getInstance().sendMessage(messagePacking);
		
		Activity activity = AppManager.getAppManager().currentActivity();
		if ("true".equals(isContainsPic)) {// 有图片,利用ftp去下载图片
			LogUtil.d("老师截图发送作业,马上开始下载作业!");
			FTPUtils.getInstance();
			
			if (!FTPUtils.downLoadFile(Constants.FILE_PATH, Constants.FILE_NAME)) {// 下载不成功
				LogUtil.d("下载作业失败!");
				
				Dialog dialog = new FtpReconnectDialog((Context) AppManager.getAppManager().currentActivity());
				dialog.show();
			}
		}
		
		if("DrawBoxActivity".equals(activity.getClass().getSimpleName())){
			LogUtil.d("上次作业没有提交成功!这次只是改变背景!");
			UIHelper.getInstance().getDrawBoxActivity().closeProgressDialog();
			UIHelper.getInstance().getDrawBoxActivity().cancleTask();
			((DrawBoxActivity)activity).replaceBackGround(isContainsPic);
		}else{
			UIHelper.getInstance().showDrawBoxActivity(isContainsPic);
		}
	}
}
