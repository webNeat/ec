package fr.isima.ejb.container.tests;

import java.lang.reflect.Proxy;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import fr.isima.ejb.container.Container;
import fr.isima.ejb.container.EntityManagerImp;
import fr.isima.ejb.container.tests.mocks.EjbClient;
import fr.isima.ejb.container.tests.mocks.EjblClientSingleton;

public class ContainerTest {
	private Container ejbContainer;
	private EjbClient ejbClient;
	private EjblClientSingleton ejbClientSingleton;
	@Before
	public void init(){
		ejbContainer = Container.getContanier();
		ejbClient = new EjbClient();
		ejbClientSingleton = new EjblClientSingleton();
	}
	@Ignore
	public void ejbInterfaceToClasseTest(){
		Class<?> classeFromMap = ejbContainer.getInterfaceToClass().get("fr.isima.ejb.container.tests.mocks.StatelessBeanInterface");
		Assert.assertEquals(classeFromMap.getName(), "fr.isima.ejb.container.tests.mocks.StatelessBean");
	}
	@Ignore
	public void emInjectionTest(){
		ejbContainer.handleAnnotations(ejbClient);
		Assert.assertTrue(ejbClient.getEntityManager() instanceof EntityManagerImp);
	}
	@Ignore
	public void ejbInjectionTest(){
		// call our container to handle this instance (handle the @EJB annotations)
		ejbContainer.handleAnnotations(ejbClient);
		// check if the beans were injected (the type of attributes is Proxy)
		Assert.assertTrue(ejbClient.getStatelessEjb() instanceof Proxy);
	}
	@Test
	public void ejbSingletonInjectionTest(){
		// call our container to handle this instance (handle the @EJB annotations)
		ejbContainer.handleAnnotations(ejbClient);
		
		// check if the beans were injected (the type of attributes is Proxy)
		Assert.assertTrue(ejbClient.getStatelessEjb() instanceof Proxy);
		//Assert.assertTrue(ejbClientSingleton.getSingletonEjb() instanceof Proxy);
		
	}
	/*
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
	}*/
	@After
	public void destroy(){
		ejbClient = null;
	}
}
