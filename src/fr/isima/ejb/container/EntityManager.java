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
	/*
	 * Clear the persistence context, causing all managed entities to become detached. 
	 * Changes made to entities that have not been flushed to the database will not be persisted.
	 */
	void clear();
	/*
	 * Check if the instance is a managed entity instance belonging to the current persistence context
	 */
	boolean contains(Object entity);
	/*
	 * Synchronize the persistence context to the underlying database
	 */
	void flush();
	/*
	 *Merge the state of the given entity into the current persistence context 
	 */
	<T> void merge(T entity);
	/*
	 * Close an application-managed entity manager. If this method is called when the entity manager is joined to an active transaction, 
	 * the persistence context remains managed until the transaction completes.
	 */
	void close();
	/*
	 * Remove the given entity from the persistence context, causing a managed entity to become detached. 
	 */
	void detach();
	/*
	 * Determine whether the entity manager is open.
	 */
	boolean isOpen();
	/*
	 * Determine whether the entity manager is joined to the current transaction. Returns false if the entity manager 
	 * is not joined to the current transaction or if no transaction is active
	 */
	void joinTransaction();
}
