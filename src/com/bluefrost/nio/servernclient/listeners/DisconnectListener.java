package com.bluefrost.nio.servernclient.listeners;

import java.nio.channels.SocketChannel;

import com.bluefrost.nio.servernclient.events.EventSystemWrapper.EventSystem.EventHandler;
import com.bluefrost.nio.servernclient.events.EventSystemWrapper.EventSystem.Listener;
import com.bluefrost.nio.servernclient.useraccess.ClientManager;
import com.bluefrost.nio.servernclient.useraccess.IPMap;
import com.bluefrost.nio.servernclient.useraccess.ClientManager.Client;

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
			Client c = ClientManager.get(event.sc);
			if(c.isLoggedIn()){System.out.println(c.getUsername() + " Disconnected!");}
			else{System.out.println(event.sc.getLocalAddress()+" Disconnected!");}
			ClientManager.remove(event.getSocketChannel());
			IPMap.remove(event.getSocketChannel().getLocalAddress());
		}catch(Exception e){e.printStackTrace();}
	} 




	public static class ClientDisconnectEvent {

		private SocketChannel sc;
		public SocketChannel getSocketChannel(){return sc;}


		public ClientDisconnectEvent(SocketChannel channel){
			sc = channel;
		}


	}

}
