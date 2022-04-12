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
package com.someguyssoftware.treasure2.charm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import com.someguyssoftware.treasure2.enums.Rarity;

import net.minecraft.util.ResourceLocation;

public class TreasureCharmRegistry {
    private static final Map<ResourceLocation, ICharm> REGISTRY = new HashMap<>();
    private static final Map<Integer, List<ICharm>> REGISTRY_BY_LEVEL = new HashMap<>();
    private static final Map<Rarity, List<ICharm>> REGISTRY_BY_RARITY = new HashMap<>();

    /**
     * 
     * @param charm
     */
    public static void register(ICharm charm) {
        if (!REGISTRY.containsKey(charm.getName())) {
            REGISTRY.put(charm.getName(), charm);
        }
        if (!REGISTRY_BY_LEVEL.containsKey(Integer.valueOf(charm.getLevel()))) {
        	List<ICharm> charmList = new ArrayList<>();
        	charmList.add(charm);
        	REGISTRY_BY_LEVEL.put(Integer.valueOf(charm.getLevel()), charmList);            
        }
        else {
        	REGISTRY_BY_LEVEL.get(Integer.valueOf(charm.getLevel())).add(charm);
        }
        if (!REGISTRY_BY_RARITY.containsKey(charm.getRarity())) {
            List<ICharm> charmList = new ArrayList<>();
            charmList.add(charm);
            REGISTRY_BY_RARITY.put(charm.getRarity(), charmList);
        }
        else {
            REGISTRY_BY_RARITY.get(charm.getRarity()).add(charm);
        }
    }

    /**
     * 
     * @param name
     * @return
     */
    public static Optional<ICharm> get(ResourceLocation name) {
        
        if (REGISTRY.containsKey(name)) {
            return Optional.of(REGISTRY.get(name));
        }
        return Optional.empty();
    }
    
    /**
     * @param level
     * @return
     */
    public static Optional<List<ICharm>> get(Integer level) {
        if (REGISTRY_BY_LEVEL.containsKey(level)) {
            return Optional.of(REGISTRY_BY_LEVEL.get(level));
        }
        return Optional.empty();
    }

    /**
     * @param rarity
     * @return
     */
    public static Optional<List<ICharm>> get(Rarity rarity) {
        if (REGISTRY_BY_RARITY.containsKey(rarity)) {
            return Optional.of(REGISTRY_BY_RARITY.get(rarity));
        }
        return Optional.empty();
    }

	/**
	 * 
	 * @param predicate
	 * @return
	 */
	public static Optional<List<ICharm>> getBy(Predicate<ICharm> predicate) {
		List<ICharm> charms = new ArrayList<>();
		for (ICharm c : TreasureCharmRegistry.values()) {
			if (predicate.test(c)) {
				charms.add(c);
			}
		}
		if (charms.size() == 0) {
			return Optional.empty();
		}
		return Optional.of(charms);
	}
	
    /**
     * 
     * @return
     */
    public static List<ICharm> values() {
    	return new ArrayList<ICharm>(REGISTRY.values());
    }
}
