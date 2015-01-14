package fr.isima.ejb.container.tests.mocks;

import fr.isima.ejb.container.annotations.Stateless;

@Stateless
public class StatelessBean implements StatelessBeanInterface{

	public void speak() {
		System.out.println("StatelessBean is speaking");
	}

}
