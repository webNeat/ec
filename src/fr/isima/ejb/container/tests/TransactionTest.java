package fr.isima.ejb.container.tests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.isima.ejb.container.Container;
import fr.isima.ejb.container.Transaction;
import fr.isima.ejb.container.annotations.TransactionAttribute;
import fr.isima.ejb.container.exceptions.LocalAllInterfacesUnfoundException;
import fr.isima.ejb.container.exceptions.NoLocalInterfaceIsImplemented;
import fr.isima.ejb.container.exceptions.SingletonBeanMakingExecption;
import fr.isima.ejb.container.logging.Logger;
import fr.isima.ejb.container.tests.mocks.EjbClient;

public class TransactionTest {
	private Container ejbContainer;
	private EjbClient ejbClient;
	@Before
	public void init(){
		try {
			ejbContainer = Container.getInstance();			
		} catch (LocalAllInterfacesUnfoundException | NoLocalInterfaceIsImplemented | SingletonBeanMakingExecption e) {
			Logger.log(e.getMessage());
			Assert.assertTrue(false);
		}
		ejbClient = new EjbClient();
		ejbContainer.handleAnnotations(ejbClient);
		Transaction.getAll().clear();
		Transaction.setCounter(0);
	}
	
	@Test
	public void requiredWithoutTransactionTest(){
		ejbClient.getStatelessEjb().doRequiredTransaction();
		Assert.assertTrue(Transaction.getCounter() == 1);
		Assert.assertTrue(Transaction.getAll().empty());
	}
	@Test
	public void requiredWithTransactionTest(){
		Transaction.make(this, TransactionAttribute.Type.REQUIRES_NEW);
		Assert.assertTrue(Transaction.getCounter() == 1);
		ejbClient.getStatelessEjb().doRequiredTransaction();
		Assert.assertTrue(Transaction.getCounter() == 1);
		Assert.assertTrue(Transaction.getAll().size() == 1);
		Assert.assertTrue(Transaction.getAll().peek().getBean() == this);
	}	
	@Test
	public void requiresNewTest(){
		Transaction.make(this, TransactionAttribute.Type.REQUIRES_NEW);
		Assert.assertTrue(Transaction.getCounter() == 1);
		ejbClient.getStatelessEjb().doRequiresNewTransaction();
		Assert.assertTrue(Transaction.getCounter() == 2);
		Assert.assertTrue(Transaction.getAll().size() == 1);
		Assert.assertTrue(Transaction.getAll().peek().getBean() == this);
	}
	@Test
	public void neverTest(){
		ejbClient.getStatelessEjb().doNeverTransaction();
		Assert.assertTrue(Transaction.getCounter() == 0);
		Assert.assertTrue(Transaction.getAll().empty());
	}
	
	@After
	public void destroy(){
		ejbClient = null;
	}
}
