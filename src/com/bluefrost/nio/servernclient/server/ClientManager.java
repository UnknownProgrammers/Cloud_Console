package com.bluefrost.nio.servernclient.server;

import java.nio.channels.SocketChannel;

import com.google.common.collect.HashBiMap;

/*
 * Created by:
 * Blue/CyanFrost 
 * Ellie/Eleanor <3
 * 
 * Date: some time in October.
 */

public class ClientManager {

	public static HashBiMap<Client, SocketChannel> map = HashBiMap.create();

	public synchronized static Object get(Object o){
		if(o instanceof Client){
			return map.get((Client)o);
		}
		if(o instanceof SocketChannel){
			return map.inverse().get((SocketChannel)o);
		}
		return null;
	}
		
	public synchronized static void store(Client c, SocketChannel sc){
		map.put(c, sc);
	}

	public synchronized static void remove(Object o){
		if(o instanceof Client){
			map.remove((Client)o);
		}
		if(o instanceof SocketChannel){
			map.inverse().remove((SocketChannel)o);
		}
	}
	
	
	public static class Client{

		public String username = "notLoggedIn";
		public String password = "notLoggedIn";
		
	}
}
