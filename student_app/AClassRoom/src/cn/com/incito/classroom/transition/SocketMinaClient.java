package cn.com.incito.classroom.transition;

import com.alibaba.fastjson.JSONObject;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Set;

import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.constants.Constant;
import cn.com.incito.classroom.transition.handler.TimeServerHandler;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.core.MessageParser;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;

/**
 * Created by popoy on 2014/7/23.
 */
public class SocketMinaClient extends Thread {
    public SocketConnector socketConnector;
    ConnectFuture cf;
    private static SocketMinaClient instance = null;

    public static SocketMinaClient getInstance() {
        if (instance == null) {
            instance = new SocketMinaClient();
        }
        return instance;
    }

    private SocketMinaClient() {

    }

    /**
     * 缺省连接超时时间
     */
    public static final int DEFAULT_CONNECT_TIMEOUT = 5;

    public void getconnect() {
        socketConnector = new NioSocketConnector();

        // 长连接
        // socketConnector.getSessionConfig().setKeepAlive(true);
        socketConnector.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT);
        socketConnector.getFilterChain().addLast("logger", new LoggingFilter());
        socketConnector.getFilterChain().addLast(
                "codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8")))); // 设置编码过滤器
        TimeServerHandler ioHandler = new TimeServerHandler();
        socketConnector.setHandler(ioHandler);
        socketConnector.getSessionConfig().setReadBufferSize(2048);
        socketConnector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
        InetSocketAddress addr = new InetSocketAddress(Constant.IP, Constant.PORT);
        cf = socketConnector.connect(addr);
    }

    public void sendMessage(final Object msg) {
        try {
            if (cf == null) {
                getconnect();
            }
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

    @Override
    public void run() {
        try {
            //客户端向服务器端发起建立连接请求
            getconnect();
            MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_HAND_SHAKE);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("imei", MyApplication.deviceId);
            messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(jsonObject.toJSONString()));
            sendMessage(messagePacking);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SocketConnector getSocketConnector() {
        return socketConnector;
    }

    public void setSocketConnector(SocketConnector socketConnector) {
        this.socketConnector = socketConnector;
    }


}
