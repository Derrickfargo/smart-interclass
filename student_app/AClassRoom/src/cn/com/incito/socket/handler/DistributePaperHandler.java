package cn.com.incito.socket.handler;

import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

public class DistributePaperHandler extends MessageHandler {

    @Override
    protected void handleMessage() {
        UIHelper.getInstance().showDrawBoxActivity();
    }

}
