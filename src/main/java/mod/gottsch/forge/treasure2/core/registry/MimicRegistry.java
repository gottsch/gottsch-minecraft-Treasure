package mod.gottsch.forge.treasure2.core.registry;

import java.util.*;

import net.minecraft.resources.ResourceLocation;

/**
 * NOTE this is an mod api association. you cannot setup these associations with tags/json data.
 */
public class MimicRegistry {
	/**
	 * A map from Chest name to Mimic name.
	 */
	private static final Map<ResourceLocation, ResourceLocation> MAP = new HashMap<>();

	/**
	 * 
	 */
	private MimicRegistry() {	}
	
	/**
	 * 
	 * @param chest
	 * @param mimic
	 */
	public static synchronized void register(ResourceLocation chest, ResourceLocation mimic) {
		MAP.put(chest, mimic);
	}
	
	/**
	 * 
	 * @param chest
	 * @return
	 */
	public static synchronized Optional<ResourceLocation> getMimic(ResourceLocation chest) {
		if (MAP.containsKey(chest)) {
			return Optional.of(MAP.get(chest));
		}
		return Optional.empty();
	}
	
	public static List<ResourceLocation> getNames() {
		return new ArrayList<>(MAP.keySet());
	}
	
	public static List<ResourceLocation> getMimics() {
		return new ArrayList<>(MAP.values());
	}
}


