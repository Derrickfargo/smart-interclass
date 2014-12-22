package cn.com.incito.server.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.config.AppConfig;
import cn.com.incito.server.core.ConnectionManager;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;

/**
 * 设备登陆消息处理器
 * 
 * @author 刘世平
 * 
 */
public class StudentLoginHandler extends MessageHandler {
	private Logger logger = Logger.getLogger(StudentLoginHandler.class.getName());
	private String imei;

	@Override
	public void handleMessage() {
		imei = data.getString("imei");
		logger.info("收到设备（学生）登陆消息:" + data.toJSONString());
		Student student = service.login(imei);
		ConnectionManager.notification(imei, message.getChannel());
		Application app = Application.getInstance();
		app.addSocketChannel(imei, message.getChannel());
		//1 等待老师上课，2分组中，3做作业，4锁屏
		
		// 回复设备登陆消息
		Properties props = AppConfig.getProperties();
		String ip = props.get(AppConfig.CONF_SERVER_IP).toString();
		String port = props.get(AppConfig.CONF_SERVER_PORT).toString();
		data.put(AppConfig.CONF_SERVER_IP, ip);
		data.put(AppConfig.CONF_SERVER_PORT, port);
		if(app.getState()==1){//等待老师上课
			data.put("state", 1);
		}else if(app.getState()==2){//分组中
			data.put("state", 2);
			List<Group> groupList=new ArrayList<Group>();
			//遍历临时分组 将还没有分组的小组列表传回给pad端
			for (Integer key : Application.getInstance().getTempGroup().keySet()) {
				groupList.add(Application.getInstance().getTempGroup().get(key));
			}
			data.put("group", groupList);
			data.put("groupConfirm",Application.getInstance().getGroupList());
			logger.info("回复设备登陆消息:" + data.toJSONString());
		}else if(app.getState()==3){//做作业
			data.put("state", 3);
//			data.put("quiz",app.getQuiz());
//			logger.info("回复设备登陆消息:老师端发送的作业大小" + app.getQuiz().length);
		}else if(app.getState()==4){//锁屏
			data.put("state", 4);
		}else{
			data.put("state", 1);
		}
		if (student != null) {
			data.put("student", student);
		}
		
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_STUDENT_LOGIN);

		messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(data.toJSONString()));
		byte[] handShakResponse = messagePacking.pack().array();
		ByteBuffer buffer = ByteBuffer.allocate(handShakResponse.length);
		buffer.put(handShakResponse);
		buffer.flip();
		try {
			message.getChannel().write(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
