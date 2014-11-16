package com.bluefrost.nio.servernclient.settings;

public class GlobalSettings {

	private static Settings s = new DefaultSettings();
	public static void setSettings(Settings s){GlobalSettings.s = s;}
	public static Settings getSettings(){return s;}
	
	
	public static abstract class Settings {
		
		public abstract int maxIPConnections();
		
	}
	
	
	private static class DefaultSettings extends Settings{

		@Override public int maxIPConnections() {return 5;}
		
	}
	
}
