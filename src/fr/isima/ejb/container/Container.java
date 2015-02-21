package fr.isima.ejb.container;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fr.isima.ejb.container.annotations.EJB;
import fr.isima.ejb.container.annotations.Local;
import fr.isima.ejb.container.annotations.PersistenceContext;
import fr.isima.ejb.container.annotations.PostConstruct;
import fr.isima.ejb.container.annotations.PreDestroy;
import fr.isima.ejb.container.annotations.Singleton;
import fr.isima.ejb.container.annotations.Stateless;
import fr.isima.ejb.container.exceptions.EjbClassNotFoundException;
import fr.isima.ejb.container.exceptions.EjbInjectionException;
import fr.isima.ejb.container.exceptions.LocalAllInterfacesUnfoundException;
import fr.isima.ejb.container.exceptions.NoLocalInterfaceIsImplemented;
import fr.isima.ejb.container.exceptions.PersistanceContextInjectionException;
import fr.isima.ejb.container.exceptions.PostConstructInvokationException;
import fr.isima.ejb.container.exceptions.PreDestroyInvokationException;
import fr.isima.ejb.container.exceptions.SingletonBeanMakingExecption;
import fr.isima.ejb.container.logging.Logger;

public class Container {
	private static Container contanier = null;

	public static Container getInstance() throws LocalAllInterfacesUnfoundException,
			NoLocalInterfaceIsImplemented, SingletonBeanMakingExecption {
		if (contanier == null)
			contanier = new Container();
		return contanier;
	}

	private Map<String, Class<?>> interfaceToClass;
	private BeanManager beanManager;
	private Container() throws LocalAllInterfacesUnfoundException, NoLocalInterfaceIsImplemented, SingletonBeanMakingExecption {
		interfaceToClass = new HashMap<String, Class<?>>();
		beanManager = BeanManager.getInstance();
		fillInterfaceToClass();
	}

	private void fillInterfaceToClass() throws LocalAllInterfacesUnfoundException, NoLocalInterfaceIsImplemented, SingletonBeanMakingExecption {
		// Loop over all classes annotated with @Stateless and @Statefull
		// and find the corresponding interface for each class then fill our map
		Set<Class<?>> statelessClasses = AnnotationsHelper.getClassesAnnotatedWith(Stateless.class);
		Set<Class<?>> singletonClasses = AnnotationsHelper.getClassesAnnotatedWith(Singleton.class);

		for(Class<?> ejbClass : statelessClasses)
			beanManager.addStatelessClass(ejbClass);
		for(Class<?> ejbClass : singletonClasses)
			beanManager.addSingletonClass(ejbClass);
		
		statelessClasses.addAll(singletonClasses);
		for(Class<?> ejbClass : statelessClasses){
			Class<?> interfaces[] =  ejbClass.getInterfaces();
			if(interfaces.length > 0){
				int taille = interfaces.length;
				int cp = 0;
				for(Class<?> ejbInterface : interfaces){
					if(AnnotationsHelper.isAnnotatedWith(ejbInterface, Local.class)){
						interfaceToClass.put(ejbInterface.getName(), ejbClass);
						break;
					} else
						cp++;
				}
				if(taille == cp){
					throw new LocalAllInterfacesUnfoundException(ejbClass);
				}
			}else{
				throw new NoLocalInterfaceIsImplemented(ejbClass);
			}
		}
	}
	public Map<String, Class<?>> getInterfaceToClass() {
		return interfaceToClass;
	}

	public void handleAnnotations(Object client) {
		Logger.log("Handling annotations");
		try {
			handleEjbAnnotation(client);
			handlePersistenceAnnotation(client);
		} catch (Exception e) {
			Logger.log("Error while handling annotations : " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void handleEjbAnnotation(Object client) throws EjbInjectionException, EjbClassNotFoundException {
		Logger.log("Handling @EJB annotations");
		// get all fields having @EJB annotation
		Set<Field> fields = AnnotationsHelper.getFieldsAnnotatedWith(
				client.getClass(), EJB.class);
		for (Field field : fields) {
			String fieldInterfaceName = field.getType().getName();
			Logger.log("Found an EJB with the interface " + fieldInterfaceName);
			if (interfaceToClass.containsKey(fieldInterfaceName)) {
				Class<?> beanClass = interfaceToClass.get(fieldInterfaceName);
				Logger.log("The corresponding class is " + beanClass.getName());
				// create proxy for that ejb
				Object beanProxy = null;
				try {
					beanProxy = Proxy.newProxyInstance(beanClass.getClassLoader(), getProxyInterfacesOf(beanClass), new EJBHandler(beanClass));
					// inject the proxy
					field.setAccessible(true);
					field.set(client, beanProxy);
				} catch (IllegalArgumentException | IllegalAccessException | LocalAllInterfacesUnfoundException | NoLocalInterfaceIsImplemented | SingletonBeanMakingExecption e) {
					throw new EjbInjectionException(e);
				}
			} else {
				throw new EjbClassNotFoundException(field.getType());
			}
		}
	}
	
	private void handlePersistenceAnnotation(Object client) throws PersistanceContextInjectionException {
		// get all fields having @PersistenceContext annotation
		Logger.log("Handling @PersistenceContext annotations");		
		Set<Field> fields = AnnotationsHelper.getFieldsAnnotatedWith(client.getClass(), PersistenceContext.class);
		for(Field field : fields){
			if(field.getType().getSimpleName().equals("EntityManager")) {
				try{
					field.setAccessible(true);
					field.set(client, EntityManagerImpl.getEntityManager());
				} catch(Exception e) {
					throw new PersistanceContextInjectionException(e);
				}
			}
		}
	}
	
	private Class<?>[] getProxyInterfacesOf(Class<?> beanClass){
		Class<?>[] ints = beanClass.getInterfaces();
		Class<?>[] res = new Class<?>[ints.length + 1];
		for(int i = 0; i < ints.length; i++)
			res[i] = ints[i];
		res[ints.length] = ProxyInterface.class;
		return res;
	}

	void invokePostConstructMethodsOf(Object bean) throws PostConstructInvokationException {
		Logger.log("Invoking the post construct methods");
		Set<Method> methods = AnnotationsHelper.getMethodsAnnotatedWith(bean.getClass(), PostConstruct.class);
		for(Method m : methods){
			Logger.log("trying to invoke the method '" + m.getName() + "'");
			try {
				m.invoke(bean, new Object[]{});
				Logger.log("the method '" + m.getName() + "' was successfully invoked");
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
				throw new PostConstructInvokationException(m, e);
			}
		}
	}	
	void invokePreDestroyMethodsOf(Object bean) throws PreDestroyInvokationException {
		Logger.log("Invoking the pre destroy methods");
		Set<Method> methods = AnnotationsHelper.getMethodsAnnotatedWith(bean.getClass(), PreDestroy.class);
		for(Method m : methods){
			Logger.log("trying to invoke the method '" + m.getName() + "'");
			try {
				m.invoke(bean, new Object[]{});
				Logger.log("the method '" + m.getName() + "' was successfully invoked");
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
				throw new PreDestroyInvokationException(m, e);			
			}
		}
	}
}