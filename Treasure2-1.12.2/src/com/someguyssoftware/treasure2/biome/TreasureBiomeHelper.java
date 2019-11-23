/**
 * 
 */
package com.someguyssoftware.treasure2.biome;

import java.util.ArrayList;
import java.util.List;

import com.someguyssoftware.gottschcore.biome.BiomeTypeHolder;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import scala.actors.threadpool.Arrays;

/**
 * Temporary class. TODO move to GottschCore.
 * @author Mark Gottschling on Nov 22, 2019
 *
 */
@SuppressWarnings("unchecked")
public class TreasureBiomeHelper {
	public static List<String> biomeNames;
	
	static {
		biomeNames = Arrays.asList(new String[] {
				"ocean","deep_ocean","frozen_ocean","deep_frozen_ocean","cold_ocean","deep_cold_ocean",
				"lukewarm_ocean","deep_lukewarm_ocean","warm_ocean","deep_warm_ocean",
				"river","frozen_river","beach","stone_shore","snowy_beach","forest","wooded_hills","flower_forest",
				"birch_forest","birch_forest_hills","tall_birch_forest","tall_birch_hills","dark_forest","dark_forest_hills",
				"jungle","jungle_hills","modified_jungle","jungle_edge","modified_jungle_edge","bamboo_jungle",
				"bamboo_jungle_hills","taiga","taiga_hills","taiga_mountains","snowy_taiga","snowy_taiga_hills",
				"snowy_taiga_mountains","giant_tree_taiga","giant_tree_taiga_hills","giant_spruce_taiga",
				"giant_spruce_taiga_hills","mushroom_fields","mushroom_field_shore","swamp","swamp_hills",
				"savanna","savanna_plateau","shattered_savanna","shattered_savanna_plateau","plains",
				"sunflower_plains","desert","desert_hills","desert_lakes","snowy_tundra","snowy_mountains",
				"ice_spikes","mountains","wooded_mountains","gravelly_mountains","modified_gravelly_mountains",
				"mountain_edge","badlands","badlands_plateau","modified_badlands_plateau","wooded_badlands_plateau",
				"modified_wooded_badlands_plateau","eroded_badlands","nether","the_end","small_end_islands",
				"end_midlands","end_highlands","end_barrens","the_void"
		});
	}
	
	/**
	 * 
	 * @param biomes
	 * @return
	 */
	public static List<Biome> loadBiomesList(String[] biomes) {
		List<Biome> list = new ArrayList<>();
		for (String biomeName : biomes) {
			Biome biome = ForgeRegistries.BIOMES.getValue(new ResourceLocation(biomeName));
			if (!list.contains(biome)) {
				list.add(biome);
			}
		}
		return list;
	}

	/**
	 * 
	 * @param biome
	 * @param whiteList
	 * @param blackList
	 * @return
	 */
	public static boolean isBiomeAllowed(Biome biome, List<Biome> whiteList, List<Biome> blackList) {
        if (whiteList != null && whiteList.size() > 0) {
        	if (whiteList.contains(biome)) {
        		return true;
        	}
        }
        
        if (blackList != null && blackList.size() > 0) {
        	if (blackList.contains(biome)) {
        		return false;
        	}
        }
        
    	// neither white list nor black list have values = all biomes are valid
    	return true;
	}
}
