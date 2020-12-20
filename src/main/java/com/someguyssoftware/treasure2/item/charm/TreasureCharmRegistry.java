package com.someguyssoftware.treasure2.item.charm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import net.minecraft.util.ResourceLocation;

public class TreasureCharmRegistry {
    private static final Map<ResourceLocation, ICharm> REGISTRY = new HashMap<>();

    /**
     * 
     * @param charm
     */
    public static void register(ICharm charm) {
        if (!REGISTRY.containsKey(charm.getName())) {
            REGISTRY.put(charm.getName(), charm);
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
     * 
     * @return
     */
    public static List<ICharm> values() {
    	return (List<ICharm>) REGISTRY.values();
    }
}
