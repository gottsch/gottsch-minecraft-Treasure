/*
 * This file is part of Treasure2.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.forge.treasure2.core.registry;

import com.google.common.collect.Maps;
import mod.gottsch.forge.treasure2.core.rarity.RarityOrder;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Map;

/**
 * @author by Mark Gottschling on 9/4/2025
 */
public enum RarityOrderRegistry {
    INSTANCE;

    private static final List<RarityOrder> CORE = Lists.newArrayList();
    // NOTE not implemented but any special rarity order would go here
    private static final Map<String, List<RarityOrder>> SPECIALTY = Maps.newHashMap();

    public static void clear() {
        CORE.clear();
    }

    public static void registerCore(RarityOrder rarityOrder) {
        if (!CORE.contains(rarityOrder)) {
            CORE.add(rarityOrder);
        }
    }

    public static List<RarityOrder> getCore() {
        return List.copyOf(CORE);
    }

    /**
     * should be called after all rarity orders have been registered.
     */
    public static void sortCore() {
        CORE.sort(RarityOrder.BY_ORDER);
    }
}
