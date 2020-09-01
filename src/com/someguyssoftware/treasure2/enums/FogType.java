/**
 * 
 */
package com.someguyssoftware.treasure2.enums;

/**
 * 
 * @author Mark Gottschling on May 9, 2018
 *
 */
public enum FogType {
	NORMAL(1),
	WITHER(2),
	POISON(3);
	
	private int type;
	
	FogType(int type) {
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

}
