package bluefrost.serializable.objects.v1.json;

import java.util.UUID;

import bluefrost.serializable.objects.v1.json.PlayerList.PUAN;


public class JsonTester {

	public static void main(String args[]) throws Exception{
		String s = new PlayerList(new PUAN("Sam",UUID.randomUUID()), new PUAN("Rock",UUID.randomUUID())).toString();
		p(s);
		Object o = MyJsonUtils.fromString(s);
		for(PUAN pd1: ((PlayerList)o).plyrs){
			// = (PlayerData1)MyJsonUtils.fromString(b);
			p(pd1.us + "\t" + pd1.id);
		}
	}
	
	
	public static void p(Object o){
		System.out.println(o);
	}
}
