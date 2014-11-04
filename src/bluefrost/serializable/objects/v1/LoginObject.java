package bluefrost.serializable.objects.v1;

public class LoginObject extends EncryptableObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String username;
	public String getUsername(){return username;}
	private String password;
	public String getPassword(){return password;}
	
	public LoginObject(String u, String p){
		username = u;
		password = p;
	}
}
