package fr.isima.ejb.container.exceptions;

public class NoLocalInterfaceIsImplemented extends Exception{

	public NoLocalInterfaceIsImplemented(Class<?> ejbClass) {
		super("no local interface is implemented from class : " + ejbClass.getSimpleName());
	}

}
