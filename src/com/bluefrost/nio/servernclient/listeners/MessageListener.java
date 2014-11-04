package com.bluefrost.nio.servernclient.listeners;

import com.bluefrost.nio.servernclient.events.EventSystemWrapper.EventSystem.EventHandler;
import com.bluefrost.nio.servernclient.events.EventSystemWrapper.EventSystem.Listener;
import com.bluefrost.nio.servernclient.events.MessageEvent;
import com.bluefrost.nio.servernclient.server.ClientManager;

public class MessageListener implements Listener{

	
	@EventHandler
	public void saySomething(MessageEvent event){
		try{
			synchronized(ClientManager.map){
				
				System.out.println(ClientManager.map.inverse().get(event.getSocketChannel()).username+" said " + new String(event.getBytes()));
			}
		}catch(Exception e){e.printStackTrace();}
	}
}
