package cn.com.incito.socket.handler;

import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.socket.core.MessageHandler;

public class IpSettingHandler extends MessageHandler{

	@Override
	protected void handleMessage() {
		String server_ip=data.getString("server_ip");
		String server_port=data.getString("server_port");
		Constants.setSERVER_IP(server_ip);
		Constants.setSERVER_PORT(server_port);
		
	}

}
