package bluefrost.serializable.objects.v1;

import java.security.Key;

public class KeyObject extends EncryptableObject{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Key k;
	
	public KeyObject(Key k){
		this.k = k;
	}
	
}
