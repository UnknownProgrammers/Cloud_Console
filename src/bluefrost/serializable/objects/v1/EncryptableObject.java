package bluefrost.serializable.objects.v1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.channels.SocketChannel;
import java.security.Key;

public class EncryptableObject implements Serializable{
	
	
	private transient SocketChannel sc = null;
	public void setSocketChannel(SocketChannel s){ sc = s;}
	public SocketChannel getSocketChannel(){return sc;}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EncryptedObject encrypt(Key key){
	    try{
			return new EncryptedObject(this, key);
		}catch(Exception e){ e.printStackTrace(); return null;}
	}

	public static EncryptableObject fromByteArray(byte[] array){
		try{
			ByteArrayInputStream bis = new ByteArrayInputStream(array);
			ObjectInput in = null;
			in = new ObjectInputStream(bis);
			Object o = in.readObject(); 
			try{
				bis.close();
				in.close();
			}catch(Exception e){System.out.println("A Memory Leak Has Happened!");e.printStackTrace();}
			return (EncryptableObject)o;
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
