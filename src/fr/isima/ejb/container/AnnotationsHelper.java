package fr.isima.ejb.container;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;

import fr.isima.ejb.container.annotations.Stateless;

public class AnnotationsHelper {
	public static Set<Class<?>> getClassesAnnotatedWith(Class<? extends Annotation> annotationClass){
		Reflections refs = new Reflections();
		return refs.getTypesAnnotatedWith(annotationClass);
	}
	
	public static Set<Field> getFieldsAnnotatedWith(Class<?> clientClass, Class<? extends Annotation> annotationClass){
		Reflections refs = new Reflections(clientClass.getName(), new FieldAnnotationsScanner());
		return refs.getFieldsAnnotatedWith(annotationClass);
	}
	
	public static Set<Method> getMethodsAnnotatedWith(Class<?> clientClass, Class<? extends Annotation> annotationClass){
		Reflections refs = new Reflections(clientClass.getName(), new MethodAnnotationsScanner());
		return refs.getMethodsAnnotatedWith(annotationClass);
	}

	public static boolean isAnnotatedWith(Class<?> clientClass,	Class<? extends Annotation> annotationClass) {
		for( Annotation anno : clientClass.getDeclaredAnnotations() ){
			if( annotationClass.isInstance(anno) )
				return true;
		}
		return false;
	}
}
