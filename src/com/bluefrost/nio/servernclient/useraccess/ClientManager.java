package com.bluefrost.nio.servernclient.useraccess;

import java.nio.channels.SocketChannel;
import java.security.Key;
import java.util.concurrent.ConcurrentHashMap;

import bluefrost.serializable.objects.v1.EncryptableObject;

import com.bluefrost.encryption.Crypto;
import com.bluefrost.nio.servernclient.main.Main;
import com.bluefrost.sql.light.usermanagement.UserBase.PermissionsList;
import com.bluefrost.sql.light.usermanagement.UserBase.PermissionsList.Permission;
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

	public static void say(Permission perm, byte[] data){
		if(perm == null)return;
		synchronized(map){
			for(Client c: map.keySet()){
				if(c.getPermissions()!= null)//ifLoggedIn
					if(c.getPermissions().hasPermission(perm)){
						Main.getNIOS().send(get(c),data);
					}
			}
		}
	}

	public static void say(byte[] data){
		say(Permission.magical, data);
	}


	public static void say(Permission perm, EncryptableObject eo){
		if(perm == null)return;
		synchronized(map){
			for(Client c: map.keySet()){
				try{
					if(c.getPermissions()!= null);
					if(c.getPermissions().hasPermission(perm)){
						if(c.getKey() != Crypto.getPriKey()){
							Main.getNIOS().send(get(c),eo.encrypt(c.getKey()).toByteArray());
						}else{
							Main.getNIOS().send(get(c), "Please Enable Encryption. Server Demands It.".getBytes());
						}
					}
				}catch(Exception e){}
			}
		}
	}

	public static void say(EncryptableObject eo){
		say(Permission.magical, eo);
	}


	public static class Client{

		private ConcurrentHashMap<String, Object> m = new ConcurrentHashMap<String, Object>();
		public ConcurrentHashMap<String, Object> getMetaData(){return m;}
		
		private PermissionsList list; //leave Null for people not logged in!
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
		public void setKey(Key k){
			synchronized(key){
				key = k;
			}
		}
		public Key getKey() {
			synchronized(key){
				if(key == null) return Crypto.getPriKey();
				return key;
			}
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
