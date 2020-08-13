/**
 * 
 */
package com.someguyssoftware.treasure2.enums;

/**
 * 
 * @author Mark Gottschling on Jan 30, 2019
 *
 */
public enum WorldGenerators {
	CHEST("chest"),
	WELL("well"),
	GEM("gem"),
	WITHER_TREE("witherTree"),
	SURFACE_CHEST("surfaceChest"),
	SUBMERGED_CHEST("submergedChest"),
	OASIS("oasis");
	
	private String value;
	
	/**
	 * 
	 * @param value
	 */
	WorldGenerators(String value) {
		this.value = value;
	}
	
	/**
	 * 
	 */
	public String toString() {
		return getValue();
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
