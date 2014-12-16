package com.bluefrost.nio.servernclient.settings;

public class GlobalSettings {

	private static Settings s = new DefaultSettings();
	public static void setSettings(Settings s){GlobalSettings.s = s;}
	public static Settings getSettings(){return s;}
	
	public static Object pull(String s){
		return GlobalSettings.s.pull(s);
	}
	
	public static abstract class Settings {
		public abstract Object pull(String s);
	}
	
	
	private static class DefaultSettings extends Settings {

		@Override
		public Object pull(String s) {
			switch(s){
				case "[IPMap] Max Connextions":
					return 5;
				case "[Encryption] Level":
					return 3;
				
			}
			return false;
		}

		
	}
	
}
