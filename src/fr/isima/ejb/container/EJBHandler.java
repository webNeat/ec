package fr.isima.ejb.container;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import fr.isima.ejb.container.annotations.TransactionAttribute;
import fr.isima.ejb.container.logging.Logger;

public class EJBHandler implements InvocationHandler {
	private Object bean;
	private TransactionAttribute.Type defaultTransactionType;
	public EJBHandler(Object bean){
		this.bean = bean;
		TransactionAttribute ta = bean.getClass().getAnnotation(TransactionAttribute.class);
		if(ta == null)
			defaultTransactionType = TransactionAttribute.Type.NEVER;
		else
			defaultTransactionType = ta.value();
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result = null;
		if(method.getName().equals("getOriginalClass")){
			Logger.log("getOriginalClass called");
			result = bean.getClass();
		} else if(method.getName().equals("getBean")){
			Logger.log("getBean called");
			result = bean;			
		} else if(method.getName().equals("equals")){
			Logger.log("equals called");
			result = ( bean == ((ProxyInterface)args[0]).getBean() );	
		} else {
			Logger.log(method.getName() + " is called with params : " + args);
			try {
				TransactionAttribute.Type tType = getTransactionType(method);
				Transaction.make(bean, tType);
				method = bean.getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());
				result = method.invoke(bean, args);
				Transaction.destroy(bean, tType);
			} catch (Throwable e){
				Logger.log(e.getMessage());
				throw e;
			}
		}
		Logger.log("result is : " + result);
		return result;
	}

	private TransactionAttribute.Type getTransactionType(Method method) throws NoSuchMethodException, SecurityException {
		Method beanMethod = bean.getClass().getMethod(method.getName(), method.getParameterTypes());
		TransactionAttribute ta = beanMethod.getAnnotation(TransactionAttribute.class);
		if(ta != null)
			return ta.value();
		return defaultTransactionType;
	}
}
