package fr.isima.ejb.container.tests.mocks;

public interface StatelessBeanInterface {
	void speak();
	boolean isPreDestroyed();
	boolean isPostConstructed();
}
