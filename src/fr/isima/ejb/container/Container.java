package fr.isima.ejb.container;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;

import fr.isima.ejb.container.annotations.EJB;
import fr.isima.ejb.container.annotations.Local;
import fr.isima.ejb.container.annotations.PersistenceContext;
import fr.isima.ejb.container.annotations.PostConstruct;
import fr.isima.ejb.container.annotations.PreDestroy;
import fr.isima.ejb.container.annotations.Singleton;
import fr.isima.ejb.container.annotations.Stateless;
import fr.isima.ejb.container.exceptions.LocalAllInterfacesUnfoundException;
import fr.isima.ejb.container.exceptions.NoLocalInterfaceIsImplemented;
import fr.isima.ejb.container.logging.Logger;

public class Container {
	private static  Container contanier = null;
	public static Container getContanier() throws LocalAllInterfacesUnfoundException, NoLocalInterfaceIsImplemented {
		if(contanier == null)
			contanier = new Container();
		return contanier;
	}
	
	private Map<String, Class<?>> interfaceToClass;
	private Container() throws LocalAllInterfacesUnfoundException, NoLocalInterfaceIsImplemented {
		interfaceToClass = new HashMap<String, Class<?>>();
		fillInterfaceToClass();
	}

	private void fillInterfaceToClass() throws LocalAllInterfacesUnfoundException, NoLocalInterfaceIsImplemented {
		// Loop over all classes annotated with @Stateless 
		// and find the corresponding interface for each class then fill our map
		Set<Class<?>> classes = AnnotationsHelper.getClassesAnnotatedWith(Stateless.class);
		classes.addAll(AnnotationsHelper.getClassesAnnotatedWith(Singleton.class));
		for(Class<?> ejbClasse : classes){
			Class<?> interfaces[] =  ejbClasse.getInterfaces();
			if(interfaces.length > 0){
				int taille = interfaces.length;
				int cp = 0;
				for(Class<?> ejbInterface : interfaces){
					Logger.log("Interface : " + ejbInterface.getSimpleName() + " class : " + ejbClasse.getSimpleName());
					if(AnnotationsHelper.isAnnotatedWith(ejbInterface, Local.class)){
						interfaceToClass.put(ejbInterface.getName(), ejbClasse);
						Logger.log("local");
						break;
					}
					else
						cp++;
				}
				if(taille == cp){
					throw new LocalAllInterfacesUnfoundException(ejbClasse);
				}
			}else{
				throw new NoLocalInterfaceIsImplemented(ejbClasse);
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
		}		
	}

	private void handleEjbAnnotation(Object client) {
		Logger.log("Handling @EJB annotations");
		// get all fields having @EJB annotation
		Set<Field> fields = AnnotationsHelper.getFieldsAnnotatedWith(client.getClass(), EJB.class );
		for(Field field : fields){
			String fieldInterfaceName = field.getType().getName();
			Logger.log("Found an EJB with the interface " + fieldInterfaceName);
			if(interfaceToClass.containsKey(fieldInterfaceName)){
				Class<?> beanClass = interfaceToClass.get(fieldInterfaceName);
				Logger.log("The corresponding class is " + beanClass.getName());
				// create proxy for that ejb
				Object bean = getProxy(beanClass);
				// Execute @PostConstruct methods
				invokePostConstructMethods(bean);
				// inject the proxy
				field.setAccessible(true);
				try {
					field.set(client, bean);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				// Throw some Exception ejb interafce not found 
			}
		}
	}

	private void invokePostConstructMethods(Object bean) {
		Logger.log("Invoking the post construct methods");
		Set<Method> methods = AnnotationsHelper.getMethodsAnnotatedWith(bean.getClass(), PostConstruct.class);
		Logger.log("Class : " + bean.getClass().getName());
//		for(Method m : methods){
//			Logger.log("trying to invoke the method '" + m.getName() + "'");
//			try {
//				m.invoke(bean, new Object[]{});
//				Logger.log("the method '" + m.getName() + "' was successfully invoked");
//			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//				e.printStackTrace();
//			}
//		}
	}
	
	private void invokePreDestroyMethods(Object bean) {
		Set<Method> methods = AnnotationsHelper.getMethodsAnnotatedWith(bean.getClass(), PreDestroy.class);
		for(Method m : methods){
			try {
				m.invoke(bean, new Object[]{});
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	private Object getProxy(Class<?> clientClass) {
		Object bean = null;
		// check the annotation of the ejb class
		// for @stateless
		if(AnnotationsHelper.isAnnotatedWith(clientClass, Stateless.class)){
			bean = Beans.getInstance().make(clientClass);
		}
		// for @singleton
		if(AnnotationsHelper.isAnnotatedWith(clientClass, Singleton.class)){
			bean = Beans.getInstance().makeSingleton(clientClass);
		}
		return bean;
	}
	
	public void removeBean(Object bean){
		invokePreDestroyMethods(bean);
	}
	
	private void handlePersistenceAnnotation(Object client){
		// get all fields having @PersistenceContext annotation
		Reflections reflection = new Reflections(client.getClass().getName(), new FieldAnnotationsScanner()) ;
		Set<Field> fields = reflection.getFieldsAnnotatedWith(PersistenceContext.class );
		for(Field field : fields){
			if(field.getType().getSimpleName().equals("EntityManager")) {
				try{
				field.setAccessible(true);
				field.set(client, EntityManagerImp.getEntityManager());
				}catch(Exception e){
					System.out.println("Erreur Injection Entity Manager : " + e.getMessage());
					
				}
			}
		}
	}

}
  