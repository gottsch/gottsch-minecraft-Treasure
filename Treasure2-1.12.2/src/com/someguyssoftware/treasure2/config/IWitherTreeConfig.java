/**
 * 
 */
package com.someguyssoftware.treasure2.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

/**
 * @author Mark Gottschling on Mar 25, 2018
 *
 */
public interface IWitherTreeConfig extends IBiomeListConfig {
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
	
	// TODO this is common to all generator config (chest, well, tree, etc). should be intermediate interface (IRawBiomeList) between
	// this and IBiomeListConfig OR should be part of IBiomeListConfig
	String[] getRawBiomeBlackList();
	IWitherTreeConfig setRawBiomeBlackList(String[] rawBiomeBlackList);

	String[] getRawBiomeWhiteList();
	IWitherTreeConfig setRawBiomeWhiteList(String[] rawBiomeWhiteList);

	public int getChunksPerTree();
	public IWitherTreeConfig setChunksPerTree(int chunksPerTree);

	public double getGenProbability();
	public IWitherTreeConfig setGenProbability(double genProbability);

	public int getMaxSupportingTrees();
	public IWitherTreeConfig setMaxSupportingTrees(int maxSupportingTrees);

	int getMaxTrunkSize();
	IWitherTreeConfig setMaxTrunkSize(int maxTrunkSize);

	int getMinSupportingTrees();
	IWitherTreeConfig setMinSupportingTrees(int minSupportingTrees); 
}
