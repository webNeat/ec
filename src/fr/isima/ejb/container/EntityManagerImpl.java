package fr.isima.ejb.container;

import fr.isima.ejb.container.logging.Logger;

public class EntityManagerImpl implements EntityManager{
	private static EntityManagerImpl entityManager;
	
	private EntityManagerImpl() {}
	public static EntityManagerImpl getEntityManager() {
		if(entityManager == null)
			entityManager =  new EntityManagerImpl();	
		return entityManager;
	}
	@Override
	public void persist(Object entity) {
		Logger.log("Persisit an Entity");
	}

	@Override
	public void remove(Object entity) {
		Logger.log("Remove an Entity");
	}

}
