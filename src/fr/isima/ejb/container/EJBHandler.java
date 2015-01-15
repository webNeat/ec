package fr.isima.ejb.container;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class EJBHandler implements InvocationHandler {
	private Object originalBean;

	public EJBHandler(Object bean){
		originalBean = bean;
	}

	@Override
	public Object invoke(Object bean, Method method, Object[] args) throws Throwable {
		return method.invoke(bean, args);
	}

}
