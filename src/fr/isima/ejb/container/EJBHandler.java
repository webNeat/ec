package fr.isima.ejb.container;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class EJBHandler implements InvocationHandler {

	public EJBHandler(Object newInstance) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object invoke(Object bean, Method method, Object[] args) throws Throwable {
		return method.invoke(bean, args);
	}

}
