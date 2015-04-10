package bluefrost.serializable.objects.v1.json;

import java.nio.channels.SocketChannel;
import java.security.Key;

import com.bluefrost.nio.servernclient.listeners.GSONListener;
import com.bluefrost.nio.servernclient.listeners.GSONListener.GSONObjectFinder;

public class MyJsonUtils {

	public static class JsonObject {

		public String name = this.getClass().getSimpleName();
			
		public String toString(){
			return GSONListener.g.toJson(this);
		}
		
		private transient SocketChannel sc;
		
		public SocketChannel getSocketChannel(){return sc;}
		
		public void setSocketChannel(SocketChannel sc2) {
			sc = sc2;
		}
		
		public JsonShell seal(Key k){
			return new JsonShell(this).encrypt(k);
		}
	}
	
	public static Object fromString(String s) throws Exception{
		return GSONListener.g.fromJson(s, MyJsonUtils.findClass(GSONListener.g.fromJson(s, GSONObjectFinder.class).name));
	}
	
	public static Class<?> findClass(String name) throws ClassNotFoundException{
		return Class.forName(MyJsonUtils.class.getPackage().toString().replaceFirst("package ", "")+"."+name);
	}
}
