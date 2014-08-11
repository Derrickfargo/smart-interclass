package cn.com.incito.socket.handler;

import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

public class DistributePaperHandler extends MessageHandler {

    @Override
    protected void handleMessage() {
    	byte[] paper=data.getBytes("paper");
        UIHelper.getInstance().showDrawBoxActivity(paper);
    }

}
