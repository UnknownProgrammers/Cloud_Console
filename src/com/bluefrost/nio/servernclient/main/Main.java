package com.bluefrost.nio.servernclient.main;

import java.io.IOException;

import com.bluefrost.nio.servernclient.events.EventSystemWrapper;
import com.bluefrost.nio.servernclient.listeners.DisconnectListener;
import com.bluefrost.nio.servernclient.server.NIOS;

/*
 * Created by:
 * Blue/CyanFrost 
 * Ellie/Eleanor <3
 * 
 * Date: some time in October.
 */

public class Main {

	private static EventSystemWrapper esw;
	public static EventSystemWrapper getEventSystem(){return esw;}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		NIOS.Worker worker = new NIOS.Worker();
		new Thread(worker).start();
		new Thread(new NIOS(null, 9090, worker)).start();
		
	}
	
	
	
	public void registerEvents(){
		esw.addListener(new DisconnectListener());
	}

}
