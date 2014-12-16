package com.bluefrost.nio.servernclient.listeners.JsonListeners;

import bluefrost.serializable.objects.v1.json.Message;

import com.bluefrost.nio.servernclient.events.EventSystemWrapper.EventSystem.EventHandler;
import com.bluefrost.nio.servernclient.events.EventSystemWrapper.EventSystem.Listener;

public class MessageEvent implements Listener {

	@EventHandler
	public void v1_(Message Event){
			
	}
}
