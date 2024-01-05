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
package mod.gottsch.forge.treasure2.core.adornment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import mod.gottsch.forge.treasure2.core.Treasure;
import mod.gottsch.forge.treasure2.core.capability.TreasureCapabilities;
import mod.gottsch.forge.treasure2.core.util.ModUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * 
 * @author Mark Gottschling on Sep 3, 2021
 *
 */
// TODO remove
@Deprecated
public class TreasureAdornments {
	public static final String RING = "ring";
	public static final String NECKLACE = "necklace";
	public static final String BRACELET = "bracelet";

	public static final String COPPER = "copper";
	public static final String SILVER = "silver";
	public static final String GOLD = "gold";

	// for future - if other mods can register new types via InterModCommunication
	private static final Set<String> TYPES = new HashSet<>();
	private static final Set<String> MATERIALS = new HashSet<>();

	// caches
	private static final List<Item> ADORNMENTS_CACHE = new ArrayList<>();

	static {
		TYPES.add(RING);
		TYPES.add(NECKLACE);
		TYPES.add(BRACELET);

		MATERIALS.add(COPPER);
		MATERIALS.add(SILVER);
		MATERIALS.add(GOLD);
	}

	/**
	 * 
	 * @return
	 */
	public static List<Item> getAll() {
		if (ADORNMENTS_CACHE.isEmpty()) {
			ADORNMENTS_CACHE.addAll(Stream
					.of(ItemTags.getAllTags().getTag(ModUtils.asLocation(RING)).getValues(),
							ItemTags.getAllTags().getTag(ModUtils.asLocation(NECKLACE)).getValues(),
							ItemTags.getAllTags().getTag(ModUtils.asLocation(BRACELET)).getValues())
					.flatMap(Collection::stream).collect(Collectors.toList())
					);
		}
		return ADORNMENTS_CACHE;
	}

	/**
	 * 
	 * @param type
	 * @return
	 */
	public static List<Item> getByType(String type) {

		if (TYPES.contains(type.toLowerCase())) {
			return ItemTags.getAllTags().getTag(ModUtils.asLocation(type.toLowerCase())).getValues();
		}
		else {
			return getAll();
		}
	}

	public static List<Item> getByMaterial(String material) {
		if (MATERIALS.contains(material.toLowerCase())) {
			return ItemTags.getAllTags().getTag(ModUtils.asLocation(material.toLowerCase())).getValues();
		}
		else {
			return getAll();
		}
	}

	/**
	 * 
	 * @param stack
	 */
	public static void setHoverName(ItemStack stack) {
		stack.getCapability(TreasureCapabilities.CHARMABLE).ifPresent(cap -> {
			// check first if it is charmed - charmed names supercede source item names
			if (cap.isCharmed()) {
				int level = cap.getHighestLevel().getCharm().getLevel();
				Set<String> tags = stack.getItem().getTags().stream().filter(tag -> tag.getNamespace().equals(Treasure.MODID)) .map(ResourceLocation::getPath).collect(Collectors.toSet());
				String type =tags.contains(RING) ? RING : tags.contains(NECKLACE) ? NECKLACE : tags.contains(BRACELET) ? BRACELET : stack.getItem().getName(stack).getString();

				stack.setHoverName(new TranslationTextComponent("tooltip.adornment.name.level" + level, 
						new TranslationTextComponent("tooltip.adornment.type." + type),
						new TranslationTextComponent("tooltip.charm.type." + cap.getHighestLevel().getCharm().getType().toLowerCase()).getString()));
			}			
			else if (cap.getSourceItem() != null && !cap.getSourceItem().equals(Items.AIR.getRegistryName())) {
				Item sourceItem = ForgeRegistries.ITEMS.getValue(cap.getSourceItem());
				if (!cap.isCharmed()) {					
					stack.setHoverName(
							((TranslationTextComponent)sourceItem.getName(new ItemStack(sourceItem)))
							.append(new StringTextComponent(" "))
							.append(stack.getItem().getName(stack)));
				}
				else {
					stack.setHoverName(
							((TranslationTextComponent)sourceItem.getName(new ItemStack(sourceItem)))
							.append(new StringTextComponent(" "))
							.append(stack.getHoverName()));
				}
			}
		});
	}
}
