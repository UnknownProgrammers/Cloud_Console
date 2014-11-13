package com.bluefrost.nio.servernclient.listeners;

import java.nio.channels.SocketChannel;

import bluefrost.serializable.objects.v1.Apples;
import bluefrost.serializable.objects.v1.EncryptableObject;
import bluefrost.serializable.objects.v1.EncryptedObject;
import bluefrost.serializable.objects.v1.LoginObject;
import bluefrost.serializable.objects.v1.Utils;

import com.bluefrost.encryption.Crypto;
import com.bluefrost.nio.servernclient.events.EventSystemWrapper.EventSystem.EventHandler;
import com.bluefrost.nio.servernclient.events.EventSystemWrapper.EventSystem.Listener;
import com.bluefrost.nio.servernclient.main.Main;
import com.bluefrost.nio.servernclient.useraccess.ClientManager;
import com.bluefrost.nio.servernclient.useraccess.ClientManager.Client;

public class MessageListener implements Listener{


	@EventHandler
	public void saySomething(MessageEvent event){
		try{
			synchronized(ClientManager.map.inverse().get(event.getSocketChannel())){
				Client c = ClientManager.map.inverse().get(event.getSocketChannel());
				Object o = Utils.fromByteArray(event.getBytes());
				if(o instanceof EncryptedObject){
					System.out.println("Recieved an Encrypted Object, Decrypting Now!");
					EncryptableObject eo = ((EncryptedObject)o).decrypt(c.getKey());
					if(eo == null){
						System.out.println("NULLLL");
						eo = ((EncryptedObject)o).decrypt(Crypto.getPriKey());
						if(eo == null){
							System.out.println("DoubleNull");
						}
					}
					eo.setSocketChannel(event.getSocketChannel());
					System.out.println("Object instanceof " +eo.getClass().getName());
					if(!c.loggedin && !(eo instanceof LoginObject))return;
					Main.getEventSystem().listen(eo);
				}else if(o instanceof EncryptableObject){
					EncryptableObject eo = (EncryptableObject)o;
					if(!c.loggedin && !(eo instanceof LoginObject))return;
					eo.setSocketChannel(event.getSocketChannel());
					Main.getEventSystem().listen(eo);
				}
			}
		}catch(Exception e){e.printStackTrace();}
	}

	@EventHandler
	public void onAppleEvent(Apples event){
		try{
			synchronized(ClientManager.map){
				Client c = ClientManager.map.inverse().get(event.getSocketChannel());
				System.out.println(c.username + " gave us an apple: " + event.a);

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
