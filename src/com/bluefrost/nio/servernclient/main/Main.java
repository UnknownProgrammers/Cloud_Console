package com.bluefrost.nio.servernclient.main;

import com.bluefrost.nio.servernclient.events.EventSystemWrapper;
import com.bluefrost.nio.servernclient.listeners.ConnectionListener;
import com.bluefrost.nio.servernclient.listeners.DisconnectListener;
import com.bluefrost.nio.servernclient.listeners.MessageListener;
import com.bluefrost.nio.servernclient.server.NIOS;

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

	public static void main(String[] args){
		try{
			
			registerEvents();
			//*
			NIOS.Worker worker = new NIOS.Worker();
			new Thread(worker).start();
			NIOS nios = new NIOS(null, 9090, worker);
			new Thread(nios).start();
			//*/
			//Thread.sleep(100);
			//nios.end();

		}catch(Exception e){e.printStackTrace();}
	}





	public static void registerEvents(){
		esw.addListener(new MessageListener());
		esw.addListener(new DisconnectListener());
		esw.addListener(new ConnectionListener());
	}

}
