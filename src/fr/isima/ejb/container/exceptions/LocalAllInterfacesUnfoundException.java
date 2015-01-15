package fr.isima.ejb.container.exceptions;

public class LocalAllInterfacesUnfoundException extends Exception{
	
	public LocalAllInterfacesUnfoundException(Class<?> ejbClass) {
		super("All interfaces are not annotated by @Local from class : " + ejbClass.getSimpleName());
		
	}

}
