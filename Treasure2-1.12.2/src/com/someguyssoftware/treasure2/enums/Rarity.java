/**
 * 
 */
package com.someguyssoftware.treasure2.enums;

/**
 * @author Mark Gottschling onJan 11, 2018
 *
 */
public enum Rarity {
	COMMON(0),
	UNCOMMON(1),
	SCARCE(2),
	RARE(3),
	EPIC(4);//,
//	UNIQUE;
	
	private int value;
	
	/**
	 * 
	 * @param value
	 */
	Rarity(int value) {
		this.value = value;
	}
	
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

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}
}
