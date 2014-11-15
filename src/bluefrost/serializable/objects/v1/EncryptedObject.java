package bluefrost.serializable.objects.v1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;

public class EncryptedObject implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EncryptedObject(byte[] object){encrypted = object;}
	private byte[] encrypted;
	
	public EncryptableObject decrypt(Key key) throws Exception{
		String Algrithem;
		if(key instanceof PrivateKey){Algrithem = "RSA";}
		else{Algrithem = "AES";}
		if(encrypted == null) {System.out.println("NULL");return null;}
		Cipher decrypt =  Cipher.getInstance(Algrithem);  
		decrypt.init(Cipher.DECRYPT_MODE, key);  
		ByteArrayInputStream bais = new ByteArrayInputStream(encrypted);
		CipherInputStream cin=new CipherInputStream(bais, decrypt);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[2048];
		int read=0;
		while((read=cin.read(buf))!=-1) {//reading encrypted data
			out.write(buf,0,read);  //writing decrypted data
			out.flush();
		}
		out.close();
		cin.close();
		bais.close();
		return EncryptableObject.fromByteArray(out.toByteArray());
	}
	
	public EncryptedObject(EncryptableObject so, Key key)throws Exception{ encrypted = encrypt(so, key);}
		
	private byte[] encrypt(EncryptableObject o, Key key) throws Exception{
		String Algrithem;
		if(key instanceof PublicKey){Algrithem = "RSA";}
		else{Algrithem = "AES";}
		ByteArrayInputStream i = new ByteArrayInputStream(o.toByteArray());
		Cipher cipher = Cipher.getInstance(Algrithem);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		CipherOutputStream ciph = new CipherOutputStream(out, cipher);
		byte[] buf = new byte[2048];
		int read;
		while((read=i.read(buf))!=-1){//reading data
			ciph.write(buf,0,read);  //writing encrypted data
			ciph.flush();
		}
		out.close();
		ciph.close();
		i.close();
		return out.toByteArray();
	}
	
	public byte[] toByteArray(){
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(bos);   
			out.writeObject(this);
			return bos.toByteArray();
		}catch(Exception e){e.printStackTrace(); return null;} 
		finally {
			try {if (out != null) {out.close();}} catch (IOException ex) {}
			try {bos.close();} catch (IOException ex) {}
		}
	}
}
