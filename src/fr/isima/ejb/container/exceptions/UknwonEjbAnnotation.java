package fr.isima.ejb.container.exceptions;

public class UknwonEjbAnnotation extends Exception{

	public UknwonEjbAnnotation(Class<?> classe) {
		super("Uknwon Ejb Annotation of the class : " + classe);
	}

}
