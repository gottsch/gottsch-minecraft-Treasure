/**
 * 
 */
package com.someguyssoftware.treasure2.registry;

import java.util.List;

/**
 * @author Mark Gottschling on Jul 19, 2021
 *
 */
public interface ISimpleListReigstry<T> {

	/**
	 * 
	 * @param key
	 * @return
	 */
	boolean isRegistered(T object);

	/**
	 * Registers a
	 * @param object
	 */
	void register(T object);

	/**
	 * 
	 * @param object
	 */
	void unregister(T object);

	/**
	 * 
	 */
	void clear();

	/**
	 * This will not update parent collection.
	 * @return
	 */
	List<T> getValues();

}
