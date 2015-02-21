package fr.isima.ejb.container.exceptions;

public class ProxyMakingException extends Exception{

	public ProxyMakingException(Exception e) {
		super("Proxy Making Exception " + e.getMessage());
	}

}
