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
package com.someguyssoftware.treasure2.runestone;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.util.ResourceLocationUtil;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

/**
 * 
 * @author Mark Gottschling on Jan 15, 2022
 *
 */
public class TreasureRunestones {
	private static final Map<ResourceLocation, IRunestone> REGISTRY = new HashMap<>();

	public static final IRunestone RUNE_OF_ANVIL;
	// RUNE_OF_DAMAGE x2 - SCARCE
	// RUNE_OF_MANA x2 = SCARCE
	// RUNE_OF_EVERLASTING -adornment &  charms are infinite but curse of vanishing is added - MYTHICAL
	// RUNE_OF_ANGELS - healing and protection x2
	// RUNE_OF_METAL_LIFE - uses equipment durability instead of mana


	static {
		RUNE_OF_ANVIL = new AnvilRunestone.Builder(ResourceLocationUtil.create("anvil_rune"))
				.with($ -> {
					$.lore = I18n.translateToLocal("tooltip.runestone.lore.anvil_rune");
					$.rarity = Rarity.EPIC;
				}).build();
		register(RUNE_OF_ANVIL);
	}

	/**
	 * 
	 * @param runestone
	 */
	public static void register(IRunestone runestone) {
		Treasure.logger.debug("registering runestone -> {}", runestone.getName());
		if (!REGISTRY.containsKey(runestone.getName())) {        	
			REGISTRY.put(runestone.getName(), runestone);
		}
	}

	public static Optional<IRunestone> get(ResourceLocation name) {
		if (REGISTRY.containsKey(name)) {
			return Optional.of(REGISTRY.get(name));
		}
		return Optional.empty();
	}

	/**
	 * 
	 * @return
	 */
	public static List<IRunestone> values() {
		return (List<IRunestone>) REGISTRY.values();
	}
}
