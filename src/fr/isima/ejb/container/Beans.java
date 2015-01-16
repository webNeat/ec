package fr.isima.ejb.container;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
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
	//Stateless
	private Map<Class<?>, List<Object>> classToBeans;
	//Singleton
	private Map<Class<?>, Object> classToBeanSingleton;
	private Beans(){
		classToBeans = new HashMap<Class<?>, List<Object>>();
		classToBeanSingleton = new HashMap<Class<?>, Object>();
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
	private Class<?>[] getProxyInterfacesOf(Class<?> beanClass){
		Class<?>[] ints = beanClass.getInterfaces();
		Class<?>[] res = new Class<?>[ints.length + 1];
		for(int i = 0; i < ints.length; i++)
			res[i] = ints[i];
		res[ints.length] = ProxyInterface.class;
		return res;
	}
	public Object make(Class<?> clientClass){
		Object bean = getBeanOfClass(clientClass);
		if(bean == null){
			try {
				bean = Proxy.newProxyInstance(clientClass.getClassLoader(), getProxyInterfacesOf(clientClass), new EJBHandler(clientClass.newInstance()));
			} catch (IllegalArgumentException | InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return bean;
	}
	public Object makeSingleton(Class<?> classe){
		Object proxy = getBeanSingletonOfClass(classe);
		if(proxy == null){
			try {
				proxy = Proxy.newProxyInstance(classe.getClassLoader(), getProxyInterfacesOf(classe), new EJBHandler(classe.newInstance()));
				classToBeanSingleton.put(classe, proxy);
			} catch (IllegalArgumentException | InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return proxy;
	}
	public Object getBeanSingletonOfClass(Class<?> classe){	
		if(classToBeanSingleton.containsKey(classe)){
			Object proxy = classToBeanSingleton.get(classe);
			return proxy;
		}
		return null;
	}
}
