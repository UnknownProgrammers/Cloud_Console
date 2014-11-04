package com.bluefrost.nio.servernclient.listeners;

import bluefrost.serializable.objects.v1.EncryptableObject;
import bluefrost.serializable.objects.v1.LoginObject;

import com.bluefrost.nio.servernclient.events.EventSystemWrapper.EventSystem.EventHandler;
import com.bluefrost.nio.servernclient.events.EventSystemWrapper.EventSystem.Listener;
import com.bluefrost.nio.servernclient.events.MessageEvent;
import com.bluefrost.nio.servernclient.main.Main;
import com.bluefrost.nio.servernclient.server.ClientManager;
import com.bluefrost.nio.servernclient.server.ClientManager.Client;
import com.bluefrost.sql.light.usermanagement.UserManager;

public class MessageListener implements Listener{

	
	@EventHandler
	public void saySomething(MessageEvent event){
		try{
			synchronized(ClientManager.map){
				try{
				//	Apples a = Apples.fromByteArray(event.getBytes());
					//System.out.println(a.a);
					//return;
					EncryptableObject eo = EncryptableObject.fromByteArray(event.getBytes());
					if(eo instanceof LoginObject){
						if(UserManager.validateUser(((LoginObject)eo).username, ((LoginObject)eo).password)){
							System.out.println("User Validated as " + ((LoginObject)eo).username+":" + ((LoginObject)eo).password);
							((Client)ClientManager.get(event.getSocketChannel())).username = ((LoginObject)eo).username;
							((Client)ClientManager.get(event.getSocketChannel())).password = ((LoginObject)eo).password;
							Main.nios.send(event.getSocketChannel(), new String("You logged in as " + ((LoginObject)eo).username).getBytes());
						}
						
						return;
					}
				}catch(Exception e){}
				System.out.println(ClientManager.map.inverse().get(event.getSocketChannel()).username+" said " + new String(event.getBytes()));
				
			}
		}catch(Exception e){e.printStackTrace();}
	}
}
