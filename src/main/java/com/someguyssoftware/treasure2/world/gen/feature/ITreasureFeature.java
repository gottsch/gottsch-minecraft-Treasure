package com.someguyssoftware.treasure2.world.gen.feature;

import java.util.List;
import java.util.Map;

import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.chest.ChestInfo;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.data.TreasureData;
import com.someguyssoftware.treasure2.enums.Rarity;

import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public interface ITreasureFeature {

    /**
     * 
     * @return
     */
//	public boolean isEnabled();
	
	public void init();
	
	public Map<String, Integer> getChunksSinceLastDimensionFeature();

	public Map<String, Map<Rarity, Integer>> getChunksSinceLastDimensionRarityFeature();
	
    default public boolean checkDimensionWhiteList(String dimensionName) {
        // test the dimension white list
        if (!TreasureConfig.GENERAL.dimensionsWhiteList.get().contains(dimensionName)) {
            return false;
        }
        return true;
    }

    // TODO move to BiomeHelper
    default public boolean checkOceanBiomes(Biome biome) {
//		if (biome == Biomes.OCEAN || biome == Biomes.DEEP_OCEAN || biome == Biomes.FROZEN_OCEAN ||
//				BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN)) {
//			return true;
//		}
		return false;
    }
    
	/**
	 * 
	 * @param world
	 * @param pos
	 * @param minDistance
	 * @return
	 */
	public static boolean isRegisteredChestWithinDistance(IWorld world, ICoords coords, int minDistance) {

		double minDistanceSq = minDistance * minDistance;

		// get a list of dungeons
		List<ChestInfo> infos = TreasureData.CHEST_REGISTRIES.get(WorldInfo.getDimension((World)world).toString()).getValues();

		if (infos == null || infos.size() == 0) {
			Treasure.LOGGER.debug("Unable to locate the ChestConfig Registry or the Registry doesn't contain any values");
			return false;
		}

		for (ChestInfo info : infos) {
			// calculate the distance to the poi
			double distance = coords.getDistanceSq(info.getCoords());
			if (distance < minDistanceSq) {
				return true;
			}
		}
		return false;
	}
}
