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

import org.jetbrains.annotations.Nullable;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.item.TreasureItems;
import mod.gottsch.forge.treasure2.core.tags.TreasureTags;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

public class TreasureItemTagsProvider extends ItemTagsProvider {
	public TreasureItemTagsProvider(PackOutput output, CompletableFuture<Provider> lookup,
			CompletableFuture<TagLookup<Block>> blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, lookup, blockTagProvider, Treasure.MODID, existingFileHelper);
	}

	@Override
	protected void addTags(Provider provider) {     
		// keys rarity
		tag(TreasureTags.Items.COMMON_KEY).add(TreasureItems.WOOD_KEY.get());
		tag(TreasureTags.Items.COMMON_KEY).add(TreasureItems.STONE_KEY.get());
		tag(TreasureTags.Items.UNCOMMON_KEY).add(TreasureItems.LEAF_KEY.get());
		tag(TreasureTags.Items.SCARCE_KEY).add(TreasureItems.EMBER_KEY.get());
		tag(TreasureTags.Items.SCARCE_KEY).add(TreasureItems.LIGHTNING_KEY.get());
		tag(TreasureTags.Items.UNCOMMON_KEY).add(TreasureItems.IRON_KEY.get());
		tag(TreasureTags.Items.SCARCE_KEY).add(TreasureItems.GOLD_KEY.get());
		tag(TreasureTags.Items.RARE_KEY).add(TreasureItems.METALLURGISTS_KEY.get());
		tag(TreasureTags.Items.SCARCE_KEY).add(TreasureItems.TOPAZ_KEY.get());
		tag(TreasureTags.Items.SCARCE_KEY).add(TreasureItems.ONYX_KEY.get());
		tag(TreasureTags.Items.RARE_KEY).add(TreasureItems.DIAMOND_KEY.get());
		tag(TreasureTags.Items.RARE_KEY).add(TreasureItems.EMERALD_KEY.get());
		tag(TreasureTags.Items.EPIC_KEY).add(TreasureItems.RUBY_KEY.get());
		tag(TreasureTags.Items.EPIC_KEY).add(TreasureItems.SAPPHIRE_KEY.get());
		
		tag(TreasureTags.Items.EPIC_KEY).add(TreasureItems.JEWELLED_KEY.get());
		
		tag(TreasureTags.Items.RARE_KEY).add(TreasureItems.SKELETON_KEY.get());
		
		tag(TreasureTags.Items.SCARCE_KEY).add(TreasureItems.SPIDER_KEY.get());
		tag(TreasureTags.Items.RARE_KEY).add(TreasureItems.WITHER_KEY.get());
		
		tag(TreasureTags.Items.COMMON_KEY).add(TreasureItems.PILFERERS_LOCK_PICK.get());
		tag(TreasureTags.Items.UNCOMMON_KEY).add(TreasureItems.THIEFS_LOCK_PICK.get());
		
		tag(TreasureTags.Items.MYTHICAL_KEY).add(TreasureItems.ONE_KEY.get());
		
		tag(TreasureTags.Items.KEYS).addTag(TreasureTags.Items.COMMON_KEY);
		tag(TreasureTags.Items.KEYS).addTag(TreasureTags.Items.UNCOMMON_KEY);
		tag(TreasureTags.Items.KEYS).addTag(TreasureTags.Items.SCARCE_KEY);
		tag(TreasureTags.Items.KEYS).addTag(TreasureTags.Items.RARE_KEY);
		tag(TreasureTags.Items.KEYS).addTag(TreasureTags.Items.EPIC_KEY);
		tag(TreasureTags.Items.KEYS).addTag(TreasureTags.Items.MYTHICAL_KEY);
		
		// locks rarity
		tag(TreasureTags.Items.COMMON_LOCKS).add(TreasureItems.WOOD_LOCK.get());
		tag(TreasureTags.Items.COMMON_LOCKS).add(TreasureItems.STONE_LOCK.get());
		tag(TreasureTags.Items.UNCOMMON_LOCKS).add(TreasureItems.LEAF_LOCK.get());
		tag(TreasureTags.Items.SCARCE_LOCKS).add(TreasureItems.EMBER_LOCK.get());
		tag(TreasureTags.Items.UNCOMMON_LOCKS).add(TreasureItems.IRON_LOCK.get());
		tag(TreasureTags.Items.SCARCE_LOCKS).add(TreasureItems.GOLD_LOCK.get());
		tag(TreasureTags.Items.SCARCE_LOCKS).add(TreasureItems.TOPAZ_LOCK.get());
		tag(TreasureTags.Items.SCARCE_LOCKS).add(TreasureItems.ONYX_LOCK.get());
		tag(TreasureTags.Items.RARE_LOCKS).add(TreasureItems.DIAMOND_LOCK.get());
		tag(TreasureTags.Items.RARE_LOCKS).add(TreasureItems.EMERALD_LOCK.get());
		tag(TreasureTags.Items.EPIC_LOCKS).add(TreasureItems.RUBY_LOCK.get());
		tag(TreasureTags.Items.EPIC_LOCKS).add(TreasureItems.SAPPHIRE_LOCK.get());
		
		tag(TreasureTags.Items.SCARCE_LOCKS).add(TreasureItems.SPIDER_LOCK.get());
		// NOTE wither lock is not tagged as this group is used when selecting locks for chests,
		// and wither is a special lock.
		
		tag(TreasureTags.Items.LOCKS).addTag(TreasureTags.Items.COMMON_LOCKS);
		tag(TreasureTags.Items.LOCKS).addTag(TreasureTags.Items.UNCOMMON_LOCKS);
		tag(TreasureTags.Items.LOCKS).addTag(TreasureTags.Items.SCARCE_LOCKS);
		tag(TreasureTags.Items.LOCKS).addTag(TreasureTags.Items.RARE_LOCKS);
		tag(TreasureTags.Items.LOCKS).addTag(TreasureTags.Items.EPIC_LOCKS);
		
		// wishables rarity
		tag(TreasureTags.Items.COMMON_WISHABLE).add(TreasureItems.COPPER_COIN.get());
		tag(TreasureTags.Items.UNCOMMON_WISHABLE).add(TreasureItems.SILVER_COIN.get());
		tag(TreasureTags.Items.SCARCE_WISHABLE).add(TreasureItems.GOLD_COIN.get());
		tag(TreasureTags.Items.SCARCE_WISHABLE).add(TreasureItems.TOPAZ.get());
		tag(TreasureTags.Items.SCARCE_WISHABLE).add(Items.DIAMOND);
		tag(TreasureTags.Items.SCARCE_WISHABLE).add(Items.EMERALD);
		tag(TreasureTags.Items.RARE_WISHABLE).add(TreasureItems.ONYX.get());
		tag(TreasureTags.Items.RARE_WISHABLE).add(TreasureItems.RUBY.get());
		tag(TreasureTags.Items.RARE_WISHABLE).add(TreasureItems.WHITE_PEARL.get());
		tag(TreasureTags.Items.EPIC_WISHABLE).add(TreasureItems.SAPPHIRE.get());
		tag(TreasureTags.Items.EPIC_WISHABLE).add(TreasureItems.BLACK_PEARL.get());
		
		tag(TreasureTags.Items.WISHABLES).addTag(TreasureTags.Items.COMMON_WISHABLE);
		tag(TreasureTags.Items.WISHABLES).addTag(TreasureTags.Items.UNCOMMON_WISHABLE);
		tag(TreasureTags.Items.WISHABLES).addTag(TreasureTags.Items.SCARCE_WISHABLE);
		tag(TreasureTags.Items.WISHABLES).addTag(TreasureTags.Items.RARE_WISHABLE);
		tag(TreasureTags.Items.WISHABLES).addTag(TreasureTags.Items.EPIC_WISHABLE);
		// NOTE can't add until they have items assigned to them.
		
		// pouchables
		tag(TreasureTags.Items.POUCH).add(TreasureItems.COPPER_COIN.get());
		tag(TreasureTags.Items.POUCH).add(TreasureItems.SILVER_COIN.get());
		tag(TreasureTags.Items.POUCH).add(TreasureItems.GOLD_COIN.get());
		tag(TreasureTags.Items.POUCH).add(TreasureItems.TOPAZ.get());
		tag(TreasureTags.Items.POUCH).add(TreasureItems.ONYX.get());
		tag(TreasureTags.Items.POUCH).add(TreasureItems.RUBY.get());
		tag(TreasureTags.Items.POUCH).add(TreasureItems.SAPPHIRE.get());
		tag(TreasureTags.Items.POUCH).add(TreasureItems.WHITE_PEARL.get());
		tag(TreasureTags.Items.POUCH).add(TreasureItems.BLACK_PEARL.get());
		tag(TreasureTags.Items.POUCH).add(Items.DIAMOND);
		tag(TreasureTags.Items.POUCH).add(Items.EMERALD);
		tag(TreasureTags.Items.POUCH).addTag(TreasureTags.Items.KEYS);
		tag(TreasureTags.Items.POUCH).addTag(TreasureTags.Items.LOCKS);

	}
}
