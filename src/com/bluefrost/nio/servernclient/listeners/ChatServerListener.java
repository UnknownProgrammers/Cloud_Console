package com.bluefrost.nio.servernclient.listeners;


import com.bluefrost.nio.servernclient.events.EventSystemWrapper.EventSystem.EventHandler;
import com.bluefrost.nio.servernclient.events.EventSystemWrapper.EventSystem.Listener;
import com.bluefrost.nio.servernclient.useraccess.ClientManager;

public class ChatServerListener implements Listener{

	@EventHandler
	public void _v1(bluefrost.serializable.objects.v1.CM event){
		ClientManager.say(new bluefrost.serializable.objects.v1.CM(ClientManager.get(event.getSocketChannel()).getDisplayName()+": " +event.getMessage()));
	}
	
}
