package bluefrost.serializable.objects.v1;

import java.io.ByteArrayInputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

public class Utils {

	
	public static Object fromByteArray(byte[] array){
		try{
			ByteArrayInputStream bis = new ByteArrayInputStream(array);
			ObjectInput in = null;
			in = new ObjectInputStream(bis);
			Object o = in.readObject(); 
			try{
				bis.close();
				in.close();
			}catch(Exception e){System.out.println("A Memory Leak Has Happened!");e.printStackTrace();}
			return (Object)o;
		}catch(Exception e){}
		return null;
	}
}
