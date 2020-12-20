package com.someguyssoftware.treasure2.item.charm;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import net.minecraft.util.ResourceLocation;

public class TreasureCharmRegistry {
    private static final Map<ResourceLocation, ICharm> REGISTRY = new HashMap<>();

    /**
     * 
     * @param modID
     * @param charm
     */
    @Deprecated
    public static void register(String modID, ICharm charm) {
        ResourceLocation key = new ResourceLocation(modID, charm.getName().toLowerCase());
        if (!REGISTRY.containsKey(key)) {
            REGISTRY.put(key, charm);
        }
    }
    
    public static void register(ICharm charm) {
        if (!REGISTRY.containsKey(charm.getName2())) {
            REGISTRY.put(key, charm);
        }
    }

    /**
     * 
     * @param modID
     * @param name
     * @return
     */
    @Deprecated
    public static Optional<ICharm> get(String modID, String name) {
        ResourceLocation key = new ResourceLocation(modID, name.toLowerCase());
        if (REGISTRY.containsKey(key)) {
            return Optional.of(REGISTRY.get(key));
        }
        return Optional.empty();
    }
    
    public static Optional<ICharm> get(ResourceLocation name) {
        
        if (REGISTRY.containsKey(name)) {
            return Optional.of(REGISTRY.get(name));
        }
        return Optional.empty();
    }
}
