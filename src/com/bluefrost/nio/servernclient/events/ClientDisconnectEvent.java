package com.bluefrost.nio.servernclient.events;

import java.nio.channels.SocketChannel;

public class ClientDisconnectEvent {

	private SocketChannel sc;
	public SocketChannel getSocketChannel(){return sc;}
	
	
	public ClientDisconnectEvent(SocketChannel channel){
		sc = channel;
	}
	
	
}
