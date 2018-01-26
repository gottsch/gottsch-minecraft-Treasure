/**
 * 
 */
package com.someguyssoftware.lootbuilder.exception;

/**
 * 
 * @author Mark Gottschling on Jan 18, 2018
 *
 */
public class DatabaseInitializationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6357187077221278636L;

	public DatabaseInitializationException() {
		super();
	}
	
	public DatabaseInitializationException(String error) {
		super(error);
	}
	
	public DatabaseInitializationException(Throwable throwable) {
		super(throwable);
	}
	
	public DatabaseInitializationException(String error, Throwable throwable) {
		super(error, throwable);
	}
}
