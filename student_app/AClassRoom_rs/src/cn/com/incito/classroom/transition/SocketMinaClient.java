package cn.com.incito.classroom.transition;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.ConnectException;
import java.net.InetSocketAddress;

import cn.com.incito.classroom.constants.Constant;
import cn.com.incito.classroom.transition.handler.TimeServerHandler;

/**
 * Created by popoy on 2014/7/23.
 */
public class SocketMinaClient {
    public SocketConnector socketConnector;

    /**
     * 缺省连接超时时间
     */
    public static final int DEFAULT_CONNECT_TIMEOUT = 5;

    public SocketMinaClient() {
        init();
    }

    public void init() {
        socketConnector = new NioSocketConnector();

        // 长连接
        // socketConnector.getSessionConfig().setKeepAlive(true);
        socketConnector.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT);

        socketConnector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory()));
        TimeServerHandler ioHandler = new TimeServerHandler();
        socketConnector.setHandler(ioHandler);
        socketConnector.getSessionConfig().setReadBufferSize(2048);
        socketConnector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
    }

    public void sendMessage(final Object msg) {
        InetSocketAddress addr = new InetSocketAddress(Constant.IP, Constant.PORT);
        ConnectFuture cf = socketConnector.connect(addr);
        try {
            cf.awaitUninterruptibly();
            cf.getSession().write(msg);
            System.out.println("send message " + msg);
        } catch (RuntimeIoException e) {
            if (e.getCause() instanceof ConnectException) {
                try {
                    if (cf.isConnected()) {
                        cf.getSession().close();
                    }
                } catch (RuntimeIoException e1) {
                }
            }
        }
    }

    public SocketConnector getSocketConnector() {
        return socketConnector;
    }

    public void setSocketConnector(SocketConnector socketConnector) {
        this.socketConnector = socketConnector;
    }


}
