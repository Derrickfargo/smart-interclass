package cn.com.incito.socket.core;

/**
 * 
 * 通讯连接接口
 * 
 */
public interface ISocket
{
    /**
     *获取连接
     */
    void startConnection();
    /**
     * 关闭连接
     */
    void stopConnection();
}

