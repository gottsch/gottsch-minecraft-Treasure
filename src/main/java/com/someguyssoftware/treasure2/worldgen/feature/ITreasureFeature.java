package com.someguyssoftware.treasure2.worldgen.feature;

import java.util.Map;

import com.someguyssoftware.treasure2.enums.Rarity;

public interface ITreasureFeature {

	public Map<String, Integer> getChunksSinceLastDimensionFeature();

	public Map<String, Map<Rarity, Integer>> getChunksSinceLastDimensionRarityFeature();

    default public boolean checkDimensionWhiteList(String dimensionName) {
        // test the dimension white list
        if (!TreasureConfig.GENERAL.dimensionsWhiteList.get().contains(dimensionName)) {
            return false;
        }
        return true;
    }

    default public boolean checkOceanBiomes(ICoords coords) {
        Biome biome = world.getBiome(coords.toPos());
		if (biome == Biomes.OCEAN || biome == Biomes.DEEP_OCEAN || biome == Biomes.FROZEN_OCEAN ||
				BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN)) {
			return false;
		}
    }
}
