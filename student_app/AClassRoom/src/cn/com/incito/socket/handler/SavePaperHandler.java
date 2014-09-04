package cn.com.incito.socket.handler;

import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;
/**
 * 试卷提交保存hanlder
 * Created by liguangming on 2014/7/28.
 */
public class SavePaperHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		if(MyApplication.getInstance().isSubmitPaper()){
			MyApplication.getInstance().setSubmitPaper(false);
		}else{
			if(UIHelper.getInstance().getDrawBoxActivity()!=null){
				UIHelper.getInstance().getDrawBoxActivity().submitPaper();
			}
		}
	}
}
