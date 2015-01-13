package fr.isima.ejb.container.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

// create an interface and a class for an ejb
// create a client class which uses the ejb
public class ContainerTest {
	// get an instance of our ebj container as attribute
	@Before
	public void init(){
		// make an instance from the client class
	}
	@Test
	public void ejbInjectionTest(){
		// call our container to handle this instance (handle the @EJB annotations)
		// check if the beans were injected (the type of attributes is Proxy)		
	}
	@Test
	public void ejbPostConstructTest(){
		// call our container to handle this instance (handle the @EJB annotations)
		// check if the state attribute of the ebjs with @PostContruct is setted to "Post Contructed"
	}
	@Test
	public void ejbPreDestroyTest(){
		// call our container to handle this instance (handle the @EJB annotations)
		// tell the container to delete an ejb
		// check if the state attribute of the this ejb is setted to "Pre Destroyed"
	}
	@After
	public void destroy(){
		// destroy the instance of client class
	}
}
