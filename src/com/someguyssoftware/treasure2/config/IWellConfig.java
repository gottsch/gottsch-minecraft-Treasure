/**
 * 
 */
package com.someguyssoftware.treasure2.config;

import java.util.List;

import com.someguyssoftware.gottschcore.biome.BiomeTypeHolder;

import net.minecraft.world.biome.Biome;

/**
 * 
 * @author Mark Gottschling on Feb 16, 2018
 *
 */
public interface IWellConfig {
	
	public void init();
	public boolean isWellAllowed();
	public int getChunksPerWell();
	public double getGenProbability();
	
	public List<Biome> getBiomeWhiteList();
	public List<Biome> getBiomeBlackList();
	public List<BiomeTypeHolder> getBiomeTypeWhiteList();
	public List<BiomeTypeHolder> getBiomeTypeBlackList();
}
