package bluefrost.serializable.objects.v1;

public class LoginObject extends EncryptableObject{

	public String username;
	public String password;
	
	public LoginObject(String u, String p){
		username = u;
		password = p;
	}
}
