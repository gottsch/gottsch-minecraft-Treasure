/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

/**
 * @author Mark Gottschling on Apr 25, 2020
 *
 */
public enum CharmType {
	HEALING("healing", new double[] {20, 50, 100, 200}),
	SHIELDING("shielding", new double[] {20, 50, 100, 200}, new double[] {0.5, 0.6, 0.7, 0.8}),
	FULLNESS("fullness", new double[] {20, 50, 100, 200}),
	HARVESTING("harvesting", new double[] {20, 30, 40, 50}, new double[] {2, 3, 4, 5}),				 // values here indicate number of harvestable blocks
	ILLUMINATION("illumination", new double[] {3, 6, 12, 20}),
	DECAY("decay", new double[] {20, 50, 100, 200});
	
	
	private String name;
	private double[] baseValues;
	private double[] basePercents;
	private int[] baseDurations;
	
	/**
	 * 
	 * @param level
	 * @param max
	 */
	CharmType(String name, double[] values) {
		this.name = name;
		this.baseValues = values;
		this.basePercents = new double[] {0, 0, 0, 0};
		this.baseDurations = new int[] {0, 0, 0, 0};
	}	

	/**
	 * 
	 * @param value
	 * @param percent
	 */
	CharmType(String name, double[] values, double[] percents) {
		this.name = name;
		this.baseValues = values;
		this.basePercents = percents;
		this.baseDurations = new int[] {0, 0, 0, 0};
	}	
	
	/**
	 * 
	 * @param name
	 * @param values
	 * @param percents
	 * @param durations
	 */
	CharmType(String name, double[] values, double[] percents, int[] durations) {
		this.name = name;
		this.baseValues = values;
		this.basePercents = percents;
		this.baseDurations = durations;
	}
	
	public double[] getBasePercents() {
		return basePercents;
	}

	public double[] getBaseValues() {
		return baseValues;
	}

	public int[] getBaseDurations() {
		return baseDurations;
	}

	public String getName() {
		return name;
	}
}
