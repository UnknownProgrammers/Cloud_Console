package bluefrost.serializable.objects.v1.json;

import java.security.Key;

import bluefrost.serializable.objects.v1.json.MyJsonUtils.JsonObject;

import com.bluefrost.encryption.Crypto;

public class JsonShell extends JsonObject{

	/*
	 * 	Different Type of Serialization  *
	 */
	
	private byte[] b;
	public String getObjectStored(){
		return new String(b);
	}
	
	private boolean encrypted = false;
	public boolean isEncrypted(){
		return encrypted;
	}
	
	public JsonShell encrypt(Key k){
		b = Crypto.encryptData(b, k);
		encrypted = true;
		return this;
	}
	
	public JsonShell decrypt(Key k){
		b = Crypto.decryptData(b,k);
		encrypted = false;
		return this;
	}
	
	public JsonShell(byte[] b){
		this.b = b;
	}
	
	public JsonShell(String s){
		this.b = s.getBytes();
	}
	
	public JsonShell(JsonObject jo){
		this.b = jo.toString().getBytes();
	}
}
