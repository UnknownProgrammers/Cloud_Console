package com.bluefrost.nio.servernclient.server;

import java.nio.channels.SocketChannel;

import com.google.common.collect.BiMap;

/*
 * Created by:
 * Blue/CyanFrost 
 * Ellie/Eleanor <3
 * 
 * Date: some time in October.
 */

public class ClientManager {



	BiMap<Client, SocketChannel> map;

	
	public void b(){
		
	}













	public static class Client{


		private SocketChannel sc;
		public SocketChannel getSocketChannel(){return sc;}
		
		public String username;
		public String password;
		
	}
}
