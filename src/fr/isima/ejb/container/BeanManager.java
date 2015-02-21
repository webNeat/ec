package fr.isima.ejb.container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.isima.ejb.container.exceptions.LocalAllInterfacesUnfoundException;
import fr.isima.ejb.container.exceptions.NoLocalInterfaceIsImplemented;
import fr.isima.ejb.container.exceptions.PostConstructInvokationException;
import fr.isima.ejb.container.exceptions.PreDestroyInvokationException;
import fr.isima.ejb.container.exceptions.SingletonBeanMakingExecption;
import fr.isima.ejb.container.exceptions.StatelessBeanMakingExecption;

public class BeanManager {
	private static BeanManager instance = null;
	public static BeanManager getInstance() throws LocalAllInterfacesUnfoundException, NoLocalInterfaceIsImplemented, SingletonBeanMakingExecption {
		if(instance == null)
			instance = new BeanManager();
		return instance;
	}
	private Map<Class<?>, List<Object>> statelessClassToBeans;
	private Map<Class<?>, Object> singletonClassToBean;
	private BeanManager() throws LocalAllInterfacesUnfoundException, NoLocalInterfaceIsImplemented, SingletonBeanMakingExecption{
		statelessClassToBeans = new HashMap<Class<?>, List<Object>>();
		singletonClassToBean = new HashMap<Class<?>, Object>();
	}
	public void addStatelessClass(Class<?> ejbClass) {
		statelessClassToBeans.put(ejbClass, new ArrayList<Object>());
	}
	public void addSingletonClass(Class<?> ejbClass) throws SingletonBeanMakingExecption{
		singletonClassToBean.put(ejbClass, null);
	}
	private Object makeStatelessBean(Class<?> ejbClass) throws StatelessBeanMakingExecption {
		Object bean = null;
		try {
			bean = ejbClass.newInstance();
			Container.getInstance().handleAnnotations(bean);
		} catch (Exception e) {
			throw new StatelessBeanMakingExecption(e);
		}
		return bean;
	}
	private Object makeSingletonBean(Class<?> ejbClass) throws SingletonBeanMakingExecption {
		Object bean = null;
		try {
			bean = ejbClass.newInstance();
			Container.getInstance().handleAnnotations(bean);
			Container.getInstance().invokePostConstructMethodsOf(bean);
		} catch (Exception e) {
			throw new SingletonBeanMakingExecption(e);
		}
		return bean;
	}
	public Object getBeanOfClass(Class<?> clientClass) throws StatelessBeanMakingExecption, PostConstructInvokationException, LocalAllInterfacesUnfoundException, NoLocalInterfaceIsImplemented, SingletonBeanMakingExecption{
		Object bean = null;
		if(singletonClassToBean.containsKey(clientClass)){
			if( null == singletonClassToBean.get(clientClass))
				singletonClassToBean.put(clientClass, makeSingletonBean(clientClass));
			bean = singletonClassToBean.get(clientClass);			
		} else if(statelessClassToBeans.containsKey(clientClass)){
			List<Object> list = statelessClassToBeans.get(clientClass);
			if(list.isEmpty()){
				bean = makeStatelessBean(clientClass);
			} else {
				bean = list.get(list.size() - 1);
				list.remove(list.size() - 1);
			}
			Container.getInstance().invokePostConstructMethodsOf(bean);
		}
		return bean;
	}
	public void takeBean(Object bean) throws PreDestroyInvokationException, LocalAllInterfacesUnfoundException, NoLocalInterfaceIsImplemented, SingletonBeanMakingExecption {
		Class<?> bClass = bean.getClass();
		if(statelessClassToBeans.containsKey(bClass)){
			Container.getInstance().invokePreDestroyMethodsOf(bean);
			statelessClassToBeans.get(bClass).add(bean);
		}
	}

//	public Object make(Class<?> clientClass){
//		Object bean = getBeanOfClass(clientClass);
//		if(bean == null){
//			try {
//				bean = clientClass.newInstance();
//				Container.getInstance().handleAnnotations(bean);
//				bean = Proxy.newProxyInstance(clientClass.getClassLoader(), getProxyInterfacesOf(clientClass), new EJBHandler(bean));
//			} catch (IllegalArgumentException | InstantiationException | IllegalAccessException | LocalAllInterfacesUnfoundException | NoLocalInterfaceIsImplemented e) {
//				throw new ProxyMakingException(e);
//			}
//		}
//		return bean;
//	}
//	public Object makeSingleton(Class<?> classe){
//		Object proxy = getBeanSingletonOfClass(classe);
//		if(proxy == null){
//			try {
//				Object bean = classe.newInstance();
//				Container.getInstance().handleAnnotations(bean);
//				proxy = Proxy.newProxyInstance(classe.getClassLoader(), getProxyInterfacesOf(classe), new EJBHandler(bean));
//				classToBeanSingleton.put(classe, proxy);
//			} catch (IllegalArgumentException | InstantiationException | IllegalAccessException | LocalAllInterfacesUnfoundException | NoLocalInterfaceIsImplemented e) {
//				throw new ProxyMakingException(e);
//			}
//		}
//		return proxy;
//	}
//	public Object getBeanSingletonOfClass(Class<?> classe){	
//		if(classToBeanSingleton.containsKey(classe)){
//			Object proxy = classToBeanSingleton.get(classe);
//			return proxy;
//		}
//		return null;
//	}
}