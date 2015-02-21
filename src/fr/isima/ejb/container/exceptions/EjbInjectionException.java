package fr.isima.ejb.container.exceptions;

public class EjbInjectionException extends Exception{

	public EjbInjectionException(Exception e) {
		super("EjbInjection Exception " + e.getMessage() );
	}

}
