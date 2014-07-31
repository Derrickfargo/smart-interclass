package cn.com.incito.classroom.transition;

import android.content.Context;

import static org.jboss.netty.channel.Channels.*;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

public class ClientPipelineFactory implements ChannelPipelineFactory {

    private MessageListener messageListener;

    public ClientPipelineFactory(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    public ChannelPipeline getPipeline() throws Exception {
        // Create a default pipeline implementation.
        ChannelPipeline pipeline = Channels.pipeline();
        pipeline.addLast("decoder", new ChannelDecoder());
        pipeline.addLast("encoder", new ChannelEncoder());

        // and then business logic.
        pipeline.addLast("handler", new ClientResponseHandler(messageListener));
        return pipeline;
    }
}
