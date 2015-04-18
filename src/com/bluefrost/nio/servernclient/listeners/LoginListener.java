package com.bluefrost.nio.servernclient.listeners;

import com.bluefrost.nio.servernclient.events.EventSystemWrapper.EventSystem.EventHandler;
import com.bluefrost.nio.servernclient.events.EventSystemWrapper.EventSystem.Listener;
import com.bluefrost.nio.servernclient.main.Main;
import com.bluefrost.nio.servernclient.useraccess.ClientManager;
import com.bluefrost.sql.light.usermanagement.UserBase.User;

public class LoginListener implements Listener{

	/*
	@EventHandler
	public void _v1(bluefrost.serializable.objects.v1.LoginObject event){
		try{
			User u;
			if((u = Main.getDefaultUserBase().validateUser(event.getUsername(), event.getPassword()))!= null){
				ClientManager.get(event.getSocketChannel()).set(u);
				System.out.println("User " + u.username+" Logged In!");
				Main.getNIOS().send(event.getSocketChannel(), new bluefrost.serializable.objects.v1.LoggedInSuccessObject().toByteArray());
			}
		}catch(Exception e){}
	}
	

	@EventHandler
	public void _v1(bluefrost.serializable.objects.v1.json.LoginObject event){
		try{
			User u;
			if((u = Main.getDefaultUserBase().validateUser(event.getUsername(), event.getPassword()))!= null){
				ClientManager.get(event.getSocketChannel()).set(u);
				System.out.println("User " + u.username+" Logged In!");
				Main.getNIOS().send(event.getSocketChannel(), new bluefrost.serializable.objects.v1.LoggedInSuccessObject().toByteArray());
			}
		}catch(Exception e){}
	}
	//*/


	@EventHandler
	public void _v2(bluefrost.serializable.objects.v1.json.LoginObject event){
		try{
		
			//if(Main.getDefaultUserBase().validateUser(event.getUsername(), event.getPassword(),ClientManager.get(event.getSocketChannel()))){

			System.out.println("User " + event.getUsername()+" Logged In!");
			//Main.getNIOS().send(event.getSocketChannel(), new bluefrost.serializable.objects.v1.LoggedInSuccessObject().toByteArray());
			//Above, make an Alert message? or something? or a new JsonObject.
		//	}
			
			
			
		}catch(Exception e){}
	}
}
