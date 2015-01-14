package fr.isima.ejb.container.tests.mocks;

import fr.isima.ejb.container.annotations.EJB;

public class EjbClient {
	@EJB
	StatelessBeanInterface statelessEjb;

	public StatelessBeanInterface getStatelessEjb() {
		return statelessEjb;
	}
}
