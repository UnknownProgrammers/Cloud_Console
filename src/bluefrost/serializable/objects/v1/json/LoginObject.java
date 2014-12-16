package bluefrost.serializable.objects.v1.json;

import bluefrost.serializable.objects.v1.json.MyJsonUtils.JsonObject;

import com.bluefrost.nio.servernclient.listeners.GSONListener;

public class LoginObject extends JsonObject{

	public String name = this.getClass().getName();
	
	private String username;
	public String getUsername(){return username;}
	
	private String password;
	public String getPassword(){return password;}

	public LoginObject(String u, String p) {
		username = u;
		password = p;
	}

	public static String getLoginObject(String u, String p){
		return GSONListener.g.toJson(new LoginObject(u,p));
	}

	

}
