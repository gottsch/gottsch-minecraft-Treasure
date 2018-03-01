/**
 * 
 */
package com.someguyssoftware.treasure2.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

/**
 * 
 * @author Mark Gottschling on Feb 16, 2018
 *
 */
public interface IWellConfig extends IBiomeListConfig {
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
	
	public boolean isWellAllowed();
	public int getChunksPerWell();
	public double getGenProbability();

	IWellConfig setWellAllowed(boolean wellAllowed);

	IWellConfig setChunksPerWell(int chunksPerWell);

	IWellConfig setGenProbability(double genProbability);

	String[] getRawBiomeBlackList();

	IWellConfig setRawBiomeBlackList(String[] rawBiomeBlackList);

	String[] getRawBiomeWhiteList();

	IWellConfig setRawBiomeWhiteList(String[] rawBiomeWhiteList); 
}
