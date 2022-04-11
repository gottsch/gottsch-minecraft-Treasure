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
package com.someguyssoftware.treasure2.capability;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.modifier.ILevelModifier;
import com.someguyssoftware.treasure2.capability.modifier.NoLevelModifier;
import com.someguyssoftware.treasure2.charm.ICharm;
import com.someguyssoftware.treasure2.charm.ICharmEntity;
import com.someguyssoftware.treasure2.material.CharmableMaterial;
import com.someguyssoftware.treasure2.material.TreasureCharmableMaterials;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

/**
 * The CharmableCapability provides any item with a Charm Inventory, which is a datatype of  Multimap<InventoryType ICharmEntity>.
 * TODO revise verbiage - move this to MagicsInventoryCap and reference.
 * The Charm Inventory can support three types: INNATE, IMBUE or SOCKET.
 * INNATE - Charms that are added on Item spawn and can not be renewed. Once expired, the charm is removed and the space is decremented.
 * IMBUE - Charms that can be added via Books, or any IMBUING item.
 * SOCKET - Charms that can be added to sockets via Coins or Gems, or any BINDING item. 
 * @author Mark Gottschling on Dec 30, 2021
 *
 */
public class CharmableCapability implements ICharmableCapability {

	// TODO requies a reference back to the hosting Item because some methods (ex getMaxLevel) required additional computation that is provided from the Item ex. AdornmentSize.
	// TODO probably need to extend this class for Adornments, in order to provide the addtional functionality.

	/*
	 * Properties that refer to the Item that has this capability
	 */
	Multimap<InventoryType, ICharmEntity> charmEntities = ArrayListMultimap.create();

	//	private IMagicsInventoryCapability magicsCap;

	// is this item a charm source/originator ie. not an adornment or an item that is imbued or socketed with a charm
	private boolean source;
	// can this item cast/execute its charms
	private boolean executing;
	// is this item bindable to a target item that is socketable
	private boolean bindable;
	// does this item have sockets - accepts bindable items
	private boolean socketable;
	// can this item imbue target item
	private boolean imbuing;
	// can this item be imbued
	private boolean imbuable;
	// does this item have "built-in" innate charms
	private boolean innate;

	// the base material this item is made of
	private ResourceLocation baseMaterial;
	// the item that this capability belongs to
	private ResourceLocation sourceItem;
	// the max charm level allowed. calculated by baseMaterial, sourceItem and level modifiers
	private int maxLevel;

	// the current charm with the highest level
	private ICharmEntity highestLevel;

	// can this item be named by its material components ex. Gold Ring, Topaz Gold Ring
	private boolean namedByMaterial;
	// can this item be named by its charms and levels ex. Giant Ring of Healing
	private boolean namedByCharm;

	/*
	 * modifiers
	 */
	// modifieds maxLevel, maxLevelMultiplier
	private ILevelModifier levelModifier;

	// max inventory sizes
	private int maxSocketSize;
	private int maxImbueSize;
	private int maxInnateSize;

	public static class SortByLevel implements Comparator<ICharmEntity> {
		@Override
		public int compare(ICharmEntity e1, ICharmEntity e2) {
			return e1.getCharm().getLevel() - e2.getCharm().getLevel();
		}
	};

	// comparator on charm level
	public static Comparator<ICharmEntity> levelComparator = new SortByLevel();

	/**
	 * this method is added to allow method referencing to create capability in the registration event for capabilities.
	 * @return
	 */
	public static CharmableCapability create() {
		return new CharmableCapability(0, 0, 0);
	}

	public CharmableCapability(int maxInnateSize, int maxImbueSize, int maxSocketSize) {
		this.maxInnateSize = maxInnateSize;
		this.maxImbueSize = maxImbueSize;
		this.maxSocketSize = maxSocketSize;
	}

	/**
	 * 
	 * @param magicsCap
	 * @param builder
	 */
	public CharmableCapability(Builder builder) {
		this.source = builder.source;
		this.executing = builder.executing;
		this.bindable = builder.bindable;
		this.innate = builder.innate;
		this.imbuing = builder.imbuing;
		this.imbuable = builder.imbuable;
		this.socketable = builder.socketable;
		this.baseMaterial = builder.baseMaterial;
		this.sourceItem = builder.sourceItem;
		this.namedByCharm = builder.namedByCharm;
		this.namedByMaterial = builder.namedByMaterial;
		this.levelModifier = builder.levelModifier;
		this.maxInnateSize = builder.maxInnateSize;
		this.maxImbueSize = builder.maxImbueSize;
		this.maxSocketSize = builder.maxSocketSize;
	}

	@Override
	public boolean isCharmed() {
		if (charmEntities.values().size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param charm
	 * @return
	 */
	@Override
	public boolean contains(ICharm charm) {
		for (ICharmEntity entity : charmEntities.values()) {
			if (entity.getCharm().getType().equalsIgnoreCase(charm.getType()) ||
					entity.getCharm().getName().equals(charm.getName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Duplicate the capability into a destination capability.
	 */
	@Override
	public void copyTo(ItemStack stack) {

		if (stack.hasCapability(TreasureCapabilities.CHARMABLE, null)) {
			ICharmableCapability cap = stack.getCapability(TreasureCapabilities.CHARMABLE, null);
			// copy all capability properties over (they may be altered by existing runestones)
			cap.setBaseMaterial(getBaseMaterial());
			cap.setBindable(isBindable());
			cap.setExecuting(isExecuting());
			cap.setImbuable(isImbuable());
			cap.setImbuing(isImbuing());
			cap.setInnate(isInnate());
			cap.setLevelModifier(getLevelModifier());
			cap.setNamedByCharm(isNamedByCharm());
			cap.setNamedByMaterial(isNamedByMaterial());
			cap.setSocketable(isSocketable());
			cap.setSource(isSource());
			cap.setSourceItem(getSourceItem());
			cap.setMaxSocketSize(getMaxSocketSize());
			getCharmEntities().forEach((type, entity) -> {
				// duplicate charm
				ICharmEntity newEntity = entity.getCharm().createEntity(entity);
				cap.add(type, newEntity);
			});
		}
	}

	/**
	 * Transfer the charms from a source capability to a destination capability.
	 */
	@Override
	public void transferTo(ItemStack dest, InventoryType sourceType, InventoryType destType) {
		if (dest.hasCapability(TreasureCapabilities.CHARMABLE, null)) {
			ICharmableCapability cap = dest.getCapability(TreasureCapabilities.CHARMABLE, null);
			List<ICharmEntity> charms = (List<ICharmEntity>) getCharmEntities().get(sourceType);
			// sort the charms from highest to lowest so highest are copied first if room enough for them
			// NOTE mote for charm items as they can only contain 1 charm effect
			Comparator<ICharmEntity> comparator = Collections.reverseOrder(new CharmableCapability.SortByLevel());						
			Collections.sort(charms, comparator);
			// process each charm entity (only innate as charmItems can have innate)
			for (ICharmEntity entity : charms) {
				if (cap.getMaxCharmLevel() >= entity.getCharm().getLevel()) {
					if (cap.getCurrentSize(destType) < cap.getMaxSize(destType)) {
						ICharmEntity newEntity = entity.getCharm().createEntity(entity);
						cap.add(destType, newEntity);
					}
				}						
			}
		}
	}
	
	/**
	 * 
	 */
	@Override
	public boolean hasCharmType(ItemStack source, ItemStack dest, InventoryType sourceType, InventoryType destType) {
		if (source.hasCapability(TreasureCapabilities.CHARMABLE, null) && dest.hasCapability(TreasureCapabilities.CHARMABLE, null)) {
			ICharmableCapability sourceCap = source.getCapability(TreasureCapabilities.CHARMABLE, null);
			ICharmableCapability destCap = dest.getCapability(TreasureCapabilities.CHARMABLE, null);

			// faster and simpler than lambdas
			for (ICharmEntity sourceEntity : sourceCap.getCharmEntities().get(sourceType)) {
				for (ICharmEntity destEntity : destCap.getCharmEntities().get(destType)) {
					if (destEntity.getCharm().getType().equals(sourceEntity.getCharm().getType())) {
						return true;
					}
				}
			}
		}
		return false;
	}
	

	@Override
	public int getCurrentSize(InventoryType type) {
		return getCharmEntities().get(type).size();
	}

	/**
	 * Convenience method
	 * @param type
	 * @return
	 */
	@Override
	public int getMaxSize(InventoryType type) {
		// check against SOCKET first as this will be the most common
		//		return (type == InventoryType.SOCKET ? getMagicsCap().getMaxSocketSize() : type == InventoryType.IMBUE ? getMagicsCap().getMaxImbueSize() : getMagicsCap().getMaxInnateSize());
		return (type == InventoryType.SOCKET ? getMaxSocketSize() : type == InventoryType.IMBUE ? getMaxImbueSize() : getMaxInnateSize());

	}

	/**
	 * Convenience method.
	 * @param type
	 * @param entity
	 */
	@Override
	public void add(InventoryType type, ICharmEntity entity) {

		// TODO ensure only one of the same type can be added.

		// test if there is enough space to add
		if (getCurrentSize(type) < getMaxSize(type)) {
			charmEntities.get(type).add(entity);

			// record highest level charm
			if (highestLevel == null || entity.getCharm().getLevel() > highestLevel.getCharm().getLevel()) {
				highestLevel = entity;
			}
		}
	}

	/**
	 * 
	 * @param type
	 * @param index
	 */
	@Override
	public void remove(InventoryType type, int index) {
		((List<ICharmEntity>) getCharmEntities().get(type)).remove(index);
		// recalc highest level
		highestLevel = null;
		getCharmEntities().values().forEach(entity -> {
			if (highestLevel == null || entity.getCharm().getLevel() > highestLevel.getCharm().getLevel()) {
				highestLevel = entity;
			}
		});
	}

	@Override
	public void clearCharms() {
		// clear the charm inventory
		Treasure.LOGGER.debug("clearing charms");
		getCharmEntities().clear();
	}

	/**
	 * Ex.
	 * GOLD + Sapphire = g.base=8 + (g.modifier=1 * s.base=12) = 20
	 * BLACK + Black Pearl = b.base=12 + (b.modifer=1.15 * bp.base=12) = 25
	 */
	@Override
	public int getMaxCharmLevel() {
		Optional<CharmableMaterial> base = TreasureCharmableMaterials.getBaseMaterial(baseMaterial);
		Optional<CharmableMaterial> source = TreasureCharmableMaterials.getSourceItem(sourceItem);
		CharmableMaterial effectiveBase = base.isPresent() ? base.get() : TreasureCharmableMaterials.COPPER;
		return levelModifier.modifyMaxLevel(effectiveBase.getMaxLevel())
				+ (int) Math.floor(levelModifier.modifyLevelMultiplier(effectiveBase.getLevelMultiplier()) * (source.isPresent() ? source.get().getMaxLevel() : 0));
	}

	@Override
	public void appendHoverText(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		if (isImbuable() || isSocketable()) {
			tooltip.add(TextFormatting.DARK_AQUA + I18n.translateToLocalFormatted("tooltip.label.charms", getMaxCharmLevel()));
		}
		else {
			tooltip.add(TextFormatting.DARK_AQUA + I18n.translateToLocalFormatted("tooltip.label.charms.no_level", getMaxCharmLevel()));
		}

		// create header text for inventory type
		appendHoverText(stack, world, tooltip, flag, InventoryType.INNATE, false);
		appendHoverText(stack, world, tooltip, flag, InventoryType.IMBUE, true);
		appendHoverText(stack, world, tooltip, flag, InventoryType.SOCKET, true);
	}

	/**
	 * 
	 * @param stack
	 * @param world
	 * @param tooltip
	 * @param flag
	 * @param inventoryType
	 * @param titleFlag
	 */
	@Override
	public void appendHoverText(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag, InventoryType inventoryType, boolean titleFlag) {		
		List<ICharmEntity> entityList = (List<ICharmEntity>) getCharmEntities().get(inventoryType);
		// test if the cap has the inventory type ability
		switch (inventoryType) {
		case INNATE: if (!isInnate()) { return;}; break;
		case IMBUE: if (!isImbuable()) { return;}; break;
		case SOCKET: if (!isSocketable()) { return;}; break;
		}

		// add title
		if (titleFlag) {
			tooltip.add(TextFormatting.GOLD + 
					I18n.translateToLocalFormatted("tooltip.indent1", I18n.translateToLocal("tooltip.charmable.inventory." + inventoryType.name().toLowerCase()))
			+ TextFormatting.WHITE + getCapacityHoverText(stack, world, inventoryType));
		}
		// add charms
		for (ICharmEntity entity : entityList) {
//			Treasure.logger.debug("entity -> {}", entity);
			entity.getCharm().addInformation(stack, world, tooltip, flag, entity, inventoryType);
		}	
	}

	@Override
	@SuppressWarnings("deprecation")
	public String getCapacityHoverText(ItemStack stack, World world, InventoryType type) {	
		return I18n.translateToLocalFormatted("tooltip.charmable.slots", 				
				String.valueOf(Math.toIntExact(Math.round(getCurrentSize(type)))), // used
				String.valueOf(Math.toIntExact(Math.round(getMaxSize(type))))); // max				
	}

	@Override
	public void addMaxSocketSize(int increment) {
		this.maxSocketSize = getMaxSocketSize() + increment;
	}
	
	/*
	 * 
	 */
	public static class Builder {
		public boolean source;
		public boolean executing = true;
		public boolean bindable;
		public boolean socketable;
		public boolean imbuing;
		public boolean imbuable;
		public boolean innate;	    	    
		public boolean namedByMaterial;
		public boolean namedByCharm;
		public ResourceLocation baseMaterial = TreasureCharmableMaterials.COPPER.getName();
		public ResourceLocation sourceItem = Items.AIR.getRegistryName();
		public ILevelModifier levelModifier = new NoLevelModifier();
		// max inventory sizes
		private int maxSocketSize;
		private int maxImbueSize;
		private int maxInnateSize;

		public Builder(int maxInnateSize, int maxImbueSize, int maxSocketSize) {
			this.maxInnateSize = maxInnateSize;
			this.maxImbueSize = maxImbueSize;
			this.maxSocketSize = maxSocketSize;
		}

		public Builder with(Consumer<Builder> builder) {
			builder.accept(this);
			return this;
		}

		public ICharmableCapability build() {
			return new CharmableCapability(this);
		}
	}

	@Override
	public Multimap<InventoryType, ICharmEntity> getCharmEntities() {
		return charmEntities;
	}

	public void setCharmEntities(Multimap<InventoryType, ICharmEntity> charmEntities) {
		this.charmEntities = charmEntities;
	}

	@Override
	public boolean isSource() {
		return source;
	}

	@Override
	public void setSource(boolean source) {
		this.source = source;
	}

	@Override
	public boolean isExecuting() {
		return executing;
	}

	@Override
	public void setExecuting(boolean executing) {
		this.executing = executing;
	}

	@Override
	public boolean isBindable() {
		return bindable;
	}

	@Override
	public void setBindable(boolean bindable) {
		this.bindable = bindable;
	}

	@Override
	public boolean isSocketable() {
		return socketable;
	}

	@Override
	public void setSocketable(boolean socketable) {
		this.socketable = socketable;
	}

	@Override
	public boolean isImbuing() {
		return imbuing;
	}

	@Override
	public void setImbuing(boolean imbuing) {
		this.imbuing = imbuing;
	}

	@Override
	public boolean isImbuable() {
		return imbuable;
	}

	@Override
	public void setImbuable(boolean imbuable) {
		this.imbuable = imbuable;
	}

	@Override
	public boolean isInnate() {
		return innate;
	}

	@Override
	public void setInnate(boolean innate) {
		this.innate = innate;
	}

	public ResourceLocation getBaseMaterial() {
		return baseMaterial;
	}

	public void setBaseMaterial(ResourceLocation baseMaterial) {
		this.baseMaterial = baseMaterial;
	}

	public ResourceLocation getSourceItem() {
		return sourceItem;
	}

	public void setSourceItem(ResourceLocation sourceItem) {
		this.sourceItem = sourceItem;
	}

	@Override
	public ICharmEntity getHighestLevel() {
		return highestLevel;
	}

	@Override
	public void setHighestLevel(ICharmEntity highestLevel) {
		this.highestLevel = highestLevel;
	}

	public boolean isNamedByMaterial() {
		return namedByMaterial;
	}

	public void setNamedByMaterial(boolean namedByMaterial) {
		this.namedByMaterial = namedByMaterial;
	}

	public boolean isNamedByCharm() {
		return namedByCharm;
	}

	public void setNamedByCharm(boolean namedByCharm) {
		this.namedByCharm = namedByCharm;
	}

	public void setMaxSocketSize(int size) {
		this.maxSocketSize = size;
	}
	
	public int getMaxSocketSize() {
		return maxSocketSize;
	}

	public int getMaxImbueSize() {
		return maxImbueSize;
	}

	public int getMaxInnateSize() {
		return maxInnateSize;
	}

	@Override
	public int getSocketSize() {
		return getCharmEntities().get(InventoryType.SOCKET).size();
	}

	@Override
	public int getImbueSize() {
		return getCharmEntities().get(InventoryType.IMBUE).size();
	}

	@Override
	public int getInnateSize() {
		return getCharmEntities().get(InventoryType.INNATE).size();
	}

	@Override
	public ILevelModifier getLevelModifier() {
		return levelModifier;
	}

	@Override
	public void setLevelModifier(ILevelModifier levelModifier) {
		this.levelModifier = levelModifier;
	}


}
