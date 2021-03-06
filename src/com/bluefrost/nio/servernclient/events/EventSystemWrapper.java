package com.bluefrost.nio.servernclient.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 * Created by:
 * (Blue/Cyan)Frost 
 * Ellie/Eleanor <3
 * 
 * Date: some time in October.
 */

public class EventSystemWrapper {
	
	private EventSystem es = new EventSystem();
	@Deprecated public EventSystem getEventSystem(){return es;} //WARNING: Not Thread Safe
	
	public void addListener(EventSystem.Listener l){
		synchronized(es){
			es.register(l);
		}
	}
	
	public void removeListener(EventSystem.Listener l){
		synchronized(es){
			es.removeListener(l);
		}
	}
	
	public void listen(Object o){
		synchronized(es){
			es.listen(o);
		}
	}
	
	
	
	
	public static class EventSystem {


		private HashMap<Class<?>, List<Listener>> map = new HashMap<Class<?>, List<Listener>>();
		public HashMap<Class<?>, List<Listener>> getMap(){return map;}



		public void register(Listener l){
			for(Method m: l.getClass().getDeclaredMethods()){
				if(true){
					if(m.isAnnotationPresent(EventHandler.class)){
						
						if(m.getParameterTypes().length==1){
							if(map.containsKey(m.getParameterTypes()[0])){
								//if map has this object listed 
								if(map.get(m.getParameterTypes()[0])!= null){
									addToList(m.getParameterTypes()[0],l);
								}else{
									createList(m.getParameterTypes()[0]);
									addToList(m.getParameterTypes()[0],l);
								}
							}else{
								//if map does not have this object listed
								createList(m.getParameterTypes()[0]);
								addToList(m.getParameterTypes()[0],l);
							}
						}
					}
				}
			}
		}
		private void createList(Class<?> c){
			if(map.containsKey(c)){
				if(map.get(c)== null){
					map.put(c, new ArrayList<Listener>());
				}
			}else{
				map.put(c, new ArrayList<Listener>());
			}
		}
		private void addToList(Class<?> c, Listener l){
			if(map.containsKey(c)){
				if(map.get(c)!= null){
					if(!map.get(c).contains(l)){
						map.get(c).add(l);
					}
				}
			}
		}

		public void listen(Object o){
			if(map.containsKey(o.getClass())){
				if(map.get(o.getClass())== null) throw new RuntimeException("No Listeners Assigned For " + o.getClass());
				for(Listener l: map.get(o.getClass())){
					for(Method m: getMListeners(o.getClass(),l.getClass())){
						try{
							m.invoke(l,o);
						}catch(Exception e){e.printStackTrace();}
					}
				}
			}else{
				throw new RuntimeException("No Listeners Assigned For " + o.getClass());
			}
		}


		public List<Method> getMListeners(Class<?> l1, Class<?> l2){
			//L1 == object, L1 == Listener
			List<Method> methods = new ArrayList<Method>();
			for(Method m: l2.getDeclaredMethods()){
				if(m.isAnnotationPresent(EventHandler.class)){
					if(m.getParameterTypes().length == 1){
						if(m.getParameterTypes()[0].equals(l1)){
							methods.add(m);
						}
					}

				}
			}
			return sort(methods);
		}
		
		
		public List<Method> sort(List<Method> m){
			return m;
		}

		public void removeListener(Listener l){
			for(List<Listener> list: map.values()){
				if(list.contains(l)){
					list.remove(l);
				}
			}
		}
		

		public static interface Listener{}

		@Retention(RetentionPolicy.RUNTIME)@Target(ElementType.METHOD)public static @interface EventHandler {Priority EventPriority() default Priority.Medium;}
		
		public static enum Priority{
			Low(0),
			Medium(1),
			High(2);
			
			public int level;
			
			Priority(int i){
				this.level = i;
			}
		}
	}
	
}
