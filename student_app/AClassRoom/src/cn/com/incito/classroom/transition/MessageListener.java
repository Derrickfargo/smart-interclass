package cn.com.incito.classroom.transition;

import org.jboss.netty.channel.MessageEvent;

public interface MessageListener {
    public void onMessageReceived(MessageEvent e);
}