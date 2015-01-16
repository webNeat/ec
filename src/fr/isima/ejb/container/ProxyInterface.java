package fr.isima.ejb.container;

public interface ProxyInterface {
	Class<?> getOriginalClass();
	Object getBean();	
}
