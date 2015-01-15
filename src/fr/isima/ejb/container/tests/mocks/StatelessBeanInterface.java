package fr.isima.ejb.container.tests.mocks;

import fr.isima.ejb.container.annotations.Local;

@Local
public interface StatelessBeanInterface {
	void speak();
	boolean isPreDestroyed();
	boolean isPostConstructed();
}
