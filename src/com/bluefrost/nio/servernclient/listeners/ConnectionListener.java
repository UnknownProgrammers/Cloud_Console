package com.bluefrost.nio.servernclient.listeners;

import java.nio.channels.SocketChannel;

import bluefrost.serializable.objects.v1.Apples;

import com.bluefrost.nio.servernclient.events.EventSystemWrapper.EventSystem.EventHandler;
import com.bluefrost.nio.servernclient.events.EventSystemWrapper.EventSystem.Listener;
import com.bluefrost.nio.servernclient.main.Main;

public class ConnectionListener implements Listener{

	
	@EventHandler
	public void onConnectionEvent(ConnectionEvent event){
		try{
			System.out.println("Accepting Connection from " + event.getSocketChannel().getLocalAddress());
			Main.getNIOS().send(event.getSocketChannel(), new Apples("Hi, This is an Apple!").toByteArray());
		}catch(Exception e){e.printStackTrace();}
	}
	
	

public static class ConnectionEvent {


	private SocketChannel sc;
	public SocketChannel getSocketChannel(){return sc;}
	
	private boolean canceled = false;
	public void setCanceled(boolean b){canceled = b;}
	public boolean isCanceled(){return canceled;}
	
	
	public ConnectionEvent(SocketChannel channel){
		sc = channel;
	}
	
}

}
