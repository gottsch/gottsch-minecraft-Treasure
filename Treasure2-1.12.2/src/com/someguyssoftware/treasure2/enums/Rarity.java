/**
 * 
 */
package com.someguyssoftware.treasure2.enums;

/**
 * @author Mark Gottschling onJan 11, 2018
 *
 */
public enum Rarity {
	COMMON,
	UNCOMMON,
	SCARCE,
	RARE,
	EPIC;//,
//	UNIQUE;
	
	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name();
	}
	
	/**
	 * 
	 */
	public String toString() {
		return name();
	}
}
