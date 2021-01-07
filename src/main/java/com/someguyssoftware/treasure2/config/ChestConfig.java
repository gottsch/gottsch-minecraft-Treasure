package com.someguyssoftware.treasure2.config;

import java.util.List;

import com.ibm.icu.util.ULocale.Category;
import com.someguyssoftware.gottschcore.biome.BiomeTypeHolder;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;

/**
 * 
 * @author Mark Gottschling on Jan 7, 2021
 *
 */
public class ChestConfig implements IChestConfig {
	/**
	 * 
	 * @author Mark Gottschling on Jan 7, 2021
	 *
	 */
	public static class Data {
		boolean enableChest;
		int chunksPerChest;
		double genProbability;
		int minYSpawn; // TODO refactor to depth range
		double mimicProbability;
		boolean surfaceAllowed = true;
		boolean subterraneanAllowed = true;
		
		/**
		 * 
		 * @param enableChest
		 * @param chunksPerChest
		 * @param genProbability
		 * @param minYSpawn
		 * @param whiteList
		 * @param blackList
		 * @param typeWhiteList
		 * @param typeBlackList
		 */
		public Data(boolean enableChest, int chunksPerChest, double genProbability, int minYSpawn, String[] whiteList, String[] blackList, String[] typeWhiteList, String[] typeBlackList) {
			this.enableChest = enableChest;
			this.chunksPerChest = chunksPerChest;
			this.genProbability = genProbability;
			this.minYSpawn = minYSpawn;
			// TODO create BiomeConfig and pass rest of values
		}

		@Override
		public String toString() {
			return "Data [enableChest=" + enableChest + ", chunksPerChest=" + chunksPerChest + ", genProbability="
					+ genProbability + ", minYSpawn=" + minYSpawn + ", mimicProbability=" + mimicProbability
					+ ", surfaceAllowed=" + surfaceAllowed + ", subterraneanAllowed=" + subterraneanAllowed + "]";
		}
	}
	
//	@RequiresWorldRestart
	public ForgeConfigSpec.ConfigValue<Boolean> enableChest;// = true;

//	@Comment({"The number of chunks generated before the chest spawn is attempted."})
//	@Name("01. Chunks per chest spawn:")
//	@RangeInt(min = 50, max = 32000)
	public ForgeConfigSpec.ConfigValue<Integer> chunksPerChest;// = 75;

//	@Comment({"The probability that a chest will spawn."})
//	@Name("02. Probability of chest spawn:")
//	@RangeDouble(min = 0.0, max = 100.0)
	public double genProbability = 50.0;

//	@Comment({"The minimum depth (y-axis) that a chest can generate at."})
//	@Name("03. Min. y-value for spawn location:")
//	@RangeInt(min = 5, max = 250)
	public int minYSpawn = 25;

//	@Comment({"The probability that a chest will be a mimic.", "NOTE: only common Wooden ChestConfig have mimics avaiable."})
//	@Name("04. Mimic probability:")
//	@RangeDouble(min = 0.0, max = 100.0)
	public double mimicProbability = 0.0;

	// TODO most likely going to be removed with the use of meta files / archetype : type : biome categorizations
//	@Name("05. Enable surface spawn:")
	public boolean surfaceAllowed = true;

//	@Name("06. Enable subterranean spawn:")
	public boolean subterraneanAllowed = true;

//	@Nam ment({"Biome white and black list properties."})
	public BiomesConfig biomes = new BiomesConfig(
			new String[] {},
			new String[] {"ocean", "deep_ocean", "deep_frozen_ocean", 
					"cold_ocean", "deep_cold_ocean", "lukewarm_ocean", "warm_ocean"},
			new String[] {},
			new String[] {"ocean", "deep_ocean"});

	/*
	 * 
	 */
//	public ChestConfig(final ForgeConfigSpec.Builder builder, boolean isAllowed, int chunksPer, double probability, int minYSpawn,
//			String[] whiteList, String[] blackList, String[] typeWhiteList, String[] typeBlackList) {
//		// build out properties
//		builder.comment().push("01-Common chest");
//		enableChest = builder
//		.comment("Enable/Disable generating chests associated with this rarity.")
//		.define("Enable chests for rarity:", true);
//		
//
//		
//		// update values
////		this.enableChest = isAllowed;
//		this.enableChest.set(isAllowed);
////		this.chunksPerChest = chunksPer;
//		this.chunksPerChest.set(chunksPer);
//		this.genProbability = probability;
//		this.minYSpawn = minYSpawn;
//		this.biomes = new BiomesConfig(whiteList, blackList, typeWhiteList, typeBlackList);
//		
//		builder.pop();
//	}

	/**
	 * 
	 * @param builder
	 */
//	public ChestConfig(final ForgeConfigSpec.Builder builder) {
//		builder.comment().push("01-Common chest");
//		enableChest = builder
//		.comment("Enable/Disable generating chests associated with this rarity.")
//		.define("Enable chests for rarity:", true);
//		
//		builder.pop();
//	}

	public ChestConfig(Builder builder, Data data) {
		// build out properties
		builder.comment(TreasureConfig.CATEGORY_DIV, " Common chest properties", TreasureConfig.CATEGORY_DIV).push("01-Common chest");
		enableChest = builder
		.comment(" Enable/Disable generating chests associated with this rarity.")
		.define("Enable chests for rarity:", data.enableChest);
		

		
		// update values
//		this.enableChest = isAllowed;
//		this.enableChest.set(isAllowed);
//		this.chunksPerChest = chunksPer;
//		this.chunksPerChest.set(chunksPer);
//		this.genProbability = probability;
//		this.minYSpawn = minYSpawn;
//		this.biomes = new BiomesConfig(whiteList, blackList, typeWhiteList, typeBlackList);
		
		builder.pop();
	}

	/**
	 * 
	 */
	public void init() {
		this.biomes.init();
	}
	
	@Override
	public boolean isEnableChest() {
		return enableChest.get();
	}

	@Override
	public int getChunksPerChest() {
		return chunksPerChest.get();
	}

	@Override
	public double getGenProbability() {
		return genProbability;
	}

	@Override
	public int getMinYSpawn() {
		return minYSpawn;
	}

	@Override
	public boolean isSurfaceAllowed() {
		return surfaceAllowed;
	}

	@Override
	public boolean isSubterraneanAllowed() {
		return subterraneanAllowed;
	}

	@Override
	public List<BiomeTypeHolder> getBiomeTypeWhiteList() {
		return biomes.getTypeWhiteList();
	}

	@Override
	public List<BiomeTypeHolder> getBiomeTypeBlackList() {
		return biomes.getTypeBlackList();
	}

	@Override
	public double getMimicProbability() {
		return mimicProbability;
	}

	@Override
	public List<Biome> getBiomeWhiteList() {
		return biomes.getWhiteList();
	}

	@Override
	public List<Biome> getBiomeBlackList() {
		return biomes.getBlackList();
	}
}