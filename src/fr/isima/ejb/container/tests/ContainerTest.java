package fr.isima.ejb.container.tests;

import java.lang.reflect.Proxy;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.isima.ejb.container.Container;
import fr.isima.ejb.container.EntityManagerImpl;
import fr.isima.ejb.container.exceptions.LocalAllInterfacesUnfoundException;
import fr.isima.ejb.container.exceptions.NoLocalInterfaceIsImplemented;
import fr.isima.ejb.container.exceptions.PreDestroyInvokationException;
import fr.isima.ejb.container.exceptions.SingletonBeanMakingExecption;
import fr.isima.ejb.container.logging.Logger;
import fr.isima.ejb.container.tests.mocks.EjbClient;
import fr.isima.ejb.container.tests.mocks.EjblClientSingleton;
import fr.isima.ejb.container.tests.mocks.StatelessBeanInterface;

public class ContainerTest {
	private Container ejbContainer;
	private EjbClient ejbClient;
	private EjblClientSingleton ejbClientSingleton;
	@Before
	public void init(){
		try {
			ejbContainer = Container.getInstance();			
		} catch (LocalAllInterfacesUnfoundException | NoLocalInterfaceIsImplemented | SingletonBeanMakingExecption e) {
			Logger.log(e.getMessage());
			Assert.assertTrue(false);
		}
		ejbClient = new EjbClient();
		ejbClientSingleton = new EjblClientSingleton();
	}

	@Test
	public void ejbInterfaceToClasseTest(){
		Class<?> classeFromMap = ejbContainer.getInterfaceToClass().get("fr.isima.ejb.container.tests.mocks.StatelessBeanInterface");
		Assert.assertEquals(classeFromMap.getName(), "fr.isima.ejb.container.tests.mocks.StatelessBean");
	}
	@Test
	public void emInjectionTest(){
		ejbContainer.handleAnnotations(ejbClient);
		Assert.assertTrue(ejbClient.getEntityManager() instanceof EntityManagerImpl);
	}
	@Test
	public void ejbInjectionTest(){
		// call our container to handle this instance (handle the @EJB annotations)
		ejbContainer.handleAnnotations(ejbClient);
		// check if the beans were injected (the type of attributes is Proxy)
		Assert.assertTrue(ejbClient.getStatelessEjb() != null);
		Assert.assertTrue(ejbClient.getStatelessEjb() instanceof Proxy);
	}
	@Test
	public void ejbSingletonInjectionTest(){
		// call our container to handle this instance (handle the @EJB annotations)
		ejbContainer.handleAnnotations(ejbClient);
		ejbContainer.handleAnnotations(ejbClientSingleton);
		
		Assert.assertTrue(ejbClient.getSingletonEjb() instanceof Proxy);
		Assert.assertTrue(ejbClientSingleton.getSingletonEjb() instanceof Proxy);
		Assert.assertTrue(ejbClientSingleton.getSingletonEjb().equals(ejbClient.getSingletonEjb()));
	}
	@Test
	public void ejbPostConstructTest(){
		// call our container to handle this instance (handle the @EJB annotations)
		ejbContainer.handleAnnotations(ejbClient);
		// check if the ebj was postConstructed
		Assert.assertTrue(ejbClient.getStatelessEjb().isPostConstructed());
	}
	@Test
	public void ejbPreDestroyTest() throws PreDestroyInvokationException{
		// call our container to handle this instance (handle the @EJB annotations)
		ejbContainer.handleAnnotations(ejbClient);
		// tell the container to delete the ejb
		StatelessBeanInterface bean = ejbClient.getStatelessEjb();
		// check if the state attribute of the this ejb is setted to "Pre Destroyed"
		Assert.assertTrue(ejbClient.getStatelessEjb().isPreDestroyed());
	}	
	@Test
	public void innerEjbInjectionTest(){
		ejbContainer.handleAnnotations(ejbClient);
		Assert.assertTrue(ejbClient.getSingletonEjb().getInnerBean() instanceof Proxy);
	}
	
	@After
	public void destroy(){
		ejbClient = null;
	}
}
