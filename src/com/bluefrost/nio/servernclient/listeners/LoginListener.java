package com.bluefrost.nio.servernclient.listeners;

import bluefrost.serializable.objects.v1.LoggedInSuccessObject;
import bluefrost.serializable.objects.v1.LoginObject;

import com.bluefrost.nio.servernclient.events.EventSystemWrapper.EventSystem.EventHandler;
import com.bluefrost.nio.servernclient.events.EventSystemWrapper.EventSystem.Listener;
import com.bluefrost.nio.servernclient.main.Main;
import com.bluefrost.nio.servernclient.useraccess.ClientManager;
import com.bluefrost.nio.servernclient.useraccess.ClientManager.Client;
import com.bluefrost.sql.light.usermanagement.UserBase.User;

public class LoginListener implements Listener{

	@EventHandler
	public void onLoginEvent(LoginObject event){
		try{
			User u;
			if((u = Main.getDefaultUserBase().validateUser(event.getUsername(), event.getPassword()))!= null){
				System.out.println(event.getSocketChannel().getLocalAddress()+ " Logged In As " +event.getUsername() );
				synchronized(ClientManager.map){
					Client c = ClientManager.map.inverse().get(event.getSocketChannel());
					c.username= event.getUsername();
					c.password = event.getPassword();
					c.loggedin = true;
					c.userData = u;
					Main.getNIOS().send(event.getSocketChannel(), new LoggedInSuccessObject().toByteArray());
					//Key k = Crypto.randomAESKey();
					//c.setKey(k);
					//Main.getNIOS().send(event.getSocketChannel(),new KeyObject(k).encrypt(event.getKey()).toByteArray());
						//c.setKey();
				}
			}else{
				System.out.println(event.getSocketChannel().getLocalAddress()+ " Failed To Log In As " +event.getUsername() );
			}
		}catch(Exception e){e.printStackTrace();}
	}
	
	
	
	
}
