/*
 * This file is part of  Treasure2.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableObject;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.treasure2.core.item.KeyItem;
import mod.gottsch.forge.treasure2.core.item.LockItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;

/**
 * 
 * @author Mark Gottschling on Nov 11, 2022
 *
 */
public class KeyLockRegistry {

	/*
	 * a Table registry for rarity/key lookups for keys
	 */
	private static final Multimap<IRarity, RegistryObject<KeyItem>> KEYS_BY_RARITY;
	private static final Map<ResourceLocation, RegistryObject<KeyItem>> KEYS_BY_NAME;
	private static final Map<ResourceLocation, IRarity> KEY_RARITY_BY_NAME;
	
	private static final Multimap<IRarity, RegistryObject<LockItem>> LOCKS_BY_RARITY;
	private static final Map<ResourceLocation, RegistryObject<LockItem>> LOCKS_BY_NAME;
	private static final Map<ResourceLocation, MutableObject<IRarity>> LOCK_RARITY_BY_NAME;

//	private static final Map<String, IKeyLockCategory> CATEGORY_REGISTRY;
	
	static {
		KEYS_BY_RARITY = ArrayListMultimap.create();
		KEYS_BY_NAME = Maps.newHashMap();
		KEY_RARITY_BY_NAME = Maps.newHashMap();
		
		LOCKS_BY_RARITY = ArrayListMultimap.create();
		LOCKS_BY_NAME = Maps.newHashMap();
		LOCK_RARITY_BY_NAME = Maps.newHashMap();
		
//		CATEGORY_REGISTRY = Maps.newHashMap();
	}

	private KeyLockRegistry() {

	}

	/**
	 * 
	 * @param key
	 */
	public static void registerKey(RegistryObject<KeyItem> key) {		
		// register by the key's tag
		//	Set<String> tags = ForgeRegistries.ITEMS.tags().stream().filter(tag -> tag.getKey().location().getNamespace().equals(Treasure.MODID)).map(tag -> tag.getKey().location().getPath()).collect(Collectors.toSet());

		if (!KEYS_BY_NAME.containsKey(key.get().getRegistryName())) {
			KEYS_BY_NAME.put(key.get().getRegistryName(), key);
		}
	}
	
	public static void registerLock(RegistryObject<LockItem> lock) {
		if (!LOCKS_BY_NAME.containsKey(lock.get().getRegistryName())) {
			LOCKS_BY_NAME.put(lock.get().getRegistryName(), lock);
		}
		
	}
	
	/**
	 * 
	 * @param rarity
	 * @param key
	 */
	public static void registerKeyByRarity(IRarity rarity, RegistryObject<KeyItem> key) {
		KEYS_BY_RARITY.put(rarity, key);
		if (!KEY_RARITY_BY_NAME.containsKey(key.getId())) {
			KEY_RARITY_BY_NAME.put(key.getId(), rarity);
		}
	}
	
	public static void registerLockByRarity(IRarity rarity, RegistryObject<LockItem> lock) {
		LOCKS_BY_RARITY.put(rarity, lock);
		if (!LOCK_RARITY_BY_NAME.containsKey(lock.getId())) {
			LOCK_RARITY_BY_NAME.put(lock.getId(), new MutableObject<>(rarity));
		}
	}
		
	public static List<RegistryObject<KeyItem>> getKeys() {
		return new ArrayList<>(KEYS_BY_NAME.values());
	}
	
	public static List<RegistryObject<KeyItem>> getKeys(IRarity rarity) {
		return new ArrayList<>(KEYS_BY_RARITY.get(rarity));
	}
	
	public static List<RegistryObject<LockItem>> getLocks() {
		return new ArrayList<>(LOCKS_BY_NAME.values());
	}
	
	public static List<RegistryObject<LockItem>> getLocks(IRarity rarity) {
		return new ArrayList<>(LOCKS_BY_RARITY.get(rarity));
	}

	public static void clearKeysByRarity() {
		KEYS_BY_RARITY.clear();		
		KEY_RARITY_BY_NAME.clear();
	}
	
	public static void clearLocksByRarity() {
		LOCKS_BY_RARITY.clear();
		LOCK_RARITY_BY_NAME.clear();
	}
	
	public static IRarity getRarityByKey(ResourceLocation key) {
		if (KEY_RARITY_BY_NAME.containsKey(key)) {
			return KEY_RARITY_BY_NAME.get(key);
		}
		return null;
	}
	public static IRarity getRarityByKey(KeyItem key) {
		return getRarityByKey(key.getRegistryName());
	}
	
	public static IRarity getRarityByLock(ResourceLocation lock) {
		if (LOCK_RARITY_BY_NAME.containsKey(lock)) {
			return LOCK_RARITY_BY_NAME.get(lock).getValue();
		}
		return null;
	}
	/**
	 * convenience method
	 * @param lock
	 * @return
	 */
	public static IRarity getRarityByLock(LockItem lock) {
		return getRarityByLock(lock.getRegistryName());
	}

//	public static void registerCategory(IKeyLockCategory category) {
//		if (!CATEGORY_REGISTRY.containsKey(category.toString())) {
//			CATEGORY_REGISTRY.put(category.toString(), category);
//		}
//	}
	
	// TODO research
	// might not make this available - what happens if you unregister AFTER
	// keys/locks have been associated with a unregister category?
	// - they all get set to NONE?
//	public static void unregister(IKeyLockCategory category) {
//		if (CATEGORY_REGISTRY.containsKey(category.toString())) {
//			CATEGORY_REGISTRY.remove(category.toString());
//		}
//	}
}
