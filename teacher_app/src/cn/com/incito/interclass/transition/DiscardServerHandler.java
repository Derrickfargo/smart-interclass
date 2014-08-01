package cn.com.incito.interclass.transition;  
  
import org.jboss.netty.buffer.ChannelBuffer;  
import org.jboss.netty.buffer.ChannelBuffers;  
import org.jboss.netty.channel.*;  
  
/** 
 * Created by IntelliJ IDEA. 
 * User: flychao88 
 * Date: 12-6-6 
 * Time: 上午10:10 
 * To change this template use File | Settings | File Templates. 
 */  
public class DiscardServerHandler extends SimpleChannelUpstreamHandler  {  
    @Override  
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {  
       System.out.println("服务器接收1："+e.getMessage());  
    }  
      
    @Override  
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {  
        e.getCause().printStackTrace();  
        Channel ch = e.getChannel();  
        ch.close();  
    }  
}  