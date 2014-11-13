package com.bluefrost.nio.servernclient.listeners;

import bluefrost.serializable.objects.v1.KeyObject;

import com.bluefrost.encryption.Crypto;
import com.bluefrost.nio.servernclient.events.EventSystemWrapper.EventSystem.EventHandler;
import com.bluefrost.nio.servernclient.events.EventSystemWrapper.EventSystem.Listener;
import com.bluefrost.nio.servernclient.main.Main;
import com.bluefrost.nio.servernclient.useraccess.ClientManager;
import com.bluefrost.nio.servernclient.useraccess.ClientManager.Client;

public class KeyObjectListener implements Listener{

	@EventHandler
	public void onEvent(KeyObject event){
		try{
			synchronized(ClientManager.map.inverse().get(event.getSocketChannel())){
				Client c = ClientManager.map.inverse().get(event.getSocketChannel());
				if(c.loggedin == true){
					c.setKey(Crypto.randomAESKey());
					Main.getNIOS().send(event.getSocketChannel(), new KeyObject(c.getKey()).encrypt(event.k).toByteArray());
				}
			}
		}catch(Exception e){e.printStackTrace();}
	}
}
