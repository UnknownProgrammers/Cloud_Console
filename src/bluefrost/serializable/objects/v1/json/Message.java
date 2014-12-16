package bluefrost.serializable.objects.v1.json;

import com.bluefrost.nio.servernclient.listeners.GSONListener;



public class Message{

	public String name = this.getClass().getName();
	
	public String message;
	
	public Message(String s){
		message = s;
	}
	
	public static String getMessage(String s){
		return GSONListener.g.toJson(new Message(s));
	}
}
