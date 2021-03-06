package fr.isima.ejb.container.tests.mocks;

import fr.isima.ejb.container.EntityManager;
import fr.isima.ejb.container.annotations.EJB;
import fr.isima.ejb.container.annotations.PersistenceContext;

public class EjbClient {
	@EJB
	StatelessBeanInterface statelessEjb;
	@EJB
	SingletonBeanInterface singletonEjb;
	
	@PersistenceContext(unitName = "")
    EntityManager entityManager;
	public StatelessBeanInterface getStatelessEjb() {
		return statelessEjb;
	}
	public EntityManager getEntityManager() {
		return entityManager;
	}
	public SingletonBeanInterface getSingletonEjb() {
		return singletonEjb;
	}
	
	
}
