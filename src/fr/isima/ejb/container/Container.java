package fr.isima.ejb.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import fr.isima.ejb.container.annotations.Stateless;

public class Container {
	private static  Container contanier = null;
	public static Container getContanier() {
		if(contanier == null)
			contanier = new Container();
		return contanier;
	}
	
	private Map<String, Class<?>> interfaceToClass;
	private Container() {
		interfaceToClass = new HashMap<String, Class<?>>();
		fillInterfaceToClass();
	}

	private void fillInterfaceToClass() {
		// Loop over all classes annotated with @Stateless 
		// and find the corresponding interface for each class then fill our map
		// TODO ...
		Reflections reflection = new Reflections();
		Set<Class<?>> classes = reflection.getTypesAnnotatedWith(Stateless.class);
		for(Class<?> ejbClasse : classes){
			System.out.println("Classe : " + ejbClasse.getName());
			Class<?> interfaces[] =  ejbClasse.getInterfaces();
			for(Class<?> ejbInterface : interfaces){
				//exception when the interface implemented by multiple classes

				System.out.println("Interface : " + ejbInterface.getName());
				interfaceToClass.put(ejbInterface.getName(), ejbClasse);
			}
		}
		
	}
	public Map<String, Class<?>> getInterfaceToClass() {
		return interfaceToClass;
	}

	public void handleAnnotations(Object client) {
		
	}

}
  