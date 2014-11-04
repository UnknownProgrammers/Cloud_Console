package bluefrost.serializable.objects.v1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class Apples extends EncryptableObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * Test Object 1
	 */
	public String a;
	public Apples(String s){
		a = s;
	}
	
	

	public static Apples fromByteArray(byte[] array){
		try{
			ByteArrayInputStream bis = new ByteArrayInputStream(array);
			ObjectInput in = null;
			in = new ObjectInputStream(bis);
			Object o = in.readObject(); 
			try{
				bis.close();
				in.close();
			}catch(Exception e){System.out.println("A Memory Leak Has Happened!");e.printStackTrace();}
			return (Apples)o;
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
