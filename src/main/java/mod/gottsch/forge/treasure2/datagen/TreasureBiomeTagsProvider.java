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

import java.util.concurrent.CompletableFuture;

import mod.gottsch.forge.treasure2.Treasure;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

/**
 * 
 * @author Mark Gottschling on May 20, 2023
 *
 */
public class TreasureBiomeTagsProvider extends BiomeTagsProvider {
    /**
     * 
     * @param generatorIn
     * @param existingFileHelper
     */
	public TreasureBiomeTagsProvider(PackOutput output, CompletableFuture<Provider> lookup, ExistingFileHelper existingFileHelper) {
        super(output, lookup, Treasure.MODID, existingFileHelper);
    }
    
    @Override
    protected void addTags(Provider provider) {
    	// blocks rarity
//    	tag(TreasureTags.Biomes.DESERT_WELL1).add(Biomes.DESERT);
//    	tag(TreasureTags.Biomes.DESERT_WELL2).add(Biomes.DESERT);
    }
}
