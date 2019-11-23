/**
 * 
 */
package com.someguyssoftware.treasure2.config;

import java.io.File;
import java.util.List;

import com.someguyssoftware.gottschcore.biome.BiomeTypeHolder;

import net.minecraftforge.common.config.Configuration;

/**
 * @author Mark Gottschling on Jan 23, 2018
 *
 */
public interface IChestConfig /*extends IBiomeListConfig*/ {
//	/**
//	 * Loads the Forge mod Configuration file.
//	 * @param file
//	 * @return the loaded Forge mod Configuration;
//	 */
//	default public Configuration load(File file) {
//        Configuration config = new Configuration(file);
//        config.load();
//		return config;
//	}
	
	public boolean isChestAllowed();
	public boolean isSurfaceAllowed();
	public boolean isSubterraneanAllowed();
	public int getChunksPerChest();
	public double getGenProbability();
	public int getMinYSpawn();
	public double getMimicProbability();
	public List<BiomeTypeHolder> getBiomeWhiteList();
	public List<BiomeTypeHolder> getBiomeBlackList();
}
