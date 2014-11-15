package com.bluefrost.nio.servernclient.useraccess;

import java.nio.channels.SocketChannel;
import java.security.Key;

import com.bluefrost.encryption.Crypto;
import com.bluefrost.sql.light.usermanagement.UserBase.PermissionsList;
import com.bluefrost.sql.light.usermanagement.UserBase.User;
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


	public static SocketChannel get(Client c){
		synchronized(map){
			return map.get(c);
		}
	}

	public static Client get(SocketChannel sc){
		synchronized(map){
			return map.inverse().get(sc);
		}
	}
	

	public  static void store(Client c, SocketChannel sc){
		synchronized(map){
			map.put(c, sc);
		}
	}

	public static void remove(Object o){
		if(o instanceof Client){
			synchronized(map){
				map.remove((Client)o);
			}
		}
		if(o instanceof SocketChannel){
			synchronized(map){
				map.inverse().remove((SocketChannel)o);
			}
		}
		
	}


	public static class Client{

		private PermissionsList list;
		public PermissionsList getPermissions(){return list;}
		
		private boolean loggedin = false;
		public boolean isLoggedIn(){return loggedin;}

		private String displayname = "null";
		public String getDisplayName(){return displayname;}
		private String username = "notLoggedIn";
		public String getUsername(){return username;}
		private String password = "notLoggedIn";
		public String getPassword(){return password;}

		private Key key = null;
		public void setKey(Key k){key = k;}
		public Key getKey() {
			if(key == null) return Crypto.getPriKey();
			return key;
		}

		public void set(User u){
			this.list = u.list;
			this.username = u.username;
			this.password = u.password;
			loggedin = true;
			this.displayname = u.displayname;
		}

	}
}
