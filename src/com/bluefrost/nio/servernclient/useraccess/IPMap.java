package com.bluefrost.nio.servernclient.useraccess;

import java.net.SocketAddress;
import java.util.HashMap;

import com.bluefrost.nio.servernclient.settings.GlobalSettings;

public class IPMap {

	private static int max = (int)GlobalSettings.getSettings().pull("[IPMap] Max Connextions");

	public static HashMap<SocketAddress, Integer> map = new HashMap<SocketAddress, Integer>();

	public static boolean put(SocketAddress sa){
		synchronized(map){
			if(map.containsKey(sa)){
				int i = map.get(sa);
				if(i >= max){
					return false;
				}else{
					map.put(sa, i+1);
					return true;
				}
			}else{
				map.put(sa, 1);
				return true;
			}
		}
	}
	
	public static void remove(SocketAddress sa){
		synchronized(map){
			if(map.containsKey(sa)){
				int i = map.get(sa);
				System.out.println(i);
				if(i > 1){
					map.put(sa, i-1);
				}else if(i == 1){
					map.remove(sa);
				}
			}
		}
	}
	
}