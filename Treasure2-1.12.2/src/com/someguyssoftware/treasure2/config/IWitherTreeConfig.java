/**
 * 
 */
package com.someguyssoftware.treasure2.config;

import java.util.List;

import com.someguyssoftware.gottschcore.biome.BiomeTypeHolder;

import net.minecraft.world.biome.Biome;

/**
 * @author Mark Gottschling on Mar 25, 2018
 *
 */
public interface IWitherTreeConfig {
	
	// TODO this is common to all generator config (chest, well, tree, etc). should be intermediate interface (IRawBiomeList) between
	// this and IBiomesConfig OR should be part of IBiomesConfig

	public void init();
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
