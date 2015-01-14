package fr.isima.ejb.container;

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
		System.out.println("Persisit an Entity");
	}

	@Override
	public void remove(Object entity) {

		System.out.println("Remove an Entity");
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean contains(Object entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void merge(T entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void detach() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isOpen() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void joinTransaction() {
		// TODO Auto-generated method stub
		
	}

}
