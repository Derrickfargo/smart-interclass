package cn.com.incito.socket.core;

import java.util.HashMap;
import java.util.Map;

import cn.com.incito.classroom.utils.ApiClient;
import cn.com.incito.common.utils.LogUtil;
import cn.com.incito.socket.handler.DeviceBindHandler;
import cn.com.incito.socket.handler.DeviceHasBindHandler;
import cn.com.incito.socket.handler.DeviceLoginHandler;
import cn.com.incito.socket.handler.DistributePaperHandler;
import cn.com.incito.socket.handler.DownLoadApkHandler;
import cn.com.incito.socket.handler.EvaluateCompleteHandler;
import cn.com.incito.socket.handler.EvaluateHandler;
import cn.com.incito.socket.handler.GroupEditHandler;
import cn.com.incito.socket.handler.GroupListHandler;
import cn.com.incito.socket.handler.GroupSubmitHandler;
import cn.com.incito.socket.handler.LockScreenHandler;
import cn.com.incito.socket.handler.RandomGroupHandler;
import cn.com.incito.socket.handler.ResponderEndHandler;
import cn.com.incito.socket.handler.ResponderHandler;
import cn.com.incito.socket.handler.SavePaperHandler;
import cn.com.incito.socket.handler.SavePaperResultHandler;
import cn.com.incito.socket.handler.StudentLoginHandler;
import cn.com.incito.socket.handler.VoteGroupInfoHandler;

/**
 * 消息处理器列表
 * 该类用来维护消息和消息处理器的关系
 *
 * @author 刘世平
 */
public final class MessageHandlerResource {
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
        //抢答消息
        handlerResources.put(Message.MESSAGE_STUDENT_RESPONDER, ResponderHandler.class);
        //随机分组消息
        handlerResources.put(Message.MESSAGE_RANDOM_GROUP, RandomGroupHandler.class);
        //互评
        handlerResources.put(Message.MESSAGE_STUDENT_EVALUATE, EvaluateHandler.class);
        //结束抢答
        handlerResources.put(Message.MESSAGE_RESPONDER_END, ResponderEndHandler.class);
        //结束作业互评
        handlerResources.put(Message.MESSAGE_QUIZ_FEEDBACK_COMPLETE, EvaluateCompleteHandler.class);
        //下载更新包
        handlerResources.put(Message.MESSAGE_APK_UPTE, DownLoadApkHandler.class);
        
    }

    public MessageHandler getMessageHandler(Byte key) {
        // 查询是否有该消息ID的消息处理器
        if (handlerResources.containsKey(key)) {
            try {
                // 通过反射取得对应的处理器
                return handlerResources.get(key).newInstance();
            } catch (Exception e) {
            	ApiClient.uploadErrorLog(e.getMessage());
            	LogUtil.e("获取MessageHandler出错:", e.getCause());
            }
        }
        return null;
    }

}
