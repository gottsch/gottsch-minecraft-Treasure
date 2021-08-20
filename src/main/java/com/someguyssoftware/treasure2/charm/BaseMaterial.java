package com.someguyssoftware.treasure2.charm;

/**
 * 
 * @author Mark Gottschling on Aug 16, 2021
 *
 */
public enum BaseMaterial {
	NONE(0, 0),
	COPPER(1, 2),
	SILVER(2, 3),
	GOLD(3, 4);
	
	private int value;
	private int maxCharmLevel;
	
	BaseMaterial(int value, int maxLevel) {
		this.value = value;
		this.maxCharmLevel = maxLevel;
	}

	public int getValue() {
		return value;
	}

	public int getMaxCharmLevel() {
		return maxCharmLevel;
	}
}