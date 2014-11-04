package com.bluefrost.nio.servernclient.listeners;

import com.bluefrost.nio.servernclient.events.ConnectionEvent;
import com.bluefrost.nio.servernclient.events.EventSystemWrapper.EventSystem.EventHandler;
import com.bluefrost.nio.servernclient.events.EventSystemWrapper.EventSystem.Listener;

public class ConnectionListener implements Listener{

	
	@EventHandler
	public void onConnectionEvent(ConnectionEvent event){
		try{
			System.out.println("Accepting Connection from " + event.getSocketChannel().getLocalAddress());
			
		}catch(Exception e){e.printStackTrace();}
	}
}
