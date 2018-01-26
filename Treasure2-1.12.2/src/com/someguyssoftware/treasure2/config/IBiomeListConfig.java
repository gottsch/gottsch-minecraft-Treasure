/**
 * 
 */
package com.someguyssoftware.treasure2.config;

import java.util.List;

import com.someguyssoftware.gottschcore.biome.BiomeTypeHolder;

/**
 * @author Mark Gottschling on Jan 23, 2018
 *
 */
public interface IBiomeListConfig {

	List<BiomeTypeHolder> getBiomeWhiteList();
	void setBiomeWhiteList(List<BiomeTypeHolder> biomeWhiteList);

	List<BiomeTypeHolder> getBiomeBlackList();
	void setBiomeBlackList(List<BiomeTypeHolder> biomeBlackList);

}
