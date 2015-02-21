package fr.isima.ejb.container.exceptions;

import java.lang.reflect.Method;

public class PreDestroyInvokationException extends Exception {

	public PreDestroyInvokationException(Method m, Exception e) {
		super("Problem within Invokation of method annoted by PreDestroy : " + m + " Because of : " +  e.getMessage());
	}
}
