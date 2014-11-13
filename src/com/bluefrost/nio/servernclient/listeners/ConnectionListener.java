package com.bluefrost.nio.servernclient.listeners;

import bluefrost.serializable.objects.v1.Apples;

import com.bluefrost.nio.servernclient.events.ConnectionEvent;
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
}
