package fr.isima.ejb.container;

public interface EntityManager {
	/*
	 * Make an instance managed and persistent.
	 */
	void persist(Object entity);
	/*
	 * Remove the entity instance.
	 */
	void remove(Object entity);
	
}
