/**
 * 
 */
package com.someguyssoftware.treasure2.config;

import java.util.List;

/**
 * 
 * @author Mark Gottschling on Feb 16, 2018
 *
 */
public interface IWellsConfig {
	
	public void init();
	public boolean isWellAllowed();
	public int getChunksPerWell();
	public double getGenProbability();
	
	public List<String> getBiomeWhiteList();
	public List<String> getBiomeBlackList();
//	public List<BiomeTypeHolder> getBiomeTypeWhiteList();
//	public List<BiomeTypeHolder> getBiomeTypeBlackList();
}