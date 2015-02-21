package fr.isima.ejb.container.exceptions;

import java.lang.reflect.Method;

public class PostConstructInvokationException extends Exception{
	public PostConstructInvokationException(Method method, Exception e) {
		super("Problem within Invokation of method annoted by PostConstruct : " + method + " Because of : " +  e.getMessage());
	}
}
