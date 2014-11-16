
package bluefrost.serializable.objects.v1;


public class CM extends EncryptableObject {

	private static final long serialVersionUID = 1L;

	private String message;
	public String getMessage(){
		return message;
	}
	
	public CM(String s){
		message = s;
	}
}
