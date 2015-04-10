package bluefrost.serializable.objects.v1.json;

import bluefrost.serializable.objects.v1.json.MyJsonUtils.JsonObject;

public class ChatMessageJ extends JsonObject {

	String t = "";
	
	public ChatMessageJ(String s){
		t = s;
	}
}
