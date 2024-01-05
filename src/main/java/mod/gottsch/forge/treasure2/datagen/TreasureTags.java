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

import mod.gottsch.forge.treasure2.core.enums.AdornmentType;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
/**
 * 
 * @author Mark Gottschling on Sep 9, 2022
 *
 */
public class TreasureTags {
	private static final String CURIOS_MODID = "curios";
	
	public static final class Items {
		public static final ITag.INamedTag<Item> NECKLACE = mod(CURIOS_MODID, AdornmentType.NECKLACE.getValue());
		public static final ITag.INamedTag<Item> RING = mod(CURIOS_MODID, AdornmentType.RING.getValue());
		public static final ITag.INamedTag<Item> BRACELET = mod(CURIOS_MODID, AdornmentType.BRACELET.getValue());
		public static final ITag.INamedTag<Item> CHARM = mod(CURIOS_MODID, "charm");
		public static final ITag.INamedTag<Item> BELT = mod(CURIOS_MODID, "belt");
		
		private static ITag.INamedTag<Item> mod(String domain, String path) {
			return ItemTags.bind(new ResourceLocation(domain, path).toString());
		}
	}
}
