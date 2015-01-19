package fr.isima.ejb.container.tests.mocks;

import fr.isima.ejb.container.annotations.Local;

@Local
public interface SingletonBeanInterface {
	void speak();
	StatelessBeanInterface getInnerBean();
}
