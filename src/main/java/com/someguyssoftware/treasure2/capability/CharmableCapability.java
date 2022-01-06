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

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.someguyssoftware.treasure2.capability.MagicsInventoryCapability.InventoryType;
import com.someguyssoftware.treasure2.capability.modifier.ILevelModifier;
import com.someguyssoftware.treasure2.capability.modifier.NoLevelModifier;
import com.someguyssoftware.treasure2.charm.ICharm;
import com.someguyssoftware.treasure2.charm.ICharmEntity;
import com.someguyssoftware.treasure2.charm.TreasureCharms;
import com.someguyssoftware.treasure2.material.CharmableMaterial;
import com.someguyssoftware.treasure2.material.TreasureCharmableMaterials;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
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
public class CharmableCapability implements ICharmableCapability { // TODO this could extend an AbstractMagicsSupport which implements IMagicsInventorySupport
	
	// TODO requies a reference back to the hosting Item because some methods (ex getMaxLevel) required additional computation that is provided from the Item ex. AdornmentSize.
	// TODO probably need to extend this class for Adornments, in order to provide the addtional functionality.
	
	/*
	 * Properties that refer to the Item that has this capability
	 */
	Multimap<InventoryType, ICharmEntity> charmEntities = ArrayListMultimap.create();

	private IMagicsInventoryCapability magicsCap;

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
	// the current charm with the highest level
	private ICharmEntity highestLevel;
	
	// can this item be named by its material components ex. Gold Ring, Topaz Gold Ring
	private boolean namedByMaterial;
	// can this item be named by its charms and levels ex. Giant Ring of Healing
	private boolean namedByCharm;
	
	/*
	 * modifiers
	 */
	private ILevelModifier levelModifier;
	
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
		return new CharmableCapability(new MagicsInventoryCapability());
	}
	
	/**
	 * 
	 * @param magicsCap
	 */
	public CharmableCapability(IMagicsInventoryCapability magicsCap) {
		this.magicsCap = magicsCap;
	}

	/**
	 * 
	 * @param magicsCap
	 * @param builder
	 */
	public CharmableCapability(Builder builder) {
		this.magicsCap = builder.magicsCap;
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

	@Override
	public int getCurrentSize(InventoryType type) {
		// check against SOCKET first as this will be the most common
		return (type == InventoryType.SOCKET ? getMagicsCap().getSocketSize() : type == InventoryType.IMBUE ? getMagicsCap().getImbueSize() : getMagicsCap().getInnateSize());		
	}
	
	/**
	 * Convenience method
	 * @param type
	 * @return
	 */
	@Override
	public int getMaxSize(InventoryType type) {
		// check against SOCKET first as this will be the most common
		return (type == InventoryType.SOCKET ? getMagicsCap().getMaxSocketSize() : type == InventoryType.IMBUE ? getMagicsCap().getMaxImbueSize() : getMagicsCap().getMaxInnateSize());
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
		//		Treasure.LOGGER.debug("adding type -> {} charm -> {}", type, entity.getCharm());
		if (getCurrentSize(type) < getMaxSize(type)) {
			charmEntities.get(type).add(entity);
			
			// record highest level charm
			if (highestLevel == null || entity.getCharm().getLevel() > highestLevel.getCharm().getLevel()) {
				highestLevel = entity;
			}
		}
		//		Treasure.LOGGER.debug("ther are {}  type -> {} charms", charmEntities[type.value].size(), type);
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
	
	/**
	 * 
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
			tooltip.add(TextFormatting.YELLOW + I18n.translateToLocalFormatted("tooltip.label.charms", getMaxCharmLevel()));
		}
		else {
			tooltip.add(TextFormatting.YELLOW + I18n.translateToLocalFormatted("tooltip.label.charms.no_level", getMaxCharmLevel()));
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
	private void appendHoverText(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag, InventoryType inventoryType, boolean titleFlag) {		
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
			entity.getCharm().addInformation(stack, world, tooltip, flag, entity);
		}	
	}
	
	// TODO check this out - it is probably wrong  since it is grabbing the sockets size always (should be grabbinb imbue size if imbue?)
	@SuppressWarnings("deprecation")
	public String getCapacityHoverText(ItemStack stack, World world, InventoryType type) {	
		return I18n.translateToLocalFormatted("tooltip.charmable.slots", 				
				String.valueOf(Math.toIntExact(Math.round(getCurrentSize(type)))), // used
				String.valueOf(Math.toIntExact(Math.round(getMaxSize(type))))); // max				
	}
	
	/*
	 * 
	 */
	public static class Builder {
		private IMagicsInventoryCapability magicsCap;
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
		
		// required to pass the source Item here
		public Builder(IMagicsInventoryCapability magicsCap) {
			this.magicsCap = magicsCap;
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
	public IMagicsInventoryCapability getMagicsCap() {
		return magicsCap;
	}

	protected void setMagicsCap(IMagicsInventoryCapability magicsCap) {
		this.magicsCap = magicsCap;
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

	@Override
	public int getMaxSocketSize() {
		return magicsCap.getMaxSocketSize();
	}

	@Override
	public int getMaxImbueSize() {
		return magicsCap.getMaxImbueSize();
	}

	@Override
	public int getMaxInnateSize() {
		return magicsCap.getMaxInnateSize();
	}

	@Override
	public int getSocketSize() {
		return magicsCap.getSocketSize();
	}

	@Override
	public int getImbueSize() {
		return magicsCap.getImbueSize();
	}

	@Override
	public int getInnateSize() {
		return magicsCap.getInnateSize();
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
