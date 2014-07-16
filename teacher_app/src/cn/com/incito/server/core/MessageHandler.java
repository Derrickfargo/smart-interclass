package cn.com.incito.server.core;

import java.io.Serializable;


/**
 * 消息对应的处理器接口
 *存放消息对应的处理逻辑提供共有的抽象接口方法
 *@author 刘世平
 */
public interface MessageHandler
{
    /** 
     * 消息对应的处理器
     * 存放消息对应的处理逻辑，在消息分发时使用
     * 
     * @param msg 被处理消息
     */
    Serializable handleMessage(Message msg);
}
