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

import java.util.Collection;
import java.util.List;

import mod.gottsch.forge.treasure2.core.Treasure;
import mod.gottsch.forge.treasure2.core.adornment.TreasureAdornmentRegistry;
import mod.gottsch.forge.treasure2.core.charm.TreasureCharmRegistry;
import mod.gottsch.forge.treasure2.core.enums.AdornmentType;
import mod.gottsch.forge.treasure2.core.item.Adornment;
import mod.gottsch.forge.treasure2.core.item.CharmItem;
import mod.gottsch.forge.treasure2.core.item.TreasureItems;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
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
        List<Adornment> adornments = TreasureAdornmentRegistry.getByType(AdornmentType.RING);
        adornments.forEach(ring -> {
        	tag(TreasureTags.Items.RING).add(ring);
        });
        
        adornments = TreasureAdornmentRegistry.getByType(AdornmentType.NECKLACE);
        adornments.forEach(necklace -> {
        	tag(TreasureTags.Items.NECKLACE).add(necklace);
        });
        
        adornments = TreasureAdornmentRegistry.getByType(AdornmentType.BRACELET);
        adornments.forEach(bracelet -> {
        	tag(TreasureTags.Items.BRACELET).add(bracelet);
        });
        
        // special adornments
        tag(TreasureTags.Items.RING).add(TreasureItems.ANGELS_RING.get());
        tag(TreasureTags.Items.RING).add(TreasureItems.RING_OF_FORTITUDE.get());
        tag(TreasureTags.Items.RING).add(TreasureItems.SHADOWS_GIFT.get());
        tag(TreasureTags.Items.RING).add(TreasureItems.RING_OF_LIFE_DEATH.get());
        tag(TreasureTags.Items.RING).add(TreasureItems.CASTLE_RING.get());
        tag(TreasureTags.Items.RING).add(TreasureItems.PEASANTS_FORTUNE.get());
        tag(TreasureTags.Items.RING).add(TreasureItems.GOTTSCHS_RING_OF_MOON.get());
        tag(TreasureTags.Items.NECKLACE).add(TreasureItems.GOTTSCHS_AMULET_OF_HEAVENS.get());
        tag(TreasureTags.Items.BRACELET).add(TreasureItems.BRACELET_OF_WONDER.get());
        tag(TreasureTags.Items.NECKLACE).add(TreasureItems.MEDICS_TOKEN.get());
        tag(TreasureTags.Items.BRACELET).add(TreasureItems.ADEPHAGIAS_BOUNTY.get());
        tag(TreasureTags.Items.NECKLACE).add(TreasureItems.SALANDAARS_WARD.get());
        tag(TreasureTags.Items.BRACELET).add(TreasureItems.MIRTHAS_TORCH.get());
        
        // pocket watch
        tag(TreasureTags.Items.CHARM).add(TreasureItems.POCKET_WATCH.get());
        
        // charms
        Collection<CharmItem> charms = TreasureItems.CHARM_ITEMS.values();
        charms.forEach(charm -> {
        	tag(TreasureTags.Items.CHARM).add(charm);
        });
        
        // pouch
        tag(TreasureTags.Items.BELT).add(TreasureItems.POUCH.get());
    }
}
