package bluefrost.serializable.objects.v1;

public class PM extends EncryptableObject {

	private static final long serialVersionUID = 1L;
	
	private String to;
	public String getTo(){return to;}
	
	private String message;
	public String getMessage(){return message;}
	
	public PM(String t, String m){
		this.to = t;
		this.message = m;
	}

}
