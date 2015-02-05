package cn.com.incito.socket.core.listener;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import cn.com.incito.socket.core.callback.ISendMessageCallback;
import cn.com.incito.socket.message.MessagePacking;

/**
 * 消息发送完成事件的监听
 * 
 * @author hm
 */
public class SendMessageListener implements ChannelFutureListener {

	private MessagePacking messagePacking;
	private ISendMessageCallback mSendMessageCallback;

	public SendMessageListener(MessagePacking messagePacking,ISendMessageCallback mSendMessageCallback) {
		this.messagePacking = messagePacking;
		this.mSendMessageCallback = mSendMessageCallback;
	}

	@Override
	public void operationComplete(ChannelFuture future) throws Exception {
		byte msgId = messagePacking.msgId;
		if (future.isSuccess()) {
			mSendMessageCallback.sendSuccess(msgId);
		}else{
			mSendMessageCallback.sendError(msgId, future.cause());
		}
	}
}
