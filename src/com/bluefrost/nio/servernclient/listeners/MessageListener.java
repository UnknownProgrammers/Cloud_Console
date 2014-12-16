package com.bluefrost.nio.servernclient.listeners;

import java.nio.channels.SocketChannel;

import bluefrost.serializable.objects.v1.Apples;
import bluefrost.serializable.objects.v1.EncryptableObject;
import bluefrost.serializable.objects.v1.EncryptedObject;
import bluefrost.serializable.objects.v1.LoginObject;
import bluefrost.serializable.objects.v1.Utils;

import com.bluefrost.nio.servernclient.events.EventSystemWrapper.EventSystem.EventHandler;
import com.bluefrost.nio.servernclient.events.EventSystemWrapper.EventSystem.Listener;
import com.bluefrost.nio.servernclient.main.Main;
import com.bluefrost.nio.servernclient.useraccess.ClientManager;
import com.bluefrost.nio.servernclient.useraccess.ClientManager.Client;

public class MessageListener implements Listener{


	@EventHandler
	public void onMessageEvent(MessageEvent event){
		try{
			Object o = Utils.fromByteArray(event.b);	
			if(o instanceof String){//assume its json    //****************************
				GSONListener.listen((String)o, event.sc);//	Gson/Json              	  *
				return;									 //	Listener                  *
			}											 //////////////////////////////	
			Client c = ClientManager.get(event.sc);
			if(o instanceof EncryptedObject){o = ((EncryptedObject)o).decrypt(c.getKey());}
			System.out.println("Object is: " + o.getClass().getSimpleName());
			if(o instanceof EncryptableObject){((EncryptableObject)o).setSocketChannel(event.getSocketChannel());}
			if(c.isLoggedIn()){
				Main.getEventSystem().listen(o);
			}else{
				if(o instanceof LoginObject){
					Main.getEventSystem().listen(o);
				}
			}
		}catch(Exception e){e.printStackTrace();}
	}
	
	

	@EventHandler
	public void onAppleEvent(Apples event){
		try{
			synchronized(ClientManager.map){
				Client c = ClientManager.map.inverse().get(event.getSocketChannel());
				System.out.println(c.getUsername()+ " gave us an apple: " + event.a);

			}
		}catch(Exception e){}
	}




	public static class MessageEvent {

		private byte[]  b = null;
		public byte[] getBytes(){return b;}

		private SocketChannel sc = null;
		public SocketChannel getSocketChannel(){return sc;}

		@Deprecated
		public MessageEvent(byte[] c){
			b = c;
		}

		public MessageEvent(byte[] c, SocketChannel sc){
			this.sc = sc;
			b = c;
		}

	}

}
