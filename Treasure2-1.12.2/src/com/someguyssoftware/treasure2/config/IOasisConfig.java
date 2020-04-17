/**
 * 
 */
package com.someguyssoftware.treasure2.config;

import java.util.List;

import com.someguyssoftware.gottschcore.biome.BiomeTypeHolder;

import net.minecraft.world.biome.Biome;

/**
 * 
 * @author Mark Gottschling
 *
 */
public interface IOasisConfig {
	
	public void init();
	public boolean isEnableOasis();
	public int getChunksPerOasis();
	public double getGenProbability();
	
	public List<Biome> getBiomeWhiteList();
	public List<Biome> getBiomeBlackList();
	public List<BiomeTypeHolder> getBiomeTypeWhiteList();
	public List<BiomeTypeHolder> getBiomeTypeBlackList();
	public int getTreesPerChunk();
	public void setTreesPerChunk(int treesPerChunk);
	public double getTreesPerChunkSizeFactor();
	public void setTreesPerChunkSizeFactor(double treesPerChunkSizeFactor);
}
