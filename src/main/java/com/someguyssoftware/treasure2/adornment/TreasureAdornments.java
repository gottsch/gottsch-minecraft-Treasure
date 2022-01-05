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
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.item.Adornment;
import com.someguyssoftware.treasure2.item.Adornment.Type;
import com.someguyssoftware.treasure2.material.CharmableMaterial;
import com.someguyssoftware.treasure2.util.ResourceLocationUtil;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * 
 * @author Mark Gottschling on Sep 3, 2021
 *
 */
public class TreasureAdornments {
	// NOTE Tag system doesn't exist in 1.12.2 so need registries/maps
	// TODO setup ISimpleMapRegistry
	@Deprecated
	public static final String RING = "ring";
	public static final String NECKLACE = "necklace";
	public static final String BRACELET = "bracelet";
	@Deprecated
	public static final String COPPER = "copper";
	public static final String SILVER = "silver";
	public static final String GOLD = "gold";

	// for future - if other mods can register new types via InterModCommunication
	@Deprecated
	private static final Set<String> TYPES = new HashSet<>();
	@Deprecated
	private static final Set<String> MATERIALS = new HashSet<>();

	// caches - needed?
	private static final List<Adornment> ADORNMENTS_CACHE = new ArrayList<>();
	
	public static final Map<Key, Adornment> REGISTRY = Maps.newHashMap();

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
	
	// @deprecated
	static {
		TYPES.add(RING);
		TYPES.add(NECKLACE);
		TYPES.add(BRACELET);

		MATERIALS.add(COPPER);
		MATERIALS.add(SILVER);
		MATERIALS.add(GOLD);
	}

	private static final Multimap<Type, Adornment> BY_TYPE = ArrayListMultimap.create();
	private static final Multimap<ResourceLocation, Adornment> BY_MATERIAL = ArrayListMultimap.create();

	/**
	 * 
	 * @param type
	 * @param material
	 * @param source
	 * @param adornment
	 */
	public static void register(Type type, AdornmentSize size, ResourceLocation material, ResourceLocation source, Adornment adornment) {
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
	
	public static Optional<Adornment> get(Type type, AdornmentSize size, ResourceLocation material, ResourceLocation source) {
		return get(new Key(type, size, material, source));		
	}
	
	public static Optional<Adornment> get(Key key) {
		Adornment adornment = REGISTRY.get(key);
		return adornment == null ? Optional.empty() : Optional.of(adornment);
	}
	
	/**
	 * 
	 * @param adornment
	 * @param type
	 * @param material
	 */
	@Deprecated
	public static void register(Adornment adornment, Type type, CharmableMaterial material) {
		// TODO ensure params are set

		BY_TYPE.put(type, adornment);
		BY_MATERIAL.put(material.getName(), adornment);
	}

	@Deprecated
	public static List<Adornment> getAll() {
		if (ADORNMENTS_CACHE.isEmpty()) {
			ADORNMENTS_CACHE.addAll(Stream
					.of(BY_TYPE.get(Type.BRACELET),
							BY_TYPE.get(Type.NECKLACE),
							BY_TYPE.get(Type.RING))
					.flatMap(Collection::stream).collect(Collectors.toList())
					);
		}
		Treasure.logger.debug("getAll() -> {}", ADORNMENTS_CACHE);
		return ADORNMENTS_CACHE;
	}

	public static List<Adornment> getByType(Type type) {
		return  (List<Adornment>) BY_TYPE.get(type);
	}

	/**
	 * 
	 * @param type
	 * @return
	 */
//	public static List<Adornment> getByType(String type) {
//		try {
//			return (List<Adornment>) BY_TYPE.get(Type.valueOf(type.toUpperCase()));
//		}
//		catch(Exception e) {
//			return getAll();
//		}
//	}
	
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
	 * @param stack
	 */
	public static void setHoverName(ItemStack stack) {
		if (stack.hasCapability(TreasureCapabilities.CHARMABLE, null)) {
			ICharmableCapability cap = stack.getCapability(TreasureCapabilities.CHARMABLE, null);
			// check first if it is charmed - charmed names supercede source item names
			if (cap.isCharmed()) {
				// 			int level = cap.getHighestLevel().getCharm().getLevel();
				// 			Set<String> tags = stack.getItem().getTags().stream().filter(tag -> tag.getNamespace().equals(Treasure.MODID)) .map(ResourceLocation::getPath).collect(Collectors.toSet());
				// 			String type =tags.contains(RING) ? RING : tags.contains(NECKLACE) ? NECKLACE : tags.contains(BRACELET) ? BRACELET : stack.getItem().getName(stack).getString();

				// 			stack.setHoverName(new TranslationTextComponent("tooltip.adornment.name.level" + level, 
				// 					new TranslationTextComponent("tooltip.adornment.type." + type),
				// 					new TranslationTextComponent("tooltip.charm.type." + cap.getHighestLevel().getCharm().getType().toLowerCase()).getString()));
				// 		}			
				// 		else if (cap.getSourceItem() != null && !cap.getSourceItem().equals(Items.AIR.getRegistryName())) {
				// 			Item sourceItem = ForgeRegistries.ITEMS.getValue(cap.getSourceItem());
				// 			if (!cap.isCharmed()) {					
				// 				stack.setHoverName(
				// 						((TranslationTextComponent)sourceItem.getName(new ItemStack(sourceItem)))
				// 						.append(new StringTextComponent(" "))
				// 						.append(stack.getItem().getName(stack)));
				// 			}
				// 			else {
				// 				stack.setHoverName(
				// 						((TranslationTextComponent)sourceItem.getName(new ItemStack(sourceItem)))
				// 						.append(new StringTextComponent(" "))
				// 						.append(stack.getHoverName()));
				// 			}
				// 		}
			}
		}
	}
		
	public static class Key {
		private Type type;
		private AdornmentSize size;
		private ResourceLocation material;		
		private ResourceLocation source; // TODO rename throughout to stone
		
		public Key(Type type, AdornmentSize size, ResourceLocation material, ResourceLocation source) {
			this.type = type;
			this.size = size;
			this.material = material;
			this.source = source;
		}

		public Type getType() {
			return type;
		}

		public void setType(Type type) {
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