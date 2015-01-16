package fr.isima.ejb.container;

import java.util.Stack;

import fr.isima.ejb.container.annotations.TransactionAttribute;
import fr.isima.ejb.container.logging.Logger;

public class Transaction {
	private static Stack<Transaction> all = new Stack<Transaction>();
	private static int counter = 0;
	public static void create(Object bean){
		Transaction t = new Transaction(bean);
		all.push(t);
		t.begin();
		counter ++;
	}
	public static void make(Object bean, TransactionAttribute.Type type){
		switch(type){
			case NEVER: break;
			case REQUIRED:
				if(all.empty()){
					create(bean);
				}
			break;
			case REQUIRES_NEW:
				if(!all.empty())
					all.peek().sleep();
				create(bean);
			break;
		}
	}
	public static void destroy(Object bean, TransactionAttribute.Type type){
		switch(type){
			case NEVER: break;
			case REQUIRED:
				if(all.peek().getBean().equals(bean))
					all.pop().end();
			break;
			case REQUIRES_NEW:
				all.pop().end();
				if(!all.empty())
					all.peek().awake();
			break;
		}
	}
	public static Stack<Transaction> getAll(){
		return all;
	}
	public static int getCounter(){
		return counter;
	}
	public static void setCounter(int c){
		counter = c;
	}
	
	private Object bean;
	private String beanClassName;
	public Transaction(Object bean){
		this.bean = bean;
		beanClassName = bean.getClass().getSimpleName();
	}
	public Object getBean(){
		return bean;
	}
	public void begin() {
		Logger.log("Transaction of " + beanClassName + " : Create");
	}
	public void sleep() {  
		Logger.log("Transaction of " + beanClassName + " : Sleep");
	}
	public void awake() {
		Logger.log("Transaction of " + beanClassName + " : Awake");
	}
	public void end() {
		Logger.log("Transaction of " + beanClassName + " : End");
	}
}
