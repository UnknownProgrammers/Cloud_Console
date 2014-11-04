package com.bluefrost.nio.servernclient.events;

import java.nio.channels.SocketChannel;

public class ConnectionEvent {


	private SocketChannel sc;
	public SocketChannel getSocketChannel(){return sc;}
	
	private boolean canceled = false;
	public void setCanceled(boolean b){canceled = b;}
	public boolean isCanceled(){return canceled;}
	
	
	public ConnectionEvent(SocketChannel channel){
		sc = channel;
	}
	
}
