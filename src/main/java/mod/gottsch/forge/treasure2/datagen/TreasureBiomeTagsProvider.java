/*
 * This file is part of Treasure2.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.forge.treasure2.datagen;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.tags.TreasureTags;
import mod.gottsch.forge.treasure2.datagen.tags.BiomesOPlenty;
import mod.gottsch.forge.treasure2.datagen.tags.BiomesWeveGone;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.Tags;
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
        // setup chest rarity biome filters
        ResourceKey[] filtered = new ResourceKey[]{Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS};

        // POC
        tag(TreasureTags.Biomes.TERRANEAN_RARE_BIOME_FILTER).add(filtered);

        // ////// treasure2 / vanilla tags ////////////////////////////////////////////////
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addTag(BiomeTags.IS_OVERWORLD);
        // TODO need a LAND_ONLY OVERWORLD

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

        // land based
        tag(TreasureTags.Biomes.TERRANEAN).addTags(
                TreasureTags.Biomes.WELLS_GENERAL,
                TreasureTags.Biomes.WELLS_DESERT
                );

        // ocean based - at this point it is the same as vanilla.
        tag(TreasureTags.Biomes.AQUATIC).addTags(
                BiomeTags.IS_OCEAN
        );

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

        // BOP
        tag(TreasureTags.Biomes.BOP_FOREST).addOptional(BiomesOPlenty.CONIFEROUS_FOREST);
        tag(TreasureTags.Biomes.BOP_FOREST).addOptional(BiomesOPlenty.DEAD_FOREST);
        tag(TreasureTags.Biomes.BOP_FOREST).addOptional(BiomesOPlenty.FORESTED_FIELD);
        tag(TreasureTags.Biomes.BOP_FOREST).addOptional(BiomesOPlenty.MAPLE_WOODS);
        tag(TreasureTags.Biomes.BOP_FOREST).addOptional(BiomesOPlenty.MEDITERRANEAN_FOREST);
        tag(TreasureTags.Biomes.BOP_FOREST).addOptional(BiomesOPlenty.OLD_GROWTH_DEAD_FOREST);
        tag(TreasureTags.Biomes.BOP_FOREST).addOptional(BiomesOPlenty.OLD_GROWTH_WOODLAND);
        tag(TreasureTags.Biomes.BOP_FOREST).addOptional(BiomesOPlenty.OMINOUS_WOODS);
        tag(TreasureTags.Biomes.BOP_FOREST).addOptional(BiomesOPlenty.REDWOOD_FOREST);
        tag(TreasureTags.Biomes.BOP_FOREST).addOptional(BiomesOPlenty.RAINFOREST);
        tag(TreasureTags.Biomes.BOP_FOREST).addOptional(BiomesOPlenty.ROCKY_RAINFOREST);
        tag(TreasureTags.Biomes.BOP_FOREST).addOptional(BiomesOPlenty.SEASONAL_FOREST);
        tag(TreasureTags.Biomes.BOP_FOREST).addOptional(BiomesOPlenty.SNOWY_CONIFEROUS_FOREST);
        tag(TreasureTags.Biomes.BOP_FOREST).addOptional(BiomesOPlenty.SNOWY_MAPLE_WOODS);
        tag(TreasureTags.Biomes.BOP_FOREST).addOptional(BiomesOPlenty.WOODLAND);


        tag(TreasureTags.Biomes.BOP_JUNGLE).addOptional(BiomesOPlenty.FUNGAL_JUNGLE);

        tag(TreasureTags.Biomes.BOP_DESERT).addOptional(BiomesOPlenty.LUSH_DESERT);
        tag(TreasureTags.Biomes.BOP_DESERT).addOptional(BiomesOPlenty.DRYLAND);

        tag(TreasureTags.Biomes.BOP_IS_DRY).addOptionalTag(TreasureTags.Biomes.BOP_DESERT);

        // temperate integrations
        tag(TreasureTags.Biomes.TEMPERATE).addOptionalTag(TreasureTags.Biomes.BWG_FOREST);
        tag(TreasureTags.Biomes.TEMPERATE).addOptionalTag(TreasureTags.Biomes.BOP_FOREST);

        // well integrations
        // NOTE not full list - most watery biomes removed. TODO still necessary with beard_thin?
        tag(TreasureTags.Biomes.BOP_IS_DRY).addOptionalTag(TreasureTags.Biomes.BOP_FOREST);
        tag(TreasureTags.Biomes.BOP_IS_DRY).addOptionalTag(TreasureTags.Biomes.BOP_JUNGLE);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.ASPEN_GLADE);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.AURORAL_GARDEN);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.CLOVER_PATCH);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.COLD_DESERT);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.CONIFEROUS_FOREST);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.CRAG);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.CRYSTALLINE_CHASM);

        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.DUNE_BEACH);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.FIELD);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.FIR_CLEARING);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.FLOODPLAIN);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.GLOWING_GROTTO);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.GRASSLAND);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.HIGHLAND);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.HOT_SPRINGS);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.JACARANDA_GLADE);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.JADE_CLIFFS);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.LAVENDER_FIELD);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.LUSH_SAVANNA);

        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.MOOR);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.MUSKEG);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.MYSTIC_GROVE);

        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.ORCHARD);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.ORIGIN_VALLEY);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.PASTURE);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.PRAIRIE);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.PUMPKIN_PATCH);

        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.ROCKY_SHRUBLAND);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.SCRUBLAND);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.SHRUBLAND);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.SNOWBLOSSOM_GROVE);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.SNOWY_FIR_CLEARING);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.TROPICS);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.TUNDRA);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.UNDERGROWTH);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.VOLCANO);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.VOLCANIC_PLAINS);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.WASTELAND);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.WASTELAND_STEPPE);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.WETLAND);
        tag(TreasureTags.Biomes.WELLS_GENERAL).addOptional(BiomesOPlenty.WINTRY_ORIGIN_VALLEY);

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
        tag(TreasureTags.Biomes.WELLS_DESERT).addOptionalTag(TreasureTags.Biomes.BOP_DESERT);

        // Twilight Forest
        // TODO ...
        
        // BOP
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptionalTag(TreasureTags.Biomes.BOP_FOREST);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptionalTag(TreasureTags.Biomes.BOP_JUNGLE);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptionalTag(TreasureTags.Biomes.BOP_DESERT);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.ASPEN_GLADE);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.AURORAL_GARDEN);

        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.CLOVER_PATCH);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.COLD_DESERT);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.CONIFEROUS_FOREST);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.CRAG);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.CRYSTALLINE_CHASM);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.DRYLAND);

        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.DUNE_BEACH);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.FIELD);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.FIR_CLEARING);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.FLOODPLAIN);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.GLOWING_GROTTO);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.GRASSLAND);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.HIGHLAND);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.HOT_SPRINGS);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.JACARANDA_GLADE);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.JADE_CLIFFS);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.LAVENDER_FIELD);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.LUSH_SAVANNA);

        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.MOOR);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.MUSKEG);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.MYSTIC_GROVE);

        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.ORCHARD);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.ORIGIN_VALLEY);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.PASTURE);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.PRAIRIE);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.PUMPKIN_PATCH);

        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.ROCKY_SHRUBLAND);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.SCRUBLAND);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.SHRUBLAND);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.SNOWBLOSSOM_GROVE);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.SNOWY_FIR_CLEARING);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.TROPICS);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.TUNDRA);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.UNDERGROWTH);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.VOLCANO);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.VOLCANIC_PLAINS);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.WASTELAND);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.WASTELAND_STEPPE);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.WETLAND);
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptional(BiomesOPlenty.WINTRY_ORIGIN_VALLEY);

        // BWG
        tag(TreasureTags.Biomes.ALL_OVERWORLD).addOptionalTag(new ResourceLocation(BiomesWeveGone.BWG, "overworld"));

//        tag(TreasureTags.Biomes.WITHER_TREE_BIOME_WHITELIST).add(Biomes.BADLANDS);
        tag(TreasureTags.Biomes.WITHER_BIOME_BLACKLIST).addTag(BiomeTags.IS_OCEAN);

    }
}
