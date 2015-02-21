package fr.isima.ejb.container;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import fr.isima.ejb.container.annotations.TransactionAttribute;
import fr.isima.ejb.container.exceptions.LocalAllInterfacesUnfoundException;
import fr.isima.ejb.container.exceptions.NoLocalInterfaceIsImplemented;
import fr.isima.ejb.container.exceptions.SingletonBeanMakingExecption;
import fr.isima.ejb.container.logging.Logger;

public class EJBHandler implements InvocationHandler {
	private Class<?> beanClass;
	private Object bean;
	private TransactionAttribute.Type defaultTransactionType;
	private BeanManager beanManager;
	public EJBHandler(Class<?> beanClass) throws LocalAllInterfacesUnfoundException, NoLocalInterfaceIsImplemented, SingletonBeanMakingExecption{
		this.beanClass = beanClass;
		this.bean = null;
		beanManager = BeanManager.getInstance();
		TransactionAttribute ta = beanClass.getAnnotation(TransactionAttribute.class);
		if(ta == null)
			defaultTransactionType = TransactionAttribute.Type.NEVER;
		else
			defaultTransactionType = ta.value();
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result = null;
		bean = beanManager.getBeanOfClass(beanClass);
		if(method.getName().equals("getBean")){
			Logger.log("getBean called");
			result = bean;			
		} else if(method.getName().equals("equals")){
			Logger.log("equals called");
			result = ( bean == ((ProxyInterface)args[0]).getBean() );
		} else if(method.getName().equals("toString")){
			Logger.log("toString called");
			result = bean.toString();
		} else {
			Logger.log(method.getName() + " is called with params : " + args);
			try {
				TransactionAttribute.Type tType = getTransactionType(method);
				Transaction.make(bean, tType);
				method = beanClass.getDeclaredMethod(method.getName(), method.getParameterTypes());
				result = method.invoke(bean, args);
				Transaction.destroy(bean, tType);
			} catch (Throwable e){
				Logger.log(e.getMessage());
				throw e;
			}
		}
		beanManager.takeBean(bean);
		bean = null;
		Logger.log("result is : " + result);
		return result;
	}

	private TransactionAttribute.Type getTransactionType(Method method) throws NoSuchMethodException, SecurityException {
		Method beanMethod = beanClass.getMethod(method.getName(), method.getParameterTypes());
		TransactionAttribute ta = beanMethod.getAnnotation(TransactionAttribute.class);
		if(ta != null)
			return ta.value();
		return defaultTransactionType;
	}
}
