/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

/**
 * @author Mark Gottschling on Apr 25, 2020
 *
 */
public enum CharmLevel {
	LEVEL1("level1", 1),
	LEVEL2("level2", 2),
	LEVEL3("level3", 3),
	LEVEL4("leve4", 4);
	
	private String name;
	private int value = 0;
	
	/**
	 * 
	 * @param value
	 */
	CharmLevel(String name, int value) {
		this.name = name;
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}

	public String getName() {
		return name;
	}
}
