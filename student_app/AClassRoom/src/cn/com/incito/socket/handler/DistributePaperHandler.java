package cn.com.incito.socket.handler;

import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

public class DistributePaperHandler extends MessageHandler {

    @Override
    protected void handleMessage() {
    	byte[] paper=data.getBytes("paper");
    	System.out.println("图片内容"+paper.toString());
        UIHelper.getInstance().showDrawBoxActivity(paper);
    }

}
