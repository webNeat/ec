package fr.isima.ejb.container.tests.mocks;

import fr.isima.ejb.container.annotations.PostConstruct;
import fr.isima.ejb.container.annotations.PreDestroy;
import fr.isima.ejb.container.annotations.Stateless;

@Stateless
public class StatelessBean implements StatelessBeanInterface {
	private boolean postConstructed;
	private boolean preDestroyed;
	
	public StatelessBean() {
		postConstructed = false;
		preDestroyed = false;
	}
	
	public boolean isPostConstructed() {
		return postConstructed;
	}
	public boolean isPreDestroyed() {
		return preDestroyed;
	}
	
	public void speak() {
		System.out.println("StatelessBean is speaking");
	}

	@PostConstruct
	public void postConstructMethod(){
		postConstructed = true;
	}
	
	@PreDestroy	
	public void preDestroyMethod(){
		preDestroyed = true;
	}

}
