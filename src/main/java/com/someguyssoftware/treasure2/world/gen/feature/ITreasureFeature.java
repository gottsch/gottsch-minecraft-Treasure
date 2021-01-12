package com.someguyssoftware.treasure2.world.gen.feature;

import java.util.Map;

import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.Rarity;

import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.common.BiomeDictionary;

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

    default public boolean checkOceanBiomes(Biome biome) {
		if (biome == Biomes.OCEAN || biome == Biomes.DEEP_OCEAN || biome == Biomes.FROZEN_OCEAN ||
				BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN)) {
			return true;
		}
		return false;
    }
}
