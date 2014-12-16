package com.bluefrost.nio.servernclient.listeners;

import java.nio.channels.SocketChannel;

import bluefrost.serializable.objects.v1.json.JsonShell;
import bluefrost.serializable.objects.v1.json.LoginObject;
import bluefrost.serializable.objects.v1.json.MyJsonUtils;
import bluefrost.serializable.objects.v1.json.MyJsonUtils.JsonObject;

import com.bluefrost.nio.servernclient.events.EventSystemWrapper.EventSystem.EventHandler;
import com.bluefrost.nio.servernclient.events.EventSystemWrapper.EventSystem.Listener;
import com.bluefrost.nio.servernclient.main.Main;
import com.bluefrost.nio.servernclient.useraccess.ClientManager;
import com.bluefrost.nio.servernclient.useraccess.ClientManager.Client;
import com.google.gson.Gson;

public class GSONListener implements Listener{

	public static Gson g = new Gson();
	private static GSONObjectFinder gof = new GSONObjectFinder();
	private static Class<?> c;
	private static Object o;
	
	//This is used because Strings can't store SocketChannels, and because I don't want to create a new instance of an object just to 
	// store it.
	public static void listen(String s, SocketChannel sc){
		try{
			gof = g.fromJson(s, GSONObjectFinder.class);
			c = MyJsonUtils.findClass(gof.name);
			o = g.fromJson(s, c);
			if(o instanceof JsonObject){((JsonObject)o).setSocketChannel(sc);}
			Main.getEventSystem().listen(o);
		}catch(Exception e){}
	}

	public static class GSONObjectFinder{public String name;}
	
	@EventHandler
	public void listen(JsonShell js) throws Exception{
		Client c = ClientManager.get(js.getSocketChannel());
		if(js.isEncrypted()){js.decrypt(c.getKey());}
		Object o = MyJsonUtils.fromString(js.getObjectStored());
		if(c.isLoggedIn()){
			Main.getEventSystem().listen(o);
		}else{
			if(o instanceof LoginObject){
				Main.getEventSystem().listen(o);
			}
		}
	}
	

}
