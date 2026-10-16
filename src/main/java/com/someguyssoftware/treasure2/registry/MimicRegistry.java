package com.someguyssoftware.treasure2.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

/**
 * Maps a chest block to the one mimic entity that disguises itself as that chest.
 * <p>
 * A chest only records whether it is a mimic. Which mimic is always resolved from the chest's block
 * through this registry, so a chest's mimic always matches the chest (ex. a wood chest is always a wood mimic).
 *
 * @author Mark Gottschling on 10/16/2026
 */
public class MimicRegistry {
	/*
	 * chest block name -> mimic entity name
	 */
	private static final Map<ResourceLocation, ResourceLocation> MAP = new HashMap<>();

	private MimicRegistry() {}

	/**
	 * Registers the mimic for a chest. A chest has only one mimic - registering again replaces it.
	 * @param chest the chest block registry name
	 * @param mimic the mimic entity registry name
	 */
	public static synchronized void register(ResourceLocation chest, ResourceLocation mimic) {
		MAP.put(chest, mimic);
	}

	/**
	 * @param chest the chest block registry name
	 * @return the mimic entity registry name
	 */
	public static synchronized Optional<ResourceLocation> getMimic(ResourceLocation chest) {
		return Optional.ofNullable(MAP.get(chest));
	}

	public static Optional<ResourceLocation> getMimic(Block chest) {
		return chest == null ? Optional.empty() : getMimic(chest.getRegistryName());
	}

	public static boolean hasMimic(Block chest) {
		return getMimic(chest).isPresent();
	}

	/**
	 * @return the registry names of the chests that have a mimic
	 */
	public static synchronized List<ResourceLocation> getChests() {
		return new ArrayList<>(MAP.keySet());
	}
}
