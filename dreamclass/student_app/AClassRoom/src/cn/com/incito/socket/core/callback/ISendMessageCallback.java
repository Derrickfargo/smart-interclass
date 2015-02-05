package cn.com.incito.socket.core.callback;

/**
* @ClassName: ISendMessageCallback 
* @Description: 发送消息的接口回调 对成功与失败做不同操作
* @author hm
* @date 2015年1月31日 上午9:49:06 
*
 */
public interface ISendMessageCallback {
	
	/**
	* @author hm 
	* @date 2015年1月31日 上午9:51:14 
	* @Title: sendSuccess 
	* @Description: 消息发送成功回调此方法
	* @param  msgId  消息编号
	* @return void    返回类型 
	* @throws
	 */
	public void sendSuccess(byte msgId);
	
	/**
	* @author hm 
	* @date 2015年1月31日 上午9:52:13 
	* @Title: sendError 
	* @Description: 消息发送失败回调此方法
	* @param  msgId   消息编号
	* @param  cause    失败原因 
	* @return void    返回类型 
	* @throws
	 */
	public void sendError(byte msgId,Throwable cause);
	
	/**
	* @author hm
	* @date 2015年1月31日 上午9:54:45 
	* @Title: channelIsNull 
	* @Description:  通道没有连接  
	* @param  msgId   消息编号      
	* @return void    返回类型 
	* @throws
	 */
	public void channelIsNull(byte msgId);

}
