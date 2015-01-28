package cn.com.incito.socket.handler;

import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.common.utils.LogUtil;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.socket.core.NCoreSocket;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 试卷提交保存hanlder
 * Created by liguangming on 2014/7/28.
 */
public class SavePaperHandler extends MessageHandler {
	

	@Override
	protected void handleMessage() {
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("imei", MyApplication.deviceId);
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_RICIVE_SAVEPAPER);
		messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(jsonObject.toJSONString()));
		NCoreSocket.getInstance().sendMessage(messagePacking);
		
		if (MyApplication.getInstance().isSubmitPaper()) {
			MyApplication.getInstance().setSubmitPaper(false);
		} else {
			LogUtil.d("收到老师提交作业命令");
			if (UIHelper.getInstance().getDrawBoxActivity() != null && AppManager.getAppManager().currentActivity().getClass().getSimpleName().equals("DrawBoxActivity")) {
				LogUtil.d("当前界面是做作业界面,开始准备向服务器提交作业");
				UIHelper.getInstance().getDrawBoxActivity().sendPaper();
			}
		}
	}
}
