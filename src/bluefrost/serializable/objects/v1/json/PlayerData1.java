package bluefrost.serializable.objects.v1.json;

import java.util.UUID;

import bluefrost.serializable.objects.v1.json.MyJsonUtils.JsonObject;

public class PlayerData1 extends JsonObject{

	//Data, such as Location and ip?
	
	UUID u;
	
	String us;
	
	public PlayerData1 (UUID u, String us) {
		this.u = u;
		this.us = us;
	}
	
	
}
