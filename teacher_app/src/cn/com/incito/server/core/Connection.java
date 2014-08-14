package cn.com.incito.server.core;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class Connection {
	private SocketChannel channel;
	private long lastActTime = 0;

	Connection(SocketChannel channel) throws IOException {
		this.channel = channel;
	}

	public synchronized long getLastActTime() {
		return lastActTime;
	}
}
