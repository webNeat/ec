package fr.isima.ejb.container;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;

public class AnnotationsHelper {
	public static Set<Field> getFieldsAnnotatedWith(Class<?> clientClass, Class<? extends Annotation> annotationClass){
		Reflections refs = new Reflections( clientClass.getName(), new FieldAnnotationsScanner());
		return refs.getFieldsAnnotatedWith(annotationClass);
	}

	public static boolean isAnnotatedWith(Class<?> clientClass,	Class<? extends Annotation> annotationClass) {
		for( Annotation anno : clientClass.getDeclaredAnnotations() ){
			if( annotationClass.isInstance(anno) )
				return true;
		}
		return false;
	}	
}
