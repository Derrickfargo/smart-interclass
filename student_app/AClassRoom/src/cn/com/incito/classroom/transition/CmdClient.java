package cn.com.incito.classroom.transition;

import android.content.Context;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

public class CmdClient {
    private ClientBootstrap bootstrap;
    private ChannelFuture channelFuture;
    private Channel channel;
    private static final CmdClient client = new CmdClient(); // 单例实现，用来取channel
    private Context mContext;

    private CmdClient() {
    }

    public static CmdClient getInstance() {
        return client;
    }


    public int start(MessageListener messageListener, String ip, int port) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("java.net.preferIPv6Addresses", "false");
        bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool()));
        bootstrap.setPipelineFactory(new ClientPipelineFactory(messageListener));
        bootstrap.setOption("tcpNoDelay", true);
        bootstrap.setOption("keepAlive", true);
        channelFuture = bootstrap.connect(new InetSocketAddress(ip, port)
        );
        channelFuture.awaitUninterruptibly();
        channel = channelFuture.awaitUninterruptibly().

                getChannel();

        return 1;
    }

    public void stop() {
        channelFuture.awaitUninterruptibly();
        if (!channelFuture.isSuccess()) {
            channelFuture.getCause().printStackTrace();
        }
        channelFuture.getChannel().getCloseFuture().awaitUninterruptibly();
        bootstrap.releaseExternalResources();
    }

    public void sendMessage(String msg) {
        channel = channelFuture.awaitUninterruptibly().getChannel();
        channel.write(msg);
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public ClientBootstrap getBootstrap() {
        return bootstrap;
    }

    public void setBootstrap(ClientBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    public ChannelFuture getChannelFuture() {
        return channelFuture;
    }

    public void setChannelFuture(ChannelFuture channelFuture) {
        this.channelFuture = channelFuture;
    }

}