package fr.isima.ejb.container.tests.mocks;

import fr.isima.ejb.container.annotations.Singleton;

@Singleton
public class SingletonBean implements SingletonBeanInterface{
	public void speak() {
		System.out.println("SingletonBean is speaking");
	}

}
