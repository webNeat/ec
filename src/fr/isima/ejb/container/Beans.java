package fr.isima.ejb.container;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Beans {
	private static Beans instance = null;
	public static Beans getInstance() {
		if(instance == null)
			instance = new Beans();
		return instance;
	}
	
	private Map<Class<?>, List<Object>> classToBeans;
	private Beans(){
		classToBeans = new HashMap<Class<?>, List<Object>>();
	}
	public void addBeanToClass(Object o, Class<?> clientClass){
		if(! classToBeans.containsKey(clientClass))
			classToBeans.put(clientClass, new ArrayList<Object>());
		classToBeans.get(clientClass).add(o);
	}
	public void removeBeanFromClass(Object o, Class<?> clientClass){
		if(classToBeans.containsKey(clientClass)){
			classToBeans.get(clientClass).remove(o);
		}
	}
	public Object getBeanOfClass(Class<?> clientClass){
		if(classToBeans.containsKey(clientClass) && ! classToBeans.get(clientClass).isEmpty()){
			List<Object> list = classToBeans.get(clientClass);
			Object o = list.get(list.size() - 1);
			list.remove(o);
			return o;
		}
		return null;
	}
	public Object make(Class<?> clientClass){
		Object bean = getBeanOfClass(clientClass);
		if(bean == null){
			try {
				bean = Proxy.newProxyInstance(clientClass.getClassLoader(), clientClass.getInterfaces(), new EJBHandler(clientClass.newInstance()));
			} catch (IllegalArgumentException | InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bean;
	}
}
