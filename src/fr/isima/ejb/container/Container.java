package fr.isima.ejb.container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.reflections.Reflections;

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
		
	}

}
  