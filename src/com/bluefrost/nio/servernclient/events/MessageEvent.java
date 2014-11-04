package com.bluefrost.nio.servernclient.events;

import java.nio.channels.SocketChannel;

public class MessageEvent {

	private byte[]  b = null;
	public byte[] getBytes(){return b;}
	
	private SocketChannel sc = null;
	public SocketChannel getSocketChannel(){return sc;}
	
	@Deprecated
	public MessageEvent(byte[] c){
		b = c;
	}
	
	public MessageEvent(byte[] c, SocketChannel sc){
		this.sc = sc;
		b = c;
	}
	
}
