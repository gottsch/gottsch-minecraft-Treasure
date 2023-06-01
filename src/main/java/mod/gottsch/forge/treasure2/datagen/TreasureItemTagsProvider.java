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

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.item.TreasureItems;
import mod.gottsch.forge.treasure2.core.tags.TreasureTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;

public class TreasureItemTagsProvider extends ItemTagsProvider {
	/**
	 * 
	 * @param dataGenerator
	 * @param blockTagProvider
	 * @param existingFileHelper
	 */
	public TreasureItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, ExistingFileHelper existingFileHelper) {
		super(dataGenerator, blockTagProvider, Treasure.MODID, existingFileHelper);
	}

	@Override
	protected void addTags() {     
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
//		tag(TreasureTags.Items.SCARCE_WISHABLE).add(TreasureItems.SILVER_COIN.get());
		tag(TreasureTags.Items.SCARCE_WISHABLE).add(TreasureItems.GOLD_COIN.get());
//		tag(TreasureTags.Items.RARE_WISHABLE).add(TreasureItems.GOLD_COIN.get());
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
//		tag(TreasureTags.Items.WISHABLES).addTag(TreasureTags.Items.LEGENDARY_WISHABLE);
//		tag(TreasureTags.Items.WISHABLES).addTag(TreasureTags.Items.MYTHICAL_WISHABLE);
		// @Deprecated NOTE white_pearl and black_pearl are not mapped via tags because they use
		// custom, non-rarity based loot tables
		
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
		
		//        List<Adornment> adornments = TreasureAdornmentRegistry.getByType(AdornmentType.RING);
		//        adornments.forEach(ring -> {
		//        	tag(TreasureTags.Items.RING).add(ring);
		//        });
		//        
		//        adornments = TreasureAdornmentRegistry.getByType(AdornmentType.NECKLACE);
		//        adornments.forEach(necklace -> {
		//        	tag(TreasureTags.Items.NECKLACE).add(necklace);
		//        });
		//        
		//        adornments = TreasureAdornmentRegistry.getByType(AdornmentType.BRACELET);
		//        adornments.forEach(bracelet -> {
		//        	tag(TreasureTags.Items.BRACELET).add(bracelet);
		//        });
		//        
		//        // special adornments
		//        tag(TreasureTags.Items.RING).add(TreasureItems.ANGELS_RING.get());
		//        tag(TreasureTags.Items.RING).add(TreasureItems.RING_OF_FORTITUDE.get());
		//        tag(TreasureTags.Items.RING).add(TreasureItems.SHADOWS_GIFT.get());
		//        tag(TreasureTags.Items.RING).add(TreasureItems.RING_OF_LIFE_DEATH.get());
		//        tag(TreasureTags.Items.RING).add(TreasureItems.CASTLE_RING.get());
		//        tag(TreasureTags.Items.RING).add(TreasureItems.PEASANTS_FORTUNE.get());
		//        tag(TreasureTags.Items.RING).add(TreasureItems.GOTTSCHS_RING_OF_MOON.get());
		//        tag(TreasureTags.Items.NECKLACE).add(TreasureItems.GOTTSCHS_AMULET_OF_HEAVENS.get());
		//        tag(TreasureTags.Items.BRACELET).add(TreasureItems.BRACELET_OF_WONDER.get());
		//        tag(TreasureTags.Items.NECKLACE).add(TreasureItems.MEDICS_TOKEN.get());
		//        tag(TreasureTags.Items.BRACELET).add(TreasureItems.ADEPHAGIAS_BOUNTY.get());
		//        tag(TreasureTags.Items.NECKLACE).add(TreasureItems.SALANDAARS_WARD.get());
		//        tag(TreasureTags.Items.BRACELET).add(TreasureItems.MIRTHAS_TORCH.get());

		// pocket watch
		//        tag(TreasureTags.Items.CHARM).add(TreasureItems.POCKET_WATCH.get());

		// charms
		//        Collection<CharmItem> charms = TreasureItems.CHARM_ITEMS.values();
		//        charms.forEach(charm -> {
		//        	tag(TreasureTags.Items.CHARM).add(charm);
		//        });
		//        
		//        // pouch
		//        tag(TreasureTags.Items.BELT).add(TreasureItems.POUCH.get());
	}
}
