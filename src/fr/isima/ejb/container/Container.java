package fr.isima.ejb.container;

import java.util.HashMap;
import java.util.Map;

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
	}

	public void handleAnnotations(Object client) {
		handleEjbAnnotation(client);
	}

	private void handleEjbAnnotation(Object client) {
		// get all fields having @EJB annotation
		// foreach field
		// create proxy for that ejb
		// inject the proxy
	}

}
  