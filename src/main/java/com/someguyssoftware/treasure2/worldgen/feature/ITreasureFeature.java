package com.someguyssoftware.treasure2.worldgen.feature;

import java.util.Map;

import com.someguyssoftware.treasure2.enums.Rarity;

public interface ITreasureFeature {

	public Map<String, Integer> getChunksSinceLastDimensionFeature();

	public Map<String, Map<Rarity, Integer>> getChunksSinceLastDimensionRarityFeature();
}
