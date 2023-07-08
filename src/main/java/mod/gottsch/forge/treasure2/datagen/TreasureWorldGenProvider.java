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

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.world.feature.TreasureConfiguredFeatures;
import mod.gottsch.forge.treasure2.core.world.feature.TreasurePlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;

/**
 * 
 * @author Mark Gottschling Jul 7, 2023
 *
 */
public class TreasureWorldGenProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()

    		// TODO this line will not compile!
//            .add(Registries.CONFIGURED_FEATURE, TreasureConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, TreasurePlacedFeatures::bootstrap);

    public TreasureWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(Treasure.MODID));
    }
}
