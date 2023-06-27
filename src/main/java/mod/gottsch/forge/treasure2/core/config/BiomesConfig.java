/**
 * 
 */
package mod.gottsch.forge.treasure2.core.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mod.gottsch.forge.gottschcore.biome.BiomeTypeHolder;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

/**
 * @author Mark Gottschling on Nov 23, 2019
 *
 */
// TODO move to GottschCore 1.9/1.8
public class BiomesConfig {

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
		builder.comment(Config.CATEGORY_DIV, " Biome white and black list properties", Config.CATEGORY_DIV)
		.push("biomes");
		
		whiteList = builder
				.comment(" Allowed Biomes for generation. Must match the Biome Registry Name(s). ex. minecraft:plains")
				.defineList("whitelist", Arrays.asList(data.biomeWhiteList), s -> s instanceof String);
		
		blackList = builder
				.comment(" Disallowed Biomes for generation. Must match the Biome Registry Name(s). ex. minecraft:plains")
				.defineList("blacklist", Arrays.asList(data.biomeBlackList), s -> s instanceof String);
		
		typeWhiteList = builder
				.comment(" Allowabled Biome Types for generation. Must match the Type identifer(s).")
				.defineList("whitelistType", Arrays.asList(data.biomeTypeWhiteList), s -> s instanceof String);
		
		typeBlackList = builder
				.comment(" Disallowed Biome Types for generation. Must match the Type identifer(s).")
				.defineList("blacklistType", Arrays.asList(data.biomeTypeBlackList), s -> s instanceof String);
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

	@Override
	public String toString() {
		return "BiomesConfig [whiteList=" + whiteList.get() + ", blackList=" + blackList + ", typeWhiteList=" + typeWhiteList.get()
				+ ", typeBlackList=" + typeBlackList.get() + ", typeHolderWhiteList=" + typeHolderWhiteList
				+ ", typeHolderBlackList=" + typeHolderBlackList + "]";
	}
}