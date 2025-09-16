/*
 * This file is part of  Treasure2.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.datagen;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.tags.TreasureTags;
import mod.gottsch.forge.treasure2.datagen.tags.BiomesWeveGone;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.data.ExistingFileHelper;

/**
 * 
 * @author Mark Gottschling on May 20, 2023
 *
 */
public class TreasureBiomeTagsProvider extends BiomeTagsProvider {

	public TreasureBiomeTagsProvider(PackOutput output, CompletableFuture<Provider> lookup, ExistingFileHelper existingFileHelper) {
        super(output, lookup, Treasure.MODID, existingFileHelper);
    }
    
    @Override
    protected void addTags(Provider provider) {
        String BOP = "biomesoplenty";
        String BWG = "biomeswevegone";

        // setup chest rarity biome filters
        ResourceKey[] filtered = new ResourceKey[]{Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS};

        tag(TreasureTags.Biomes.TERRANEAN_RARE_BIOME_FILTER).add(filtered);

    	// blocks rarity
//    	tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.BADLANDS);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.BAMBOO_JUNGLE);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.BASALT_DELTAS);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.BIRCH_FOREST);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.BEACH);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.CHERRY_GROVE);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.CRIMSON_FOREST);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.DARK_FOREST);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.DESERT);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.DEEP_DARK);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.DRIPSTONE_CAVES);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.FOREST);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.FLOWER_FOREST);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.FROZEN_PEAKS);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.FROZEN_RIVER);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.GROVE);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.ICE_SPIKES);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.JUNGLE);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.JAGGED_PEAKS);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.LUSH_CAVES);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.MANGROVE_SWAMP);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.MEADOW);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.MUSHROOM_FIELDS);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.OLD_GROWTH_BIRCH_FOREST);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.OLD_GROWTH_PINE_TAIGA);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.OLD_GROWTH_SPRUCE_TAIGA);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.PLAINS);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.RIVER);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.SAVANNA);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.SAVANNA_PLATEAU);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.SNOWY_PLAINS);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.SNOWY_SLOPES);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.SNOWY_TAIGA);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.SPARSE_JUNGLE);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.STONY_PEAKS);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.SUNFLOWER_PLAINS);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.SWAMP);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.TAIGA);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.WINDSWEPT_FOREST);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.WINDSWEPT_SAVANNA);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.WINDSWEPT_HILLS);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.WINDSWEPT_GRAVELLY_HILLS);
//        tag(TreasureTags.Biomes.ALL_OVERWORLD).add(Biomes.WOODED_BADLANDS);

        // ////// treasure2 / vanilla tags ////////////////////////////////////////////////
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addTag(BiomeTags.IS_OVERWORLD);

        tag(TreasureTags.Biomes.TEMPERATE).add(Biomes.BIRCH_FOREST);
        tag(TreasureTags.Biomes.TEMPERATE).add(Biomes.CHERRY_GROVE);
        tag(TreasureTags.Biomes.TEMPERATE).add(Biomes.DARK_FOREST);
        tag(TreasureTags.Biomes.TEMPERATE).add(Biomes.FOREST);
        tag(TreasureTags.Biomes.TEMPERATE).add(Biomes.FLOWER_FOREST);
        tag(TreasureTags.Biomes.TEMPERATE).add(Biomes.GROVE);
        tag(TreasureTags.Biomes.TEMPERATE).add(Biomes.MEADOW);
        tag(TreasureTags.Biomes.TEMPERATE).add(Biomes.MUSHROOM_FIELDS);
        tag(TreasureTags.Biomes.TEMPERATE).add(Biomes.OLD_GROWTH_BIRCH_FOREST);
        tag(TreasureTags.Biomes.TEMPERATE).add(Biomes.OLD_GROWTH_PINE_TAIGA);
        tag(TreasureTags.Biomes.TEMPERATE).add(Biomes.OLD_GROWTH_SPRUCE_TAIGA);
        tag(TreasureTags.Biomes.TEMPERATE).add(Biomes.PLAINS);
        tag(TreasureTags.Biomes.TEMPERATE).add(Biomes.SAVANNA);
        tag(TreasureTags.Biomes.TEMPERATE).add(Biomes.SAVANNA_PLATEAU);
        tag(TreasureTags.Biomes.TEMPERATE).add(Biomes.SUNFLOWER_PLAINS);
        tag(TreasureTags.Biomes.TEMPERATE).add(Biomes.WINDSWEPT_FOREST);
        tag(TreasureTags.Biomes.TEMPERATE).add(Biomes.WINDSWEPT_HILLS);

        tag(TreasureTags.Biomes.WELLS_GENERAL).addTag(TreasureTags.Biomes.TEMPERATE);
        tag(TreasureTags.Biomes.WELLS_GENERAL).add(Biomes.BADLANDS);
        tag(TreasureTags.Biomes.WELLS_GENERAL).add(Biomes.BAMBOO_JUNGLE);
        tag(TreasureTags.Biomes.WELLS_GENERAL).add(Biomes.BASALT_DELTAS);
        tag(TreasureTags.Biomes.WELLS_GENERAL).add(Biomes.FROZEN_PEAKS);
        tag(TreasureTags.Biomes.WELLS_GENERAL).add(Biomes.JUNGLE);
        tag(TreasureTags.Biomes.WELLS_GENERAL).add(Biomes.JAGGED_PEAKS);
        tag(TreasureTags.Biomes.WELLS_GENERAL).add(Biomes.MANGROVE_SWAMP);
        tag(TreasureTags.Biomes.WELLS_GENERAL).add(Biomes.SNOWY_PLAINS);
        tag(TreasureTags.Biomes.WELLS_GENERAL).add(Biomes.SNOWY_SLOPES);
        tag(TreasureTags.Biomes.WELLS_GENERAL).add(Biomes.SNOWY_TAIGA);
        tag(TreasureTags.Biomes.WELLS_GENERAL).add(Biomes.SPARSE_JUNGLE);
        tag(TreasureTags.Biomes.WELLS_GENERAL).add(Biomes.STONY_PEAKS);
        tag(TreasureTags.Biomes.WELLS_GENERAL).add(Biomes.SWAMP);
        tag(TreasureTags.Biomes.WELLS_GENERAL).add(Biomes.TAIGA);
        tag(TreasureTags.Biomes.WELLS_GENERAL).add(Biomes.WINDSWEPT_SAVANNA);
        tag(TreasureTags.Biomes.WELLS_GENERAL).add(Biomes.WINDSWEPT_GRAVELLY_HILLS);
        tag(TreasureTags.Biomes.WELLS_GENERAL).add(Biomes.WOODED_BADLANDS);

        // forest wells
        tag(TreasureTags.Biomes.WELLS_FOREST).add(Biomes.BIRCH_FOREST);
        tag(TreasureTags.Biomes.WELLS_FOREST).add(Biomes.CHERRY_GROVE);
        tag(TreasureTags.Biomes.WELLS_FOREST).add(Biomes.DARK_FOREST);
        tag(TreasureTags.Biomes.WELLS_FOREST).add(Biomes.FOREST);
        tag(TreasureTags.Biomes.WELLS_FOREST).add(Biomes.GROVE);
        tag(TreasureTags.Biomes.WELLS_FOREST).add(Biomes.OLD_GROWTH_BIRCH_FOREST);
        tag(TreasureTags.Biomes.WELLS_FOREST).add(Biomes.OLD_GROWTH_PINE_TAIGA);
        tag(TreasureTags.Biomes.WELLS_FOREST).add(Biomes.OLD_GROWTH_SPRUCE_TAIGA);
        tag(TreasureTags.Biomes.WELLS_FOREST).add(Biomes.WINDSWEPT_FOREST);

        // jungle wells
        tag(TreasureTags.Biomes.WELLS_JUNGLE).add(Biomes.BAMBOO_JUNGLE);
        tag(TreasureTags.Biomes.WELLS_JUNGLE).add(Biomes.JUNGLE);
        tag(TreasureTags.Biomes.WELLS_JUNGLE).add(Biomes.SPARSE_JUNGLE);

        // desert wells
        tag(TreasureTags.Biomes.WELLS_DESERT).add(Biomes.DESERT);

        // ///// INTEGRATIONS /////////////////////////////////
        // BWG
        tag(TreasureTags.Biomes.BWG_FOREST).addOptional(BiomesWeveGone.ASPEN_BOREAL);
        tag(TreasureTags.Biomes.BWG_FOREST).addOptional(BiomesWeveGone.BLACK_FOREST);
        tag(TreasureTags.Biomes.BWG_FOREST).addOptional(BiomesWeveGone.CANADIAN_SHIELD);
        tag(TreasureTags.Biomes.BWG_FOREST).addOptional(BiomesWeveGone.CIKA_WOODS);
        tag(TreasureTags.Biomes.BWG_FOREST).addOptional(BiomesWeveGone.CONIFEROUS_FOREST);
        tag(TreasureTags.Biomes.BWG_FOREST).addOptional(BiomesWeveGone.EBONY_WOODS);
        tag(TreasureTags.Biomes.BWG_FOREST).addOptional(BiomesWeveGone.FORGOTTON_FOREST);
        tag(TreasureTags.Biomes.BWG_FOREST).addOptional(BiomesWeveGone.FROSTED_CONIFEROUS_FOREST);
        tag(TreasureTags.Biomes.BWG_FOREST).addOptional(BiomesWeveGone.OVERGROWTH_WOODLANDS);
        tag(TreasureTags.Biomes.BWG_FOREST).addOptional(BiomesWeveGone.REDWOOD_THICKET);
        tag(TreasureTags.Biomes.BWG_FOREST).addOptional(BiomesWeveGone.WEEPING_WITCH_FOREST);
        tag(TreasureTags.Biomes.BWG_FOREST).addOptional(BiomesWeveGone.ZELKOVA_FOREST);

        tag(TreasureTags.Biomes.BWG_JUNGLE).addOptional(BiomesWeveGone.JACARANDA_JUNGLE);
        tag(TreasureTags.Biomes.BWG_JUNGLE).addOptional(BiomesWeveGone.FRAGMENT_JUNGLE);
        tag(TreasureTags.Biomes.BWG_JUNGLE).addOptional(BiomesWeveGone.TROPICAL_RAINFOREST);

        tag(TreasureTags.Biomes.BWG_DESERT).addOptional(BiomesWeveGone.MOJAVE_DESERT);
        tag(TreasureTags.Biomes.BWG_DESERT).addOptional(BiomesWeveGone.WINDSWEPT_DESERT);

        tag(TreasureTags.Biomes.BWG_IS_DRY).addOptionalTag(TreasureTags.Biomes.BWG_DESERT);
        tag(TreasureTags.Biomes.BWG_IS_DRY).addOptional(BiomesWeveGone.SIERRA_BADLANDS);

        // TODO setup BOP Tags

        // temperate integrations
        tag(TreasureTags.Biomes.TEMPERATE).addOptionalTag(TreasureTags.Biomes.BWG_FOREST);

        // well integrations
        // NOTE not full list - most watery biomes removed.
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "aspen_glade"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "auroral_garden"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "clover_patch"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "cold_desert"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "coniferous_forest"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "crag"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "crystalline_chasm"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "dead_forest"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "dryland"));

        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "dune_beach"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "field"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "fir_clearing"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "floodplain"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "forested_field"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP,  "fungal_jungle"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP,  "glowing_grotto"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "grassland"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "highland"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "hot_springs"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "jacaranda_glade"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "jade_cliffs"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "lavender_field"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "lush_desert"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "lush_savanna"));

        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "maple_woods"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP,  "mediterranean_forest"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "moor"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "muskeg"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "mystic_grove"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "old_growth_dead_forest"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "old_growth_woodland"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "ominous_woods"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "orchard"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "origin_valley"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "pasture"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "prairie"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "pumpkin_patch"));

        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "rainforest"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "redwood_forest"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "rocky_rainforest"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "rocky_shrubland"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "scrubland"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "seasonal_forest"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "shrubland"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP,  "snowblossom_grove"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "snowy_coniferous_forest"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "snowy_fir_clearing"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "snowy_maple_woods"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "tropics"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "tundra"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "undergrowth"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "visceral_heap"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "volcano"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "volcanic_plains"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "wasteland"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "wasteland_steppe"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "wetland"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "wintry_origin_valley"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "withered_abyss"));
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(new ResourceLocation(BOP, "woodland"));

        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptionalTag(TreasureTags.Biomes.BWG_FOREST);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptionalTag(TreasureTags.Biomes.BWG_JUNGLE);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesWeveGone.ALLIUM_SHRUBLAND);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesWeveGone.AMARANTH_GRASSLAND);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesWeveGone.ATACAMA_OUTBACK);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesWeveGone.ARAUCARIA_SAVANNA);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesWeveGone.BAOBAB_SAVANNA);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesWeveGone.BASALT_BARRERA);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesWeveGone.COCONINO_MEADOW);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesWeveGone.CRAG_GARDENS);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesWeveGone.CRIMSON_TUNDRA);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesWeveGone.DACITE_RIDGES);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesWeveGone.ENCHANTED_TANGLE);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesWeveGone.FIRECRACKER_CHAPARRAL);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesWeveGone.FROSTED_TAIGA);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesWeveGone.HOWLING_PEAKS);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesWeveGone.IRONWOOD_GOUR);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesWeveGone.LUSH_STACKS);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesWeveGone.MAPLE_TAIGA);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesWeveGone.ORCHARD);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesWeveGone.PRAIRIE);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesWeveGone.PUMPKIN_VALLEY);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesWeveGone.RED_ROCK_PEAKS);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesWeveGone.RED_ROCK_VALLEY);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesWeveGone.ROSE_FIELDS);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesWeveGone.RUGGED_BADLANDS);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesWeveGone.SAKURA_GROVE);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesWeveGone.SIERRA_BADLANDS);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesWeveGone.SKYRISE_VALE);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesWeveGone.TEMPERATE_GROVE);

        // forest well integrations
        tag(TreasureTags.Biomes.WELLS_FOREST).addOptionalTag(TreasureTags.Biomes.BWG_FOREST);
        tag(TreasureTags.Biomes.WELLS_FOREST).addOptionalTag(TreasureTags.Biomes.BWG_JUNGLE);
        // desert well integrations
        tag(TreasureTags.Biomes.WELLS_DESERT).addOptionalTag(TreasureTags.Biomes.BWG_DESERT);
        tag(TreasureTags.Biomes.WELLS_DESERT).addOptional(new ResourceLocation(BOP, "lush_desert"));


        // TODO FINISH

        // Twilight Forest
        // TODO ...
        
        // BOP
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "aspen_glade"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "auroral_garden"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "bayou"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "bog"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "clover_patch"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "cold_desert"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "coniferous_forest"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "crag"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "crystalline_chasm"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "dead_forest"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "dryland"));
        
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "dune_beach"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "end_wilds"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "end_reef"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "end_corruption"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "erupting_inferno"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "field"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "fir_clearing"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "floodplain"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "forested_field"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP,  "fungal_jungle"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP,  "glowing_grotto"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "grassland"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "gravel_beach"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "highland"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "hot_springs"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "jacaranda_glade"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "jade_cliffs"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "lavender_field"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "lush_desert"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "lush_savanna"));

        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "maple_woods"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "marsh"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP,  "mediterranean_forest"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "moor"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "muskeg"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "mystic_grove"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "old_growth_dead_forest"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "old_growth_woodland"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "ominous_woods"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "orchard"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "origin_valley"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "pasture"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "prairie"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "pumpkin_patch"));

        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "rainforest"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "redwood_forest"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "rocky_rainforest"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "rocky_shrubland"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "scrubland"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "seasonal_forest"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "shrubland"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP,  "snowblossom_grove"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "snowy_coniferous_forest"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "snowy_fir_clearing"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "snowy_maple_woods"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "spider_nest"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "tropics"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "tundra"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "undergrowth"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "visceral_heap"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "volcano"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "volcanic_plains"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "wasteland"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "wasteland_steppe"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "wetland"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "wintry_origin_valley"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "withered_abyss"));
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(new ResourceLocation(BOP, "woodland"));

        // BWG
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptionalTag(new ResourceLocation(BWG, "overworld"));

//        tag(TreasureTags.Biomes.WITHER_TREE_BIOME_WHITELIST).add(Biomes.BADLANDS);
        tag(TreasureTags.Biomes.WITHER_BIOME_BLACKLIST).addTag(BiomeTags.IS_OCEAN);

    }
}
