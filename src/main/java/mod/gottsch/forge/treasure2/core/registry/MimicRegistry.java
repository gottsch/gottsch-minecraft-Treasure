package mod.gottsch.forge.treasure2.core.registry;

import java.util.*;

import net.minecraft.resources.ResourceLocation;

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
	public static void register(ResourceLocation chest, ResourceLocation mimic) {
		MAP.put(chest, mimic);
	}
	
	/**
	 * 
	 * @param chest
	 * @return
	 */
	public static Optional<ResourceLocation> getMimic(ResourceLocation chest) {
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


