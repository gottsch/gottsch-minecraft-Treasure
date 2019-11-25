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
 * @author Mark Gottschling on Jan 23, 2018
 *
 */
public interface IChestConfig {
	
	public boolean isChestAllowed();
	public boolean isSurfaceAllowed();
	public boolean isSubterraneanAllowed();
	public int getChunksPerChest();
	public double getGenProbability();
	public int getMinYSpawn();
	public double getMimicProbability();
	
	public List<Biome> getBiomeWhiteList();
	public List<Biome> getBiomeBlackList();
	public List<BiomeTypeHolder> getBiomeTypeWhiteList();
	public List<BiomeTypeHolder> getBiomeTypeBlackList();
}
