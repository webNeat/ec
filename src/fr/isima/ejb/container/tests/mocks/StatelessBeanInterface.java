package fr.isima.ejb.container.tests.mocks;

import fr.isima.ejb.container.annotations.Local;

@Local
public interface StatelessBeanInterface {
	boolean isPreDestroyed();
	boolean isPostConstructed();
	void postConstructMethod();
	void preDestroyMethod();
	void doSomething();
	void doRequiredTransaction();
	void doNeverTransaction();
	void doRequiresNewTransaction();	
}
