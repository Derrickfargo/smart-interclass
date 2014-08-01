package cn.com.incito.socket.core;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息处理器列表
 * 该类用来维护消息和消息处理器的关系
 *
 * @author 刘世平
 */
public final class MessageHandlerResource {

    private static MessageHandlerResource resources;
    //    private Map<Byte, Class<? extends MessageHandler>> handlerResources;
    private Map<Byte, MessageHandler> handlerResources=new HashMap<Byte, MessageHandler>();

    public static MessageHandlerResource getHandlerResources() {
        if (resources == null) {
            resources = new MessageHandlerResource();
        }
        return resources;
    }

    public void putHandlerResource(byte key, MessageHandler messageHandler) {
        handlerResources.put(key, messageHandler);
    }

    private MessageHandlerResource() {
//        handlerResources = new HashMap<Byte, Class<? extends MessageHandler>>();
//        //心跳消息
//        handlerResources.put(MessageInfo.MESSAGE_HEART_BEAT, HeartbeatHandler.class);
//        //程序启动初始化消息
//        handlerResources.put(MessageInfo.MESSAGE_GROUP_LIST, SytemInitHandler1.class);
//        //学生登陆消息
//        handlerResources.put(MessageInfo.MESSAGE_STUDENT_LOGIN, LoginHandler.class);
//        //课桌绑定
//        handlerResources.put(MessageInfo.MESSAGE_DEVICE_BIND, BindDeskHandler.class);
//        //判断设备是否已经绑定课桌
//        handlerResources.put(MessageInfo.MESSAGE_DEVICE_HAS_BIND, DeviceHasBindHandler.class);

    }

    public MessageHandler getMessageHandler(Byte key) {
        // 查询是否有该消息ID的消息处理器
        if (handlerResources.containsKey(key)) {
            try {
                // 通过反射取得对应的处理器
                return handlerResources.get(key);
            } catch (Exception e) {
                System.out.println("获取MessageHandler出错:" + e.getMessage());
                return null;
            }
        }
        return null;
    }

}
