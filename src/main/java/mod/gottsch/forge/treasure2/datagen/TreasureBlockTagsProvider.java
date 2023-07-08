/*
 * This file is part of  Treasure2.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
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
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.tags.TreasureTags;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

/**
 * 
 * @author Mark Gottschling on Nov 11, 2022
 *
 */
public class TreasureBlockTagsProvider extends BlockTagsProvider {
    
    public TreasureBlockTagsProvider(PackOutput output, CompletableFuture<Provider> lookupProvider,
			ExistingFileHelper existingFileHelper) {
    	super(output, lookupProvider, Treasure.MODID, existingFileHelper);
	}

	@Override
    protected void addTags(Provider provider) {
    	// blocks rarity
    	tag(TreasureTags.Blocks.COMMON_CHESTS).add(TreasureBlocks.WOOD_CHEST.get());
    	tag(TreasureTags.Blocks.COMMON_CHESTS).add(TreasureBlocks.MOLDY_CRATE_CHEST.get());
    	tag(TreasureTags.Blocks.COMMON_CHESTS).add(TreasureBlocks.CARDBOARD_BOX.get());
    	tag(TreasureTags.Blocks.COMMON_CHESTS).add(TreasureBlocks.MILK_CRATE.get());
    	tag(TreasureTags.Blocks.UNCOMMON_CHESTS).add(TreasureBlocks.CRATE_CHEST.get());
    	tag(TreasureTags.Blocks.UNCOMMON_CHESTS).add(TreasureBlocks.IRONBOUND_CHEST.get());
    	tag(TreasureTags.Blocks.UNCOMMON_CHESTS).add(TreasureBlocks.VIKING_CHEST.get());
    	tag(TreasureTags.Blocks.SCARCE_CHESTS).add(TreasureBlocks.PIRATE_CHEST.get());
    	tag(TreasureTags.Blocks.SCARCE_CHESTS).add(TreasureBlocks.IRON_STRONGBOX.get());
//    	tag(TreasureTags.Blocks.SCARCE_CHESTS).add(TreasureBlocks.SKULL_CHEST.get());
    	tag(TreasureTags.Blocks.RARE_CHESTS).add(TreasureBlocks.GOLD_STRONGBOX.get());
    	tag(TreasureTags.Blocks.RARE_CHESTS).add(TreasureBlocks.SAFE.get());
//    	tag(TreasureTags.Blocks.RARE_CHESTS).add(TreasureBlocks.GOLD_SKULL_CHEST.get());    	
    	tag(TreasureTags.Blocks.RARE_CHESTS).add(TreasureBlocks.SPIDER_CHEST.get());    	
//    	tag(TreasureTags.Blocks.EPIC_CHESTS).add(TreasureBlocks.CRYSTAL_SKULL_CHEST.get());
    	tag(TreasureTags.Blocks.EPIC_CHESTS).add(TreasureBlocks.DREAD_PIRATE_CHEST.get());
    	tag(TreasureTags.Blocks.EPIC_CHESTS).add(TreasureBlocks.COMPRESSOR_CHEST.get());
//    	tag(TreasureTags.Blocks.EPIC_CHESTS).add(TreasureBlocks.CAULDRON_CHEST.get());    	
    	
    	tag(TreasureTags.Blocks.SKULL_CHESTS).add(TreasureBlocks.SKULL_CHEST.get());
    	tag(TreasureTags.Blocks.GOLD_SKULL_CHESTS).add(TreasureBlocks.GOLD_SKULL_CHEST.get());
    	tag(TreasureTags.Blocks.CRYSTAL_SKULL_CHESTS).add(TreasureBlocks.CRYSTAL_SKULL_CHEST.get());
    	tag(TreasureTags.Blocks.WITHER_CHESTS).add(TreasureBlocks.WITHER_CHEST.get());
    }

}
