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
public interface IBiomesConfig {

	List<Biome>  getWhiteList();
	void setWhiteList(List<Biome> whiteList);
	
	List<Biome>  getBlackList();
	void setBlackList(List<Biome> blackList);
	
	List<BiomeTypeHolder> getTypeWhiteList();
	void setTypeWhiteList(List<BiomeTypeHolder> whiteList);

	List<BiomeTypeHolder> getTypeBlackList();
	void setTypeBlackList(List<BiomeTypeHolder> blackList);

}