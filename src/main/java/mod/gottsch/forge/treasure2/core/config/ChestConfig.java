/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
 * 
 * All rights reserved.
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.forge.treasure2.core.config;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;

import com.someguyssoftware.gottschcore.biome.BiomeTypeHolder;

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
		int minDepth;
		int maxDepth;
		double mimicProbability;
		boolean surfaceAllowed = true;
		boolean subterraneanAllowed = true;
		int weight;
		
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
		public Data(boolean enableChest, int weight, int minDepth, int maxDepth, double mimicProbability, String[] whiteList, String[] blackList, String[] typeWhiteList, String[] typeBlackList) {
			this.enableChest = enableChest;
			this.weight = weight;
			this.minDepth = minDepth;
			this.maxDepth = maxDepth;
			this.mimicProbability = mimicProbability;
			this.biomesData = new BiomesConfig.Data(whiteList, blackList, typeWhiteList, typeBlackList);
		}

		@Override
		public String toString() {
			return "Data [enableChest=" + enableChest + ", minDepth=" + minDepth + ", maxDepth=" + maxDepth
					+ ", mimicProbability=" + mimicProbability + ", surfaceAllowed=" + surfaceAllowed
					+ ", subterraneanAllowed=" + subterraneanAllowed + ", weight=" + weight + ", whiteList="
					+ Arrays.toString(whiteList) + ", blackList=" + Arrays.toString(blackList) + ", typeWhiteList="
					+ Arrays.toString(typeWhiteList) + ", typeBlackList=" + Arrays.toString(typeBlackList)
					+ ", biomesData=" + biomesData + "]";
		}
	}
	
//	@RequiresWorldRestart
	public ForgeConfigSpec.ConfigValue<Boolean> enableChest;
	public ForgeConfigSpec.ConfigValue<Integer> weight;
	public ForgeConfigSpec.ConfigValue<Integer> minDepth;
	public ForgeConfigSpec.ConfigValue<Integer> maxDepth;
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
	public ChestConfig(Builder builder, String category, Data data) {
		// build out properties
		builder.comment(TreasureConfig.CATEGORY_DIV, " " + WordUtils.capitalizeFully(category) + " chest properties", TreasureConfig.CATEGORY_DIV).push(category);
		
		enableChest = builder
			.comment(" Enable/Disable generating chests associated with this rarity.")
			.define("Enable chests for rarity:", data.enableChest);
		
		weight = builder
				.comment("The weight for this rarity to spawn a chest.", "Higher number relative to other weight means this has a high chance to be selected.")
				.defineInRange("Weight of rarity:", data.weight, 0, 32000);

		minDepth = builder
				.comment("The minimum blocks deep from the surface that a chest can generate at.")
				.defineInRange("Minimum depth for spawn location:", data.minDepth, 5, 250);
		
		maxDepth = builder
				.comment("The maximum blocks deep from the surface that a chest can generate at.")
				.defineInRange("Maximum depth for spawn location:", data.maxDepth, 5, 250);
		
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
	@Deprecated
	public int getMinYSpawn() {
//		return minYSpawn.get();
		return 5;
	}

	@Override
	public int getMinDepth() {
		return minDepth.get();
	}
	
	@Override
	public int getMaxDepth() {
		return maxDepth.get();
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
	public List<String> getBiomeWhitelist() {
		return (List<String>) biomes.whiteList.get();
	}

	@Override
	public List<String> getBiomeBlacklist() {
		return (List<String>) biomes.blackList.get();
	}

	@Override
	public String toString() {
		return "ChestConfig [enableChest=" + enableChest + ", weight=" + weight + ", minDepth=" + minDepth
				+ ", maxDepth=" + maxDepth + ", mimicProbability=" + mimicProbability + ", surfaceAllowed="
				+ surfaceAllowed + ", subterraneanAllowed=" + subterraneanAllowed + ", biomes=" + biomes + "]";
	}

	@Override
	public int getWeight() {
		return weight.get();
	}

}