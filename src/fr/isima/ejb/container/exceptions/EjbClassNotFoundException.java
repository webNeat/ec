package fr.isima.ejb.container.exceptions;

public class EjbClassNotFoundException extends Exception{

	public EjbClassNotFoundException(Class<?> classe) {
		super("Class not found " + classe.getName());
		
	}

}
