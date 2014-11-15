package bluefrost.serializable.objects.v1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.Key;

import com.bluefrost.encryption.Crypto;

public class AESKeyObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public byte[] key;
	private boolean encrypted;
	
	
	public Key getKey(Key k){
		if(encrypted){
			decrypt(k);
		}
		return (Key)Utils.fromByteArray(key);
	}

	public void encrypt(Key k){
		key = Crypto.encryptData(key,k);
		encrypted = true;
	}
	
	public void decrypt(Key k){
		key = Crypto.decryptData(key, k);
		encrypted = false;
	}
	
	public AESKeyObject(Key store, Key encrypt){
		key = Utils.toByteArray(store);
		encrypt(encrypt);
	}
	

	public static AESKeyObject fromByteArray(byte[] array){
		try{
			ByteArrayInputStream bis = new ByteArrayInputStream(array);
			ObjectInput in = null;
			in = new ObjectInputStream(bis);
			Object o = in.readObject(); 
			try{
				bis.close();
				in.close();
			}catch(Exception e){System.out.println("A Memory Leak Has Happened!");e.printStackTrace();}
			return (AESKeyObject)o;
		}catch(Exception e){}
		return null;
	}

	public byte[] toByteArray(){
		try{
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutput out = null;
			out = new ObjectOutputStream(bos);   
			out.writeObject(this);
			byte[] yourBytes = bos.toByteArray();
			try{
				bos.close();
				out.close();
			}catch(Exception e){System.out.println("A Memory Leak Has Happened!");e.printStackTrace();}
			return yourBytes;
		}catch(Exception e){
			return null;
		}
	}
	
}
