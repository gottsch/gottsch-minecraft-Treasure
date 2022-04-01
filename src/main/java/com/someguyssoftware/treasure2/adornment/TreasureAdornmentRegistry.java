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
package com.someguyssoftware.treasure2.adornment;

import static com.someguyssoftware.treasure2.capability.TreasureCapabilities.CHARMABLE;
import static com.someguyssoftware.treasure2.capability.TreasureCapabilities.DURABILITY;
import static com.someguyssoftware.treasure2.capability.TreasureCapabilities.RUNESTONES;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.ICharmableCapability;
import com.someguyssoftware.treasure2.capability.InventoryType;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.charm.ICharm;
import com.someguyssoftware.treasure2.charm.TreasureCharms;
import com.someguyssoftware.treasure2.enums.AdornmentType;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.item.Adornment;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.material.CharmableMaterial;
import com.someguyssoftware.treasure2.util.ResourceLocationUtil;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * 
 * @author Mark Gottschling on Sep 3, 2021
 *
 */
public class TreasureAdornmentRegistry {
	// caches - needed?
	private static final List<Adornment> ADORNMENTS_CACHE = new ArrayList<>();
	
	public static final Map<Key, Adornment> REGISTRY = Maps.newHashMap();
	private static final Multimap<AdornmentType, Adornment> BY_TYPE = ArrayListMultimap.create();
	private static final Multimap<ResourceLocation, Adornment> BY_MATERIAL = ArrayListMultimap.create();
	
	public static final AdornmentSize STANDARD = new AdornmentSize("standard");
	public static final AdornmentSize GREAT = new AdornmentSize("great") {
		@Override
		public double modifyMaxLevel(double level) {
			return level + 3;
		};
		@Override
		public double modifyLevelMultiplier(double multi) {
			return multi + 0.25D;
		}		
	};
	public static final AdornmentSize LORDS = new AdornmentSize("lords") {
		@Override
		public double modifyMaxLevel(double level) {
			return level + 5;
		}
		@Override
		public double modifyLevelMultiplier(double multi) {
			return multi + 0.5D;
		}	
	};

	/**
	 * 
	 * @param type
	 * @param material
	 * @param source
	 * @param adornment
	 */
	public static void register(AdornmentType type, AdornmentSize size, ResourceLocation material, ResourceLocation source, Adornment adornment) {
		register(new Key(type, size, material, source), adornment);
	}
	
	public static void register(ResourceLocation material, ResourceLocation source, Adornment adornment) {
		Treasure.logger.debug("registering adornment -> {} with key -> {}", adornment.getRegistryName(), new Key(adornment.getType(), adornment.getSize(), material, source));
		register(new Key(adornment.getType(), adornment.getSize(), material, source), adornment);
	}
	
	public static void register(Key key, Adornment adornment) {
		REGISTRY.put(key, adornment);
		BY_TYPE.put(key.getType(), adornment);
		BY_MATERIAL.put(key.getMaterial(), adornment);
	}
	
	public static Optional<Adornment> get(AdornmentType type, AdornmentSize size, ResourceLocation material, ResourceLocation source) {
		return get(new Key(type, size, material, source));
	}
	
	public static Optional<Adornment> get(Key key) {
		Adornment adornment = REGISTRY.get(key);
		return adornment == null ? Optional.empty() : Optional.of(adornment);
	}

	public static List<Adornment> getAll() {
		if (ADORNMENTS_CACHE.isEmpty()) {
			ADORNMENTS_CACHE.addAll(REGISTRY.values());
		}
		return ADORNMENTS_CACHE;
	}

	public static List<Adornment> getByType(AdornmentType type) {
		return  (List<Adornment>) BY_TYPE.get(type);
	}
	
	public static List<Adornment> getByMaterial(String material) {
		try {
			return getByMaterial(ResourceLocationUtil.create(material));
		}
		catch(Exception e) {
			return getAll();
		}
	}
	
	public static List<Adornment> getByMaterial(ResourceLocation material) {
		return  (List<Adornment>) BY_MATERIAL.get(material);
	}
	
	public static List<Adornment> getByMaterial(CharmableMaterial material) {
		return getByMaterial(material.getName());
	}
	
	/**
	 * 
	 * @param baseStack
	 * @param stoneStack
	 * @return
	 */
	public static Optional<Adornment> getAdornment(ItemStack baseStack, ItemStack stoneStack) {
		if (baseStack.hasCapability(TreasureCapabilities.CHARMABLE, null) && baseStack.getItem() instanceof Adornment) {
			ICharmableCapability cap = baseStack.getCapability(TreasureCapabilities.CHARMABLE, null);
			Adornment sourceAdornment = (Adornment) baseStack.getItem();
			return get(sourceAdornment.getType(), sourceAdornment.getSize(), cap.getBaseMaterial(), stoneStack.getItem().getRegistryName());
		}
		return Optional.empty();
	}
	
	/**
	 * 
	 * @param source
	 * @param dest
	 * @return
	 */
	public static ItemStack copyStack(final ItemStack source, final ItemStack dest) {
		ItemStack resultStack = dest.copy(); // <-- is this necessary?
		// save the source item
		ResourceLocation sourceItem = resultStack.getCapability(CHARMABLE, null).getSourceItem();

		// copy item damage
		resultStack.setItemDamage(source.getItemDamage());
		
		// copy the capabilities
		if (resultStack.hasCapability(DURABILITY, null)) {
			Treasure.logger.debug("calling durability copyTo()");
			source.getCapability(DURABILITY, null).copyTo(resultStack);
		}

		if (dest.hasCapability(CHARMABLE, null)) {
			resultStack.getCapability(CHARMABLE, null).clearCharms();			
			source.getCapability(CHARMABLE, null).copyTo(resultStack);
		}

		if (dest.hasCapability(RUNESTONES, null)) {
			resultStack.getCapability(RUNESTONES, null).clear();			
			source.getCapability(RUNESTONES, null).copyTo(resultStack);
		}

		// reset the source item
		resultStack.getCapability(CHARMABLE, null).setSourceItem(sourceItem);

		return resultStack;
	}
	
	/**
	 * 
	 * @param source
	 * @param dest
	 * @param destInventoryType
	 * @return
	 */
	public static Optional<ItemStack> transferCapabilities(ItemStack source, ItemStack dest, InventoryType sourceType, InventoryType destType) {
		Treasure.logger.debug("transfering caps...");

		// create a new dest item stack
		ItemStack stack = new ItemStack(dest.getItem());

		// copy item damage
		stack.setItemDamage(dest.getItemDamage());
		
		/*
		 * transfer existing state of dest to stack plus any relevant state from source to stack
		 */
		if (dest.hasCapability(DURABILITY, null)) {
			dest.getCapability(DURABILITY, null).copyTo(stack);
		}
		
		Treasure.logger.debug("new stack damage -> {}", stack.getItemDamage());
		
		// transfer
		boolean charmSizeChanged = false;
		boolean runeSizeChanged = false;

		if (dest.hasCapability(CHARMABLE, null)) {
			stack.getCapability(CHARMABLE, null).clearCharms();			
			dest.getCapability(CHARMABLE, null).copyTo(stack);
			if (source.hasCapability(CHARMABLE, null)) {
				source.getCapability(CHARMABLE, null).transferTo(stack, sourceType, destType);
				// check if size has changed. indicates at least 1 charm was transfered. if not, return empty
				if (stack.getCapability(CHARMABLE, null).getCurrentSize(destType) > dest.getCapability(CHARMABLE, null).getCurrentSize(destType)) {
					charmSizeChanged = true;
				}
			}
		}

		if (dest.hasCapability(RUNESTONES, null)) {
			stack.getCapability(RUNESTONES, null).clear();
			Treasure.logger.debug("before copyTo, runes size -> {}", stack.getCapability(RUNESTONES, null).getEntitiesCopy().size());
			dest.getCapability(RUNESTONES, null).copyTo(stack);
			Treasure.logger.debug("after copyTo, runes size -> {}", stack.getCapability(RUNESTONES, null).getEntitiesCopy().size());
			if (source.hasCapability(RUNESTONES, null)) { // this is the rune
				Treasure.logger.debug("source(runestone)'s runes ->");
				source.getCapability(RUNESTONES, null).getEntities(InventoryType.INNATE).forEach(entity -> {
					Treasure.logger.debug("source entity -> {}", entity);
				});
				source.getCapability(RUNESTONES, null).transferTo(stack, sourceType, destType); // transfer from rune to output
				Treasure.logger.debug("after transferTo, runes size -> {}", stack.getCapability(RUNESTONES, null).getEntitiesCopy().size());
				if (stack.getCapability(RUNESTONES, null).getCurrentSize(destType) > dest.getCapability(RUNESTONES, null).getCurrentSize(destType)) {
					runeSizeChanged = true;
				}
			}
		}
		if (charmSizeChanged | runeSizeChanged) {
			return Optional.of(stack);
		}
		return Optional.empty();
	}
		
	public static class Key {
		private AdornmentType type;
		private AdornmentSize size;
		private ResourceLocation material;		
		private ResourceLocation source; // TODO rename throughout to stone
		
		public Key(AdornmentType type, AdornmentSize size, ResourceLocation material, ResourceLocation source) {
			this.type = type;
			this.size = size;
			this.material = material;
			this.source = source;
		}

		public AdornmentType getType() {
			return type;
		}

		public void setType(AdornmentType type) {
			this.type = type;
		}

		public ResourceLocation getMaterial() {
			return material;
		}

		public void setMaterial(ResourceLocation material) {
			this.material = material;
		}

		public ResourceLocation getSource() {
			return source;
		}

		public void setSource(ResourceLocation source) {
			this.source = source;
		}

		public AdornmentSize getSize() {
			return size;
		}

		public void setSize(AdornmentSize size) {
			this.size = size;
		}

		@Override
		public String toString() {
			return "Key [type=" + type + ", size=" + size + ", material=" + material + ", source=" + source + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((material == null) ? 0 : material.hashCode());
			result = prime * result + ((size == null) ? 0 : size.hashCode());
			result = prime * result + ((source == null) ? 0 : source.hashCode());
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Key other = (Key) obj;
			if (material == null) {
				if (other.material != null)
					return false;
			} else if (!material.equals(other.material))
				return false;
			if (size == null) {
				if (other.size != null)
					return false;
			} else if (!size.equals(other.size))
				return false;
			if (source == null) {
				if (other.source != null)
					return false;
			} else if (!source.equals(other.source))
				return false;
			if (type != other.type)
				return false;
			return true;
		}
	}
}