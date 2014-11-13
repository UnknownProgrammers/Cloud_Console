package com.bluefrost.nio.servernclient.main;

import java.io.File;

import com.bluefrost.encryption.Crypto;
import com.bluefrost.nio.servernclient.events.EventSystemWrapper;
import com.bluefrost.nio.servernclient.listeners.ConnectionListener;
import com.bluefrost.nio.servernclient.listeners.DisconnectListener;
import com.bluefrost.nio.servernclient.listeners.LoginListener;
import com.bluefrost.nio.servernclient.listeners.MessageListener;
import com.bluefrost.nio.servernclient.server.NIOS;
import com.bluefrost.sql.light.usermanagement.UserBase;

/*
 * Created by:
 * Blue/CyanFrost 
 * Ellie/Eleanor <3
 * 
 * Date: some time in October.
 */

public class Main {

	private static EventSystemWrapper esw = new EventSystemWrapper();
	public static EventSystemWrapper getEventSystem(){return esw;}
	
	private static UserBase ub1;
	public static UserBase getDefaultUserBase(){return ub1;}
	
	
	private static NIOS nios;
	public static NIOS getNIOS(){return nios;}
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args){
		try{
			System.out.println("Setting up UserManager DataBase...");
			
			ub1 = UserBase.setup(new File("C:\\JavaResources\\Simple.db"));

			System.out.println("UserManager DataBase is setup!");
			ub1.createUser("root", "toor");
			System.out.println("Please Standby, Generating RSA Keyset...");
			Crypto.genKeys();
			System.out.println("RSA Keyset Generated!");
			registerEvents();
			System.out.println("Registered Events!");
			//*
			NIOS.Worker worker = new NIOS.Worker();
			new Thread(worker).start();
			 nios = new NIOS(null, 9090, worker);
			 
			new Thread(nios).start();
			
			
			
			//*/
			Thread.sleep(1000);
			
			
		//	nios.end();
		 
		}catch(Exception e){e.printStackTrace();}
	}





	public static void registerEvents(){
		esw.addListener(new LoginListener());
		esw.addListener(new MessageListener());
		esw.addListener(new DisconnectListener());
		esw.addListener(new ConnectionListener());
	}

}
