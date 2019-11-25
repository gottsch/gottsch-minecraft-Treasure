/**
 * 
 */
package com.someguyssoftware.treasure2.config;

import java.io.File;
import java.util.List;

import com.someguyssoftware.gottschcore.biome.BiomeTypeHolder;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.config.Configuration;

/**
 * @author Mark Gottschling on Mar 25, 2018
 *
 */
public interface IWitherTreeConfig {
	/**
	 * Loads the Forge mod Configuration file.
	 * @param file
	 * @return the loaded Forge mod Configuration;
	 */
//	default public Configuration load(File file) {
//        Configuration config = new Configuration(file);
//        config.load();
//		return config;
//	}
	
	// TODO this is common to all generator config (chest, well, tree, etc). should be intermediate interface (IRawBiomeList) between
	// this and IBiomesConfig OR should be part of IBiomesConfig


	public int getChunksPerTree();
	public double getGenProbability();
	public int getMaxSupportingTrees();
	int getMaxTrunkSize();
	int getMinSupportingTrees();
	double getWitherBranchItemGenProbability();
	double getWitherRootItemGenProbability();
	
	public List<Biome> getBiomeWhiteList();
	public List<Biome> getBiomeBlackList();	
	public List<BiomeTypeHolder> getBiomeTypeWhiteList();
	public List<BiomeTypeHolder> getBiomeTypeBlackList();


}
