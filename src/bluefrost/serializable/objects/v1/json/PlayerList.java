package bluefrost.serializable.objects.v1.json;

import java.util.UUID;

import bluefrost.serializable.objects.v1.json.MyJsonUtils.JsonObject;

public class PlayerList extends JsonObject {
	
	PUAN[] plyrs; //All Players. Username and ID. Client renders Avatar. To track, the Client must schedule a runnable task. 
	
	public PlayerList(PUAN... jsonData){
		plyrs = jsonData;
	}
	
	public static class PUAN {
		String us;
		UUID id;
		
		public PUAN(String username, UUID id){
			this.us = username;
			this.id = id;
		}
	}
}
