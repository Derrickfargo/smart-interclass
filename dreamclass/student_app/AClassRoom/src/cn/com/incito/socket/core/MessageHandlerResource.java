package cn.com.incito.socket.core;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;
import cn.com.incito.classroom.ui.activity.EvaluateHandlerActivity;
import cn.com.incito.classroom.utils.ApiClient;
import cn.com.incito.classroom.utils.Utils;
import cn.com.incito.socket.handler.DeviceBindHandler;
import cn.com.incito.socket.handler.DeviceHasBindHandler;
import cn.com.incito.socket.handler.DeviceLoginHandler;
import cn.com.incito.socket.handler.DistributePaperHandler;
import cn.com.incito.socket.handler.EvaluateHandler;
import cn.com.incito.socket.handler.GroupEditHandler;
import cn.com.incito.socket.handler.GroupListHandler;
import cn.com.incito.socket.handler.GroupSubmitHandler;
import cn.com.incito.socket.handler.HeartbeatHandler;
import cn.com.incito.socket.handler.LockScreenHandler;
import cn.com.incito.socket.handler.SavePaperHandler;
import cn.com.incito.socket.handler.SavePaperResultHandler;
import cn.com.incito.socket.handler.StudentLoginHandler;
import cn.com.incito.socket.handler.VoteGroupInfoHandler;

import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;

/**
 * 消息处理器列表
 * 该类用来维护消息和消息处理器的关系
 *
 * @author 刘世平
 */
public final class MessageHandlerResource {
	public static final Logger Logger = LoggerFactory.getLogger();
    private static MessageHandlerResource resources;
    private Map<Byte, Class<? extends MessageHandler>> handlerResources;

    public static MessageHandlerResource getHandlerResources() {
        if (resources == null) {
            resources = new MessageHandlerResource();
        }
        return resources;
    }

    private MessageHandlerResource() {
        handlerResources = new HashMap<Byte, Class<? extends MessageHandler>>();
        //设备登陆消息回复，用于启动心跳
        handlerResources.put(Message.MESSAGE_HAND_SHAKE, DeviceLoginHandler.class);
        //心跳消息
        handlerResources.put(Message.MESSAGE_HEART_BEAT, HeartbeatHandler.class);
        //获取分组消息
        handlerResources.put(Message.MESSAGE_GROUP_LIST, GroupListHandler.class);
        //学生登陆消息
        handlerResources.put(Message.MESSAGE_STUDENT_LOGIN, StudentLoginHandler.class);
        //设备是否绑定消息
        handlerResources.put(Message.MESSAGE_DEVICE_HAS_BIND, DeviceHasBindHandler.class);
        //设备绑定消息
        handlerResources.put(Message.MESSAGE_DEVICE_BIND, DeviceBindHandler.class);
        //编辑小组信息
        handlerResources.put(Message.MESSAGE_GROUP_EDIT, GroupEditHandler.class);
        //确认小组信息
        handlerResources.put(Message.MESSAGE_GROUP_VOTE, VoteGroupInfoHandler.class);
        //提交小组信息
        handlerResources.put(Message.MESSAGE_GROUP_CONFIRM, GroupSubmitHandler.class);
        //收到作业
        handlerResources.put(Message.MESSAGE_DISTRIBUTE_PAPER, DistributePaperHandler.class);
        //保存作业图片
        handlerResources.put(Message.MESSAGE_SAVE_PAPER, SavePaperHandler.class);
        //保存作业成功与否的信息
        handlerResources.put(Message.MESSAGE_SAVE_PAPER_RESULT, SavePaperResultHandler.class);
        //解锁屏信息
        handlerResources.put(Message.MESSAGE_LOCK_SCREEN, LockScreenHandler.class);
        //学生互评
        handlerResources.put(Message.MESSAGE_STUDENT_EVALUATE, EvaluateHandler.class);
    }

    public MessageHandler getMessageHandler(Byte key) {
        // 查询是否有该消息ID的消息处理器
        if (handlerResources.containsKey(key)) {
            try {
                // 通过反射取得对应的处理器
                return handlerResources.get(key).newInstance();
            } catch (Exception e) {
            	ApiClient.uploadErrorLog(e.getMessage());
            	Logger.debug(Utils.getTime()+"MessageHandlerResource+"+"获取MessageHandler出错:" + e.getMessage());
                Log.e("MessageHandlerResource", "获取MessageHandler出错:" + e.getMessage());
                return null;
            }
        }
        return null;
    }

}
