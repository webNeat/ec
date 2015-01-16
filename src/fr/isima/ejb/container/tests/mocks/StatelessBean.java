package fr.isima.ejb.container.tests.mocks;

import fr.isima.ejb.container.annotations.PostConstruct;
import fr.isima.ejb.container.annotations.PreDestroy;
import fr.isima.ejb.container.annotations.Stateless;
import fr.isima.ejb.container.annotations.TransactionAttribute;
import fr.isima.ejb.container.annotations.TransactionAttribute.Type;
import fr.isima.ejb.container.logging.Logger;

@Stateless
@TransactionAttribute(Type.NEVER)
public class StatelessBean implements StatelessBeanInterface {
	private boolean postConstructed;
	private boolean preDestroyed;
	
	public StatelessBean() {
		Logger.log("Constructed");
		postConstructed = false;
		preDestroyed = false;
	}
	
	public boolean isPostConstructed() {
		return postConstructed;
	}
	public boolean isPreDestroyed() {
		return preDestroyed;
	}
	
	public void doSomething() {
		Logger.log("Doing something");
	}
	@TransactionAttribute(Type.REQUIRED)
	public void doRequiredTransaction() {
		Logger.log("Doing a transaction");
	}
	@TransactionAttribute(Type.NEVER)
	public void doNeverTransaction() {
		Logger.log("Doing a transaction");
	}
	@TransactionAttribute(Type.REQUIRES_NEW)
	public void doRequiresNewTransaction() {
		Logger.log("Doing a transaction");
	}

	@PostConstruct
	public void postConstructMethod(){
		Logger.log("Post Constructed !");
		postConstructed = true;
	}
	
	@PreDestroy	
	public void preDestroyMethod(){
		Logger.log("Pre Destroyed !");
		preDestroyed = true;
	}

}
