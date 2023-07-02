/*
 * This file is part of  Treasure2.
 * Copyright (c) 2019 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.biome;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Temporary class. TODO move to GottschCore.
 * TODO examine all biomes for 1.18 
 * @author Mark Gottschling on Nov 22, 2019
 *
 */
public class TreasureBiomeHelper {
//	public static List<String> biomeNames;
//	
//	// TODO is this even used?
//	static {
//		biomeNames = Arrays.asList(new String[] {
//				"ocean","deep_ocean","frozen_ocean","deep_frozen_ocean","cold_ocean","deep_cold_ocean",
//				"lukewarm_ocean","deep_lukewarm_ocean","warm_ocean","deep_warm_ocean",
//				"river","frozen_river","beach","stone_shore","snowy_beach","forest","wooded_hills","flower_forest",
//				"birch_forest","birch_forest_hills","tall_birch_forest","tall_birch_hills","dark_forest","dark_forest_hills",
//				"jungle","jungle_hills","modified_jungle","jungle_edge","modified_jungle_edge","bamboo_jungle",
//				"bamboo_jungle_hills","taiga","taiga_hills","taiga_mountains","snowy_taiga","snowy_taiga_hills",
//				"snowy_taiga_mountains","giant_tree_taiga","giant_tree_taiga_hills","giant_spruce_taiga",
//				"giant_spruce_taiga_hills","mushroom_fields","mushroom_field_shore","swamp","swamp_hills",
//				"savanna","savanna_plateau","shattered_savanna","shattered_savanna_plateau","plains",
//				"sunflower_plains","desert","desert_hills","desert_lakes","snowy_tundra","snowy_mountains",
//				"ice_spikes","mountains","wooded_mountains","gravelly_mountains","modified_gravelly_mountains",
//				"mountain_edge","badlands","badlands_plateau","modified_badlands_plateau","wooded_badlands_plateau",
//				"modified_wooded_badlands_plateau","eroded_badlands","nether","the_end","small_end_islands",
//				"end_midlands","end_highlands","end_barrens","the_void"
//		});
//	}
	
	public enum Result {
		OK,
		WHITE_LISTED,
		BLACK_LISTED
	};
	
	/**
	 * 
	 * @param biomes
	 * @return
	 */
	public static List<Biome> loadBiomesList(String[] biomes) {
		List<Biome> list = new ArrayList<>();
		if (biomes != null) {
			for (String biomeName : biomes) {
				Biome biome = ForgeRegistries.BIOMES.getValue(new ResourceLocation(biomeName));
				if (!list.contains(biome)) {
					list.add(biome);
				}
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
	public static Result isBiomeAllowed(ResourceKey<Biome> biome, List<String> whiteList, List<String> blackList) {
		return isBiomeAllowed(biome.location(), whiteList, blackList);
	}
	
	/**
	 * 
	 * @param biome
	 * @param whiteList
	 * @param blackList
	 * @return
	 */
	public static Result isBiomeAllowed(ResourceLocation biome, List<String> whiteList, List<String> blackList) {
        if (whiteList != null && whiteList.size() > 0) {
        	for (String biomeName : whiteList) {
	        	if (biomeName.equals(biome.toString())) {
	        		return Result.WHITE_LISTED;
	        	}
	        }
        	// added in 1.15. If white list has values and biome is not in it, then by definition, it is black listed.
        	return Result.BLACK_LISTED;
        }
        
        if (blackList != null && blackList.size() > 0) {
        	for (String biomeName : blackList) {
        		if (biomeName.equals(biome.toString())) {
        			return Result.BLACK_LISTED;
        		}
        	}
        }
        
    	// neither white list nor black list have values = all biomes are valid
    	return Result.OK;
	}
	
/*
 Ocean	ocean	0
Deep Ocean	deep_ocean	24
Frozen Ocean	frozen_ocean	10
Deep Frozen Ocean	deep_frozen_ocean	50
Cold Ocean	cold_ocean	46
Deep Cold Ocean	deep_cold_ocean	49
Lukewarm Ocean	lukewarm_ocean	45
Deep Lukewarm Ocean	deep_lukewarm_ocean	48
Warm Ocean	warm_ocean	44
Deep Warm Ocean	deep_warm_ocean	47
River	river	7
Frozen River	frozen_river	11
Beach	beach	16
Stone Shore	stone_shore	25
Snowy Beach	snowy_beach	26
Forest	forest	4
Wooded Hills	wooded_hills	18
Flower Forest	flower_forest	132
Birch Forest	birch_forest	27
Birch Forest Hills	birch_forest_hills	28
Tall Birch Forest	tall_birch_forest	155
Tall Birch Hills	tall_birch_hills	156
Dark Forest	dark_forest	29
Dark Forest Hills	dark_forest_hills	157
Jungle	jungle	21
Jungle Hills	jungle_hills	22
Modified Jungle	modified_jungle	149
Jungle Edge	jungle_edge	23
Modified Jungle Edge	modified_jungle_edge	151
Bamboo Jungle	bamboo_jungle	168
Bamboo Jungle Hills	bamboo_jungle_hills	169
Taiga	taiga	5
Taiga Hills	taiga_hills	19
Taiga Mountains	taiga_mountains	133
Snowy Taiga	snowy_taiga	30
Snowy Taiga Hills	snowy_taiga_hills	31
Snowy Taiga Mountains	snowy_taiga_mountains	158
Giant Tree Taiga	giant_tree_taiga	32
Giant Tree Taiga Hills	giant_tree_taiga_hills	33
Giant Spruce Taiga	giant_spruce_taiga	160
Giant Spruce Taiga Hills	giant_spruce_taiga_hills	161
Mushroom Fields	mushroom_fields	14
Mushroom Field Shore	mushroom_field_shore	15
Swamp	swamp	6
Swamp Hills	swamp_hills	134
Savanna	savanna	35
Savanna Plateau	savanna_plateau	36
Shattered Savanna	shattered_savanna	163
Shattered Savanna Plateau	shattered_savanna_plateau	164
Plains	plains	1
Sunflower Plains	sunflower_plains	129
Desert	desert	2
Desert Hills	desert_hills	17
Desert Lakes	desert_lakes	130
Snowy Tundra	snowy_tundra	12
Snowy Mountains	snowy_mountains	13
Ice Spikes	ice_spikes	140
Mountains	mountains	3
Wooded Mountains	wooded_mountains	34
Gravelly Mountains	gravelly_mountains	131
Gravelly Mountains+	modified_gravelly_mountains	162
Mountain Edge	mountain_edge	20
Badlands	badlands	37
Badlands Plateau	badlands_plateau	39
Modified Badlands Plateau	modified_badlands_plateau	167
Wooded Badlands Plateau	wooded_badlands_plateau	38
Modified Wooded Badlands Plateau	modified_wooded_badlands_plateau	166
Eroded Badlands	eroded_badlands	165
Nether	nether	8
The End	the_end	9
Small End Islands	small_end_islands	40
End Midlands	end_midlands	41
End Highlands	end_highlands	42
End Barrens	end_barrens	43
The Void	the_void	127
*/
}