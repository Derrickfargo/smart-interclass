package cn.com.incito.server.handler;

import org.apache.log4j.Logger;

import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.utils.QuizCollector;

/**
 * 申请提交作业消息处理器
 * 
 * @author 刘世平
 * 
 */
public class SendPaperHandler extends MessageHandler {
	private Logger logger = Logger.getLogger(StudentLoginHandler.class.getName());

	@Override
	public void handleMessage() {
		logger.info("收到作业提交申请消息:" + data.toJSONString());
		//将提交作业请求加入队列，等待服务器处理
		QuizCollector.getInstance().addQuizQueue(ctx);
	}

}
