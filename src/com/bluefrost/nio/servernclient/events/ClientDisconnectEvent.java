package com.bluefrost.nio.servernclient.events;

import java.nio.channels.SocketChannel;

/*
 * Created by:
 * Blue/CyanFrost 
 * Ellie/Eleanor <3
 * 
 * Date: some time in October.
 */

public class ClientDisconnectEvent {

	private SocketChannel sc;
	public SocketChannel getSocketChannel(){return sc;}
	
	
	public ClientDisconnectEvent(SocketChannel channel){
		sc = channel;
	}
	
	
}
