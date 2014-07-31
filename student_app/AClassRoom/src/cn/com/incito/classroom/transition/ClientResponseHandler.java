package cn.com.incito.classroom.transition;

import com.activeandroid.util.Log;

import java.util.concurrent.atomic.AtomicLong;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class ClientResponseHandler extends SimpleChannelUpstreamHandler {
    private static String TAG = ClientResponseHandler.class.getName();

    //    private final AtomicLong transferredBytes = new AtomicLong();
    private MessageListener messsageListener = null;

    ClientResponseHandler(MessageListener messsageListener) {
        this.messsageListener = messsageListener;
    }

    public ClientResponseHandler() {

    }

    @Override
    public void channelConnected(
            ChannelHandlerContext ctx, ChannelStateEvent e) {
        e.getChannel().write("asdf");
//        SocketAddress remoteAddress = e.getChannel().getRemoteAddress();
//    	 //e.getChannel().write(firstMessage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        messsageListener.onMessageReceived(e);
//        transferredBytes.addAndGet(((ChannelBuffer) e.getMessage()).readableBytes());
//        e.getChannel().write(e.getMessage());
//        System.out.println("messageInfo received");
    }

    @Override
    public void exceptionCaught(
            ChannelHandlerContext ctx, ExceptionEvent e) {
        Log.i(TAG, "Unexpected exception from downstream.",
                e.getCause());
        e.getChannel().close();
    }
}
