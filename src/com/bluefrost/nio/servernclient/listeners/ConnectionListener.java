package com.bluefrost.nio.servernclient.listeners;

import java.nio.channels.SocketChannel;

import bluefrost.serializable.objects.v1.KeyObject;

import com.bluefrost.encryption.Crypto;
import com.bluefrost.nio.servernclient.events.EventSystemWrapper.EventSystem.EventHandler;
import com.bluefrost.nio.servernclient.events.EventSystemWrapper.EventSystem.Listener;
import com.bluefrost.nio.servernclient.main.Main;
import com.bluefrost.nio.servernclient.useraccess.IPMap;

public class ConnectionListener implements Listener{

	
	@EventHandler
	public void onConnectionEvent(ConnectionEvent event){
		try{
			if(!IPMap.put(event.getSocketChannel().getLocalAddress())){
				try{
					event.getSocketChannel().socket().getOutputStream().write(new String("We Are Full!").getBytes());
					event.getSocketChannel().socket().getOutputStream().flush();
				}catch(Exception e){}
				event.setCanceled(true);
				return;
			}
			System.out.println("Accepting Connection from " + event.getSocketChannel().getLocalAddress());
			Main.getNIOS().send(event.getSocketChannel(), new KeyObject(Crypto.getPubKey()).toByteArray());
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
