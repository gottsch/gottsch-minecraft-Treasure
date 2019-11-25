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
public class BiomesConfig implements IBiomesConfig {

	@Name("01. White list by biome name:")
	@Comment({"Allowed Biomes for Wither Tree generation. Must match the Biome Name(s)."})
	public String[] rawBiomeWhiteList;// = new String[] {};	
	@Name("02. Black list by biome name:")
	@Comment({"Disallowed Biomes for Wither Tree generation. Must match theBiome Name(s)."})
	public String[] rawBiomeBlackList;// = new String[] {};
	@Name("03. White list by biome type:")
	@Comment({"Allowabled Biome Types for Wither Tree generation. Must match the Type identifer(s)."})
	public String[] rawBiomeTypeWhiteList;// = new String[] {}; //{"forest", "magical", "lush", "spooky", "dead", "jungle", "coniferous", "savanna"};
	@Name("04. Black list by biome type")
	@Comment({"Disallowed Biome Types for Wither Tree generation. Must match the Type identifer(s)."})
	public String[] rawBiomeTypeBlackList;// = new String[] {};
	
	@Ignore public List<Biome> whiteList = new ArrayList<>(5);;
	@Ignore public List<Biome> blackList = new ArrayList<>(5);;
	@Ignore public List<BiomeTypeHolder> typeWhiteList = new ArrayList<>(5);;
	@Ignore public List<BiomeTypeHolder> typeBlackList = new ArrayList<>(5);;
	
	/**
	 * 
	 */
	public BiomesConfig(String[] whiteList, String[] blackList, String[] typeWhiteList, String[] typeBlackList) {
		this.rawBiomeWhiteList = whiteList;
		this.rawBiomeBlackList = blackList;
		this.rawBiomeTypeWhiteList = typeWhiteList;
		this.rawBiomeTypeBlackList = typeBlackList;
		
		this.whiteList = TreasureBiomeHelper.loadBiomesList(whiteList);
		this.blackList = TreasureBiomeHelper.loadBiomesList(blackList);
		BiomeHelper.loadBiomeList(typeWhiteList, this.typeWhiteList);
		BiomeHelper.loadBiomeList(typeBlackList, this.typeBlackList);
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
