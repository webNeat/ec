package fr.isima.ejb.container.tests.mocks;

import fr.isima.ejb.container.annotations.EJB;
import fr.isima.ejb.container.annotations.Singleton;
import fr.isima.ejb.container.logging.Logger;

@Singleton
public class SingletonBean implements SingletonBeanInterface {
	
	@EJB
	StatelessBeanInterface innerBean;
	public void speak() {
		Logger.log("SingletonBean is speaking");
	}
	@Override
	public StatelessBeanInterface getInnerBean() {
		return innerBean;
	}

}
