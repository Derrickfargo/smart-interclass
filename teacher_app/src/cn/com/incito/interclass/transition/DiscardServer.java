package cn.com.incito.interclass.transition;  
  
import org.jboss.netty.bootstrap.ServerBootstrap;  
import org.jboss.netty.channel.ChannelFactory;  
import org.jboss.netty.channel.ChannelPipeline;  
import org.jboss.netty.channel.ChannelPipelineFactory;  
import org.jboss.netty.channel.Channels;  
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;  
import org.jboss.netty.handler.codec.string.StringDecoder;  
import org.jboss.netty.handler.codec.string.StringEncoder;  
  
import java.net.InetSocketAddress;  
import java.util.concurrent.Executors;  
  
/** 
 * Created by IntelliJ IDEA. 
 * User: flychao88 
 * Date: 12-6-6 
 * Time: 上午10:14 
 * To change this template use File | Settings | File Templates. 
 */  
public class DiscardServer {  
    public static void main(String[] args) throws Exception {  
        ChannelFactory factory = new NioServerSocketChannelFactory(  
            Executors.newCachedThreadPool(),  
            Executors.newCachedThreadPool());  
        ServerBootstrap bootstrap = new ServerBootstrap (factory);  
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {  
            public ChannelPipeline getPipeline() {  
                 ChannelPipeline pipeline = Channels.pipeline();  
                pipeline.addLast("encode",new StringEncoder());  
                pipeline.addLast("decode",new StringDecoder());  
                pipeline.addLast("handler",new DiscardServerHandler());  
                return pipeline;  
            }  
        });  
        bootstrap.setOption("child.tcpNoDelay", true);  
        bootstrap.setOption("child.keepAlive", true);  
        bootstrap.bind(new InetSocketAddress(9002));  
    }  
}  