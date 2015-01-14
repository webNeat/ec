package fr.isima.ejb.container;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;

import fr.isima.ejb.container.annotations.EJB;

public class Container {
	private static  Container contanier = null;
	public static Container getContanier() {
		if(contanier == null)
			contanier = new Container();
		return contanier;
	}
	
	private Map<Class<?>, Class<?>> interfaceToClass;
	private Container() {
		interfaceToClass = new HashMap<Class<?>, Class<?>>();
		fillInterfaceToClass();
	}

	private void fillInterfaceToClass() {
		// Loop over all classes annotated with @Stateless 
		// and find the corresponding interface for each class then fill our map
		// TODO ...
		Reflections reflection = new Reflections();
		List<Class<?>> classes = new ArrayList<Class<?>>();
		
		
	}

	public void handleAnnotations(Object client) {
		handleEjbAnnotation(client);
	}

	private void handleEjbAnnotation(Object client) {
		// get all fields having @EJB annotation
		Reflections reflections = new Reflections( client.getClass().getName(), new FieldAnnotationsScanner()) ;
		Set<Field> fields = reflections.getFieldsAnnotatedWith( EJB.class );
		// foreach field
		// create proxy for that ejb
		// inject the proxy
	}

}
  