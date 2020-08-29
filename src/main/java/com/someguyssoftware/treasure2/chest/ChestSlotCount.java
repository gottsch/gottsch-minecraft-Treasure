/**
 * 
 */
package com.someguyssoftware.treasure2.chest;

/**
 * @author Mark Gottschling on Mar 3, 2018
 *
 */
public enum ChestSlotCount {
	STANDARD(27),
	STRONGBOX(15),
	SKULL(9),
	COMPRESOR(52),
	WITHER(42);
	
	private int size;
	
	ChestSlotCount(int size) {
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
