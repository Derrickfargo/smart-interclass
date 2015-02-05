package cn.com.incito.socket.handler;

import cn.com.incito.socket.core.MessageHandler;

/**
 * 抢答成功消息
 * 收到该消息弹出抢答成功界面
* @ClassName: ResponderSuccessHandler 
* @author hm
* @date 2015年2月5日 上午11:34:56 
 */
public class ResponderSuccessHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		activity.showResponderSuccess();
	}
}
