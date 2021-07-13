/**
 * 
 */
package com.someguyssoftware.treasure2.config;

import java.util.List;

import com.someguyssoftware.gottschcore.biome.BiomeTypeHolder;

import net.minecraft.world.biome.Biome;

/**
 * @author Mark Gottschling on Jan 23, 2018
 *
 */
public interface IChestConfig {
	public void init();
	
	public boolean isEnableChest();
	public boolean isSurfaceAllowed();
	public boolean isSubterraneanAllowed();
	public int getChunksPerChest();
	@Deprecated
	public int getAvgChunksPerChestVariance();
	public double getGenProbability();
	@Deprecated
	public int getMinYSpawn();
	public double getMimicProbability();
	
//	public List<Biome> getBiomeWhiteList();
//	public List<Biome> getBiomeBlackList();
	public List<String> getBiomeWhiteList();
	public List<String> getBiomeBlackList();
	public List<BiomeTypeHolder> getBiomeTypeWhiteList();
	public List<BiomeTypeHolder> getBiomeTypeBlackList();

	public int getMinDepth();
	public int getMaxDepth();
}