package com.bluefrost.nio.servernclient.listeners;

import bluefrost.serializable.objects.v1.EncryptableObject;
import bluefrost.serializable.objects.v1.EncryptedObject;
import bluefrost.serializable.objects.v1.Utils;

import com.bluefrost.nio.servernclient.events.EventSystemWrapper.EventSystem.EventHandler;
import com.bluefrost.nio.servernclient.events.EventSystemWrapper.EventSystem.Listener;
import com.bluefrost.nio.servernclient.events.MessageEvent;
import com.bluefrost.nio.servernclient.main.Main;
import com.bluefrost.nio.servernclient.server.ClientManager;
import com.bluefrost.nio.servernclient.server.ClientManager.Client;

public class MessageListener implements Listener{


	@EventHandler
	public void saySomething(MessageEvent event){
		try{
			synchronized(ClientManager.map.inverse().get(event.getSocketChannel())){
				Client c = ClientManager.map.inverse().get(event.getSocketChannel());
				Object o = Utils.fromByteArray(event.getBytes());
				if(o instanceof EncryptedObject){
					EncryptableObject eo = ((EncryptedObject)o).decrypt(c.getKey());
					eo.setSocketChannel(event.getSocketChannel());
					Main.getEventSystem().listen(eo);
				}else if(o instanceof EncryptableObject){
					EncryptableObject eo = (EncryptableObject)o;
					eo.setSocketChannel(event.getSocketChannel());
					Main.getEventSystem().listen(eo);
				}
			}
		}catch(Exception e){e.printStackTrace();}
	}
}
