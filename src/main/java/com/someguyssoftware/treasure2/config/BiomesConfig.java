/**
 * 
 */
package com.someguyssoftware.treasure2.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.someguyssoftware.gottschcore.biome.BiomeHelper;
import com.someguyssoftware.gottschcore.biome.BiomeTypeHolder;

import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

/**
 * @author Mark Gottschling on Nov 23, 2019
 *
 */
// TODO move to GottschCore 1.9/1.8
public class BiomesConfig implements IBiomesConfig {

	public static class Data {
		public String[] biomeWhiteList;
		public String[] biomeBlackList;
		public String[] biomeTypeWhiteList;
		public String[] biomeTypeBlackList;
		
		public Data(String[] whiteList, String[] blackList, String[] typeWhiteList, String[] typeBlackList) {
			this.biomeWhiteList = whiteList;
			this.biomeBlackList = blackList;
			this.biomeTypeWhiteList = typeWhiteList;
			this.biomeTypeBlackList = typeBlackList;
		}
	}
	
//	public List<Biome> whiteList = new ArrayList<>(5);
	// TODO should contain registry names
	public ConfigValue<List<? extends String>> whiteList;
	public ConfigValue<List<? extends String>> blackList;
	public ConfigValue<List<? extends String>> typeWhiteList;
	public ConfigValue<List<? extends String>> typeBlackList;
	
//	public List<Biome> blackList = new ArrayList<>(5);
	public List<BiomeTypeHolder> typeHolderWhiteList = new ArrayList<>(5);
	public List<BiomeTypeHolder> typeHolderBlackList = new ArrayList<>(5);
	
	/**
	 * 
	 * @param builder
	 */
	public BiomesConfig(Builder builder, Data data)	 {
		builder.comment(TreasureConfig.CATEGORY_DIV, " Biome white and black list properties", TreasureConfig.CATEGORY_DIV).push("biomes");
		
		whiteList = builder
				.comment(" Allowed Biomes for generation. Must match the Biome Registry Name(s). ex. minecraft:plains")
				.defineList("White list by biome name:", Arrays.asList(data.biomeWhiteList), s -> s instanceof String);
		
		blackList = builder
				.comment(" Disallowed Biomes for generation. Must match the Biome Registry Name(s). ex. minecraft:plains")
				.defineList("Black list by biome name:", Arrays.asList(data.biomeBlackList), s -> s instanceof String);
		
		typeWhiteList = builder
				.comment(" Allowabled Biome Types for generation. Must match the Type identifer(s).")
				.defineList("White list by biome name:", Arrays.asList(data.biomeTypeWhiteList), s -> s instanceof String);
		
		typeBlackList = builder
				.comment(" Disallowed Biome Types for generation. Must match the Type identifer(s).")
				.defineList("Black list by biome name:", Arrays.asList(data.biomeTypeBlackList), s -> s instanceof String);
		builder.pop();
	}
	
	/**
	 * 
	 */
	public void init() {
//		this.whiteList = TreasureBiomeHelper.loadBiomesList(this.rawBiomeWhiteList);
//		this.blackList = TreasureBiomeHelper.loadBiomesList(this.rawBiomeBlackList);
//		Treasure.LOGGER.debug("white list -> {}", this.whiteList);
//		Treasure.LOGGER.debug("black list -> {}", this.blackList);
//		BiomeHelper.loadBiomeList(this.whiteList.get(), this.typeHolderWhiteList);
//		BiomeHelper.loadBiomeList(this.blackList.get(), this.typeHolderBlackList);	
	}
	
//	@Override
//	public List<Biome> getWhiteList() {
//		return whiteList;
//	}
//
//	@Override
//	public void setWhiteList(List<Biome> whiteList) {
//		this.whiteList = whiteList;
//	}
	
//	@Override
//	public List<Biome> getBlackList() {
//		return blackList;
//	}
//
//	@Override
//	public void setBlackList(List<Biome> blackList) {
//		this.blackList = blackList;
//	}
//
//	@Override
//	public List<BiomeTypeHolder> getTypeWhiteList() {
//		return typeWhiteList;
//	}
//
//	@Override
//	public void setTypeWhiteList(List<BiomeTypeHolder> whiteList) {
//		this.typeWhiteList = whiteList;
//		
//	}
//
//	@Override
//	public List<BiomeTypeHolder> getTypeBlackList() {
//		return typeBlackList;
//	}
//
//	@Override
//	public void setTypeBlackList(List<BiomeTypeHolder> blackList) {
//		this.typeBlackList = blackList;		
//	}
}