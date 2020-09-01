/**
 * 
 */
package com.someguyssoftware.treasure2.enums;

/**
 * @author Mark Gottschling on Mar 3, 2018
 *
 */
public enum FogHeight {
	FULL_FOG(4),
	HIGH_FOG(3),
	MEDIUM_FOG(2),
	LOW_FOG(1);
	
	private int size;
	
	FogHeight(int size) {
		this.size = size;
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}
}
