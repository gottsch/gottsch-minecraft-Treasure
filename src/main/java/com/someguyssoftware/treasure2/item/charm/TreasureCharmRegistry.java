package com.someguyssoftware.treasure2.item.charm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.someguyssoftware.treasure2.charm.ICharm;

import net.minecraft.util.ResourceLocation;

public class TreasureCharmRegistry {
    private static final Map<ResourceLocation, ICharm> REGISTRY = new HashMap<>();
    private static final Map<Integer, List<ICharm>> REGISTRY_BY_LEVEL = new HashMap<>();

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
     * 
     * @return
     */
    public static List<ICharm> values() {
    	return (List<ICharm>) REGISTRY.values();
    }
}
