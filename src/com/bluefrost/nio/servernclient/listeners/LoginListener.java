package com.bluefrost.nio.servernclient.listeners;

import bluefrost.serializable.objects.v1.LoginObject;

import com.bluefrost.nio.servernclient.events.EventSystemWrapper.EventSystem.EventHandler;
import com.bluefrost.nio.servernclient.events.EventSystemWrapper.EventSystem.Listener;
import com.bluefrost.nio.servernclient.server.ClientManager;
import com.bluefrost.sql.light.usermanagement.UserManager;

public class LoginListener implements Listener{

	@EventHandler
	public void onLoginEvent(LoginObject event){
		try{
			if(UserManager.validateUser(event.getUsername(), event.getPassword())){
				System.out.println(event.getSocketChannel().getLocalAddress()+ " Logged In As " +event.getUsername() );
				synchronized(ClientManager.map){
					ClientManager.map.inverse().get(event.getSocketChannel()).username = event.getUsername();
					ClientManager.map.inverse().get(event.getSocketChannel()).password = event.getPassword();
				}
			}else{
				System.out.println(event.getSocketChannel().getLocalAddress()+ " Failed To Log In As " +event.getUsername() );
			}
		}catch(Exception e){}
	}
	
	
	
	
}
