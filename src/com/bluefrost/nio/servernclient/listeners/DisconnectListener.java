package com.bluefrost.nio.servernclient.listeners;

import com.bluefrost.nio.servernclient.events.ClientDisconnectEvent;
import com.bluefrost.nio.servernclient.events.EventSystemWrapper.EventSystem.EventHandler;
import com.bluefrost.nio.servernclient.events.EventSystemWrapper.EventSystem.Listener;
import com.bluefrost.nio.servernclient.server.ClientManager;

/*
 * Created by:
 * Blue/CyanFrost 
 * Ellie/Eleanor <3
 * 
 * Date: some time in October.
 */

public class DisconnectListener implements Listener{
	
	
	
	@EventHandler
	public void onDisconnectEvent(ClientDisconnectEvent event){
		try{
			System.out.println(event.getSocketChannel().getLocalAddress() +" Disconnected!");
			ClientManager.remove(event.getSocketChannel());
		}catch(Exception e){e.printStackTrace();}
	} 
}
 