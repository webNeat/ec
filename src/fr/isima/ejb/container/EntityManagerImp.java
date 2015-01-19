package fr.isima.ejb.container;

import fr.isima.ejb.container.logging.Logger;

public class EntityManagerImp implements EntityManager{
	private static EntityManagerImp entityManager;
	
	private EntityManagerImp() {}
	public static EntityManagerImp getEntityManager() {
		if(entityManager == null)
			entityManager =  new EntityManagerImp();	
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
