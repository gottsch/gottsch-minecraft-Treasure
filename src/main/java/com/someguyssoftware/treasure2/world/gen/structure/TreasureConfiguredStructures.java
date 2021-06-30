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
package com.someguyssoftware.treasure2.world.gen.structure;

import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

/**
 * @author Mark Gottschling on Jun 18, 2021
 *
 */
public class TreasureConfiguredStructures {
    /**
     * Static instance of our structure so we can reference it and add it to biomes easily.
     */
    public static StructureFeature<?, ?> CONFIGURED_SURFACE_CHEST = TreasureStructures.SURFACE_CHEST.get().configured(IFeatureConfig.NONE);
    public static StructureFeature<?, ?> CONFIGURED_UNCOMMON_SURFACE_CHEST = TreasureStructures.UNCOMMON_SURFACE_CHEST.get().configured(IFeatureConfig.NONE);
    
    /**
     * Registers the configured structure which is what gets added to the biomes.
     * Noticed we are not using a forge registry because there is none for configured structures.
     *
     * We can register configured structures at any time before a world is clicked on and made.
     * But the best time to register configured features by code is honestly to do it in FMLCommonSetupEvent.
     */
    public static void registerConfiguredStructures() {
        Registry<StructureFeature<?, ?>> registry = WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE;
        Registry.register(registry, new ResourceLocation(Treasure.MODID, "configured_surface_chest"), CONFIGURED_SURFACE_CHEST);
        Registry.register(registry, new ResourceLocation(Treasure.MODID, "configured_surface_chest"), CONFIGURED_UNCOMMON_SURFACE_CHEST);

        /* Ok so, this part may be hard to grasp but basically, just add your structure to this to
        * prevent any sort of crash or issue with other mod's custom ChunkGenerators. If they use
        * FlatGenerationSettings.STRUCTURE_FEATURES in it and you don't add your structure to it, the game
        * could crash later when you attempt to add the StructureSeparationSettings to the dimension.
        *
        * (It would also crash with superflat worldtype if you omit the below line
        * and attempt to add the structure's StructureSeparationSettings to the world)
        *
        * Note: If you want your structure to spawn in superflat, remove the FlatChunkGenerator check
        * in StructureTutorialMain.addDimensionalSpacing and then create a superflat world, exit it,
        * and re-enter it and your structures will be spawning. I could not figure out why it needs
        * the restart but honestly, superflat is really buggy and shouldn't be your main focus in my opinion.
        *
        * Requires AccessTransformer ( see resources/META-INF/accesstransformer.cfg )
        */
        FlatGenerationSettings.STRUCTURE_FEATURES.put(TreasureStructures.SURFACE_CHEST.get(), CONFIGURED_SURFACE_CHEST);
        FlatGenerationSettings.STRUCTURE_FEATURES.put(TreasureStructures.UNCOMMON_SURFACE_CHEST.get(), CONFIGURED_UNCOMMON_SURFACE_CHEST);
    }
}
