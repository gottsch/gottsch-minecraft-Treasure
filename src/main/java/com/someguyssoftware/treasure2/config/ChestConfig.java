package com.someguyssoftware.treasure2.config;

import java.util.List;

import com.someguyssoftware.gottschcore.biome.BiomeTypeHolder;

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
		int minYSpawn;
		// TODO add depth range from spawn ie. common will be at depth of 6-20 blocks from "surface"
		double mimicProbability;
		boolean surfaceAllowed = true;
		boolean subterraneanAllowed = true;
		
		String[] whiteList;
		String[] blackList;
		String[] typeWhiteList;
		String[] typeBlackList;
		
		BiomesConfig.Data biomesData;
		
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
		public Data(boolean enableChest, int chunksPerChest, double genProbability, int minYSpawn, double mimicProbability, String[] whiteList, String[] blackList, String[] typeWhiteList, String[] typeBlackList) {
			this.enableChest = enableChest;
			this.chunksPerChest = chunksPerChest;
			this.genProbability = genProbability;
			this.minYSpawn = minYSpawn;
			this.mimicProbability = mimicProbability;
			this.biomesData = new BiomesConfig.Data(whiteList, blackList, typeWhiteList, typeBlackList);
		}

		@Override
		public String toString() {
			return "Data [enableChest=" + enableChest + ", chunksPerChest=" + chunksPerChest + ", genProbability="
					+ genProbability + ", minYSpawn=" + minYSpawn + ", mimicProbability=" + mimicProbability
					+ ", surfaceAllowed=" + surfaceAllowed + ", subterraneanAllowed=" + subterraneanAllowed + "]";
		}
	}
	
//	@RequiresWorldRestart
	public ForgeConfigSpec.ConfigValue<Boolean> enableChest;
	public ForgeConfigSpec.ConfigValue<Integer> chunksPerChest;
	public ForgeConfigSpec.ConfigValue<Double> genProbability;
	public ForgeConfigSpec.ConfigValue<Integer> minYSpawn;
	public ForgeConfigSpec.ConfigValue<Double> mimicProbability;
	
	// TODO most likely going to be removed with the use of meta files / archetype : type : biome categorizations
	@Deprecated
	public ForgeConfigSpec.ConfigValue<Boolean> surfaceAllowed;
	@Deprecated
	public ForgeConfigSpec.ConfigValue<Boolean> subterraneanAllowed;

	public BiomesConfig biomes; 

	/**
	 * 
	 * @param builder
	 * @param data
	 */
	public ChestConfig(Builder builder, Data data) {
		// build out properties
		builder.comment(TreasureConfig.CATEGORY_DIV, " Common chest properties", TreasureConfig.CATEGORY_DIV).push("01-Common chest");
		
		enableChest = builder
			.comment(" Enable/Disable generating chests associated with this rarity.")
			.define("Enable chests for rarity:", data.enableChest);
		
		chunksPerChest = builder
				.comment("The number of chunks generated before the chest spawn is attempted.")
				.defineInRange("Chunks per chest spawn:", data.chunksPerChest, 50, 32000	);

		genProbability= builder
				.comment("The probability that a chest will spawn.")
				.defineInRange("Probability of chest spawn:", data.genProbability, 0.0, 100.0);

		minYSpawn = builder
				.comment("The minimum depth (y-axis) that a chest can generate at.")
				.defineInRange("Minimum depth for spawn location:", data.minYSpawn, 5, 250);

		mimicProbability = builder
				.comment("The probability that a chest will be a mimic.")
				.defineInRange("Mimic probability:", data.mimicProbability, 0.0, 100.0);

		surfaceAllowed = builder
				.comment("")
				.define("Enable surface spawn:", data.surfaceAllowed);
		
		subterraneanAllowed = builder
				.comment("")
				.define("Enable subterranean spawn:", data.subterraneanAllowed);
		
		biomes = new BiomesConfig(builder, data.biomesData);
		
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
		return genProbability.get();
	}

	@Override
	public int getMinYSpawn() {
		return minYSpawn.get();
	}

	@Override
	public boolean isSurfaceAllowed() {
		return surfaceAllowed.get();
	}

	@Override
	public boolean isSubterraneanAllowed() {
		return subterraneanAllowed.get();
	}

	@Override
	public List<BiomeTypeHolder> getBiomeTypeWhiteList() {
//		return biomes.getTypeWhiteList();
		return null;
	}

	@Override
	public List<BiomeTypeHolder> getBiomeTypeBlackList() {
//		return biomes.getTypeBlackList();
		return null;
	}

	@Override
	public double getMimicProbability() {
		return mimicProbability.get();
	}

	@Override
	public List<String> getBiomeWhiteList() {
		return (List<String>) biomes.whiteList.get();
	}

	@Override
	public List<String> getBiomeBlackList() {
		return (List<String>) biomes.blackList.get();
	}
}