/**
 * 
 */
package com.someguyssoftware.treasure2.config;

import java.util.ArrayList;
import java.util.List;

import com.someguyssoftware.gottschcore.biome.BiomeHelper;
import com.someguyssoftware.gottschcore.biome.BiomeTypeHolder;
import com.someguyssoftware.treasure2.biome.TreasureBiomeHelper;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Ignore;
import net.minecraftforge.common.config.Config.Name;

/**
 * @author Mark Gottschling on Nov 23, 2019
 *
 */
// TODO move to GottschCore 1.9/1.8
public class BiomesConfig implements IBiomesConfig {

	@Name("01. White list by biome name:")
	@Comment({"Allowed Biomes for generation. Must match the Biome Name(s)."})
	public String[] rawBiomeWhiteList;
	@Name("02. Black list by biome name:")
	@Comment({"Disallowed Biomes for generation. Must match theBiome Name(s)."})
	public String[] rawBiomeBlackList;
	@Name("03. White list by biome type:")
	@Comment({"Allowabled Biome Types for generation. Must match the Type identifer(s)."})
	public String[] rawBiomeTypeWhiteList;
	@Name("04. Black list by biome type")
	@Comment({"Disallowed Biome Types for generation. Must match the Type identifer(s)."})
	public String[] rawBiomeTypeBlackList;
	
	@Ignore public List<Biome> whiteList = new ArrayList<>(5);
	@Ignore public List<Biome> blackList = new ArrayList<>(5);
	@Ignore public List<BiomeTypeHolder> typeWhiteList = new ArrayList<>(5);
	@Ignore public List<BiomeTypeHolder> typeBlackList = new ArrayList<>(5);
	
	/**
	 * 
	 */
	public BiomesConfig(String[] whiteList, String[] blackList, String[] typeWhiteList, String[] typeBlackList) {
		this.rawBiomeWhiteList = whiteList;
		this.rawBiomeBlackList = blackList;
		this.rawBiomeTypeWhiteList = typeWhiteList;
		this.rawBiomeTypeBlackList = typeBlackList;
	}
	
	/**
	 * 
	 */
	public void init() {
		this.whiteList = TreasureBiomeHelper.loadBiomesList(this.rawBiomeWhiteList);
		this.blackList = TreasureBiomeHelper.loadBiomesList(this.rawBiomeBlackList);
		BiomeHelper.loadBiomeList(this.rawBiomeTypeWhiteList, this.typeWhiteList);
		BiomeHelper.loadBiomeList(this.rawBiomeTypeBlackList, this.typeBlackList);	
	}
	
	@Override
	public List<Biome> getWhiteList() {
		return whiteList;
	}

	@Override
	public void setWhiteList(List<Biome> whiteList) {
		this.whiteList = whiteList;
	}
	
	@Override
	public List<Biome> getBlackList() {
		return blackList;
	}

	@Override
	public void setBlackList(List<Biome> blackList) {
		this.blackList = blackList;
	}

	@Override
	public List<BiomeTypeHolder> getTypeWhiteList() {
		return typeWhiteList;
	}

	@Override
	public void setTypeWhiteList(List<BiomeTypeHolder> whiteList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<BiomeTypeHolder> getTypeBlackList() {
		return typeBlackList;
	}

	@Override
	public void setTypeBlackList(List<BiomeTypeHolder> blackList) {
		// TODO Auto-generated method stub
		
	}
}
