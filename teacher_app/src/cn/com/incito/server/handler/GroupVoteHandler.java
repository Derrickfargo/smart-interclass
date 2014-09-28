package cn.com.incito.server.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.incito.interclass.po.Group;
import cn.com.incito.server.api.ApiClient;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.exception.AppException;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;
import cn.com.incito.server.utils.JSONUtils;

/**
 * 小组信息投票处理器
 * 
 * @author 刘世平
 * 
 */
public class GroupVoteHandler extends MessageHandler {
	private Logger logger = Logger.getLogger(GroupVoteHandler.class.getName());
	
	@Override
	public void handleMessage() {
		logger.info("收到小组投票消息:" + data);
		Integer id = data.getInteger("id");
		Integer vote = data.getInteger("vote");
		Application app = Application.getInstance();
		List<SocketChannel> channels = service.getGroupSocketChannelByGroupId(id);
		List<Integer> voteList = app.getTempVote().get(id);
		if(voteList == null){
			voteList = new ArrayList<Integer>();
		}
		
		if (vote == 1) {//有人投反对票
			app.getTempVote().remove(id);
			JSONObject json = new JSONObject();
			json.put("code", JSONUtils.SUCCESS);
			JSONObject jsonData = new JSONObject();
			jsonData.put("id", id);
			jsonData.put("agree", false);
			json.put("data", jsonData);
			logger.info("回复小组投票消息（反对）:" + json.toJSONString());
			sendResponse(json.toJSONString(), channels);
			return;
		}
		voteList.add(vote);
		app.getTempVote().put(id, voteList);
		if (channels.size() == voteList.size() || voteList.size() == 4) {
			//更新数据库....
			JSONObject data = app.getTempGroup().get(id);
			String name = data.getString("name");
			String logo = data.getString("logo");
			JSONObject result = null;
			try {
				String retval = ApiClient.updateGroup(id, name, logo);
				result = JSONObject.parseObject(retval);
			} catch (AppException e) {
				e.printStackTrace();
			}
			if(result.getIntValue("code") == 0){
				//清空缓存
				app.getTempVote().remove(id);
				app.getTempGroup().remove(id);
				//发送消息
				JSONObject json = new JSONObject();
				json.put("code", JSONUtils.SUCCESS);
				JSONObject jsonData = new JSONObject();
				jsonData.put("id", id);
				jsonData.put("agree", true);
				json.put("data", jsonData);
				logger.info("回复小组投票消息（成功）:" + json.toJSONString());
				sendResponse(json.toJSONString(), channels);
				//刷新主界面
				Group group = app.getGroupById(id);
				Group temp = JSON.parseObject(result.getString("data"), Group.class);
				group.setName(temp.getName());
				group.setLogo(temp.getLogo());
				app.addGroup(group);
				app.getTempGrouped().add(id);//把当前小组标记为已分组
				app.refresh();
			} else {
				JSONObject json = new JSONObject();
				json.put("code", 1);
				logger.info("回复小组投票消息（失败）:" + json.toJSONString());
				sendResponse(json.toJSONString(), channels);
			}
		} else {
			JSONObject json = new JSONObject();
			json.put("code", 1);
			logger.info("回复小组投票消息（失败）:" + json.toJSONString());
			sendResponse(json.toJSONString(), channels);
		}
	}

	private void sendResponse(String json, List<SocketChannel> channels) {
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_GROUP_VOTE);
        messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(json));
        byte[] messageData = messagePacking.pack().array();
        ByteBuffer buffer = ByteBuffer.allocate(messageData.length);
        for(SocketChannel channel: channels){
			buffer.clear();
			buffer.put(messageData);
			buffer.flip();
			try {
				channel.write(buffer);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}
	}
}
