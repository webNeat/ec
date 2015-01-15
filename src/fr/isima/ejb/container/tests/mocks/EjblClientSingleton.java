package fr.isima.ejb.container.tests.mocks;

import fr.isima.ejb.container.EntityManager;
import fr.isima.ejb.container.annotations.EJB;
import fr.isima.ejb.container.annotations.PersistenceContext;

public class EjblClientSingleton {
	@EJB
	SingletonBeanInterface singletonEjb;
	
	@PersistenceContext(unitName = "")
    EntityManager entityManager;
	
	public SingletonBeanInterface getSingletonEjb() {
		return singletonEjb;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}
}
