package bluefrost.serializable.objects.v1;

import java.security.Key;

public class LoginObject extends EncryptableObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String username;
	public String getUsername(){return username;}
	private String password;
	public String getPassword(){return password;}
	
	private Key k;
	public Key getKey(){return k;}
	
	public LoginObject(String u, String p, Key k){
		username = u;
		password = p;
		this.k = k;
	}
}
