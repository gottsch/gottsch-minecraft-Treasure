/**
 * 
 */
package com.someguyssoftware.treasure2.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

/**
 * @author Mark Gottschling on Jan 23, 2018
 *
 */
public interface IChestConfig extends IBiomeListConfig {
	/**
	 * Loads the Forge mod Configuration file.
	 * @param file
	 * @return the loaded Forge mod Configuration;
	 */
	default public Configuration load(File file) {
        Configuration config = new Configuration(file);
        config.load();
		return config;
	}
	
	public boolean isChestAllowed();
	public boolean isAboveGroundAllowed();
	public boolean isBelowGroundAllowed();
	public int getChunksPerChest();
	public double getGenProbability();
	public int getMinYSpawn();

	IChestConfig setChestAllowed(boolean chestAllowed);

	IChestConfig setChunksPerChest(int chunksPerChest);

	IChestConfig setGenProbability(double genProbability);

	IChestConfig setMinYSpawn(int minYSpawn);

	IChestConfig setAboveGroundAllowed(boolean aboveGroundAllowed);

	IChestConfig setBelowGroundAllowed(boolean belowGroundAllowed);

	String[] getRawBiomeBlackList();

	IChestConfig setRawBiomeBlackList(String[] rawBiomeBlackList);

	String[] getRawBiomeWhiteList();

	IChestConfig setRawBiomeWhiteList(String[] rawBiomeWhiteList); 
}
