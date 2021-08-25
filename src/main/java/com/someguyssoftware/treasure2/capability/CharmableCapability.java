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

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import com.someguyssoftware.treasure2.charm.CharmableMaterial;
import com.someguyssoftware.treasure2.charm.ICharmEntity;
import com.someguyssoftware.treasure2.charm.TreasureCharms;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

/**
 * The CharmableCapability provides any item with a Charm Inventory, which is a datatype of  Map<CharmType>, List<Charm>>.
 * The Charm Inventory can support three types: INNATE, IMBUE or SOCKET.
 * INNATE - Charms that are added on Item spawn and can not be renewed. Once expired, the charm is removed and the space is decremented.
 * IMBUE - Charms that can be added via Books, or any IMBUING item.
 * SOCKET - Charms that can be added to sockets via Coins or Gems, or any BINDING item. 
 * @author Mark Gottschling on Apr 27, 2020
 *
 */
public class CharmableCapability implements ICharmableCapability {
	/*
	 * Properties that refer to the Item that has this capability
	 */
    @SuppressWarnings("unchecked")
	ArrayList<ICharmEntity>[] charmEntities = (ArrayList<ICharmEntity>[])new ArrayList[3];
    // is this item a charm source/originator ie. not an adornment or an item that is "given" a charm
    private boolean source;
    // is this item bindable to a target item that is socketable
    private boolean bindable;

    // does this item have sockets - accepts bindable items
    private boolean socketable;
    // can this item imbue atarget item
    private boolean imbuing;
    // can this item be imbued
    private boolean imbuable;
    // does this item have "built-in" innate charms
    private boolean innate;
    
    // the base material this item is made of
    private ResourceLocation baseMaterial;
    // the item that this capability belongs to
    private ResourceLocation sourceItem;
    
    /*
     * Propeties that refer to the Charm Inventory the the Item that has this capability
     */
    private int maxSocketsSize;
    private int maxImbueSize;
    private int maxInnateSize;

    
	/**
	 * 
	 */
	public CharmableCapability() {
		init();
	}
	
	/**
	 * 
	 * @param builder
	 */
	public CharmableCapability(Builder builder) {
		this();
		this.source = builder.source;
		this.bindable = builder.bindable;
		this.innate = builder.innate;
		this.maxInnateSize = innate ? Math.max(1, builder.maxInnateSize) : 0;
		this.imbuable = builder.imbuable;
		this.maxImbueSize = imbuable ? Math.max(1, builder.maxImbueSize) : 0;
		this.socketable = builder.socketable;
		this.maxSocketsSize = socketable ? Math.max(1, builder.maxSocketsSize) : 0;
		this.baseMaterial = builder.baseMaterial;
		this.sourceItem = builder.sourceItem;
	}
		
	/**
	 * 
	 */
	protected void init() {
		charmEntities[InventoryType.INNATE.value] = new ArrayList<>(1);
		charmEntities[InventoryType.IMBUE.value] =  new ArrayList<>(1);
		charmEntities[InventoryType.SOCKET.value] = new ArrayList<>(1);
	}
	
	/**
	 * Convenience method.
	 * @param type
	 * @param entity
	 */
	@Override
	public void add(InventoryType type, ICharmEntity entity) {
		// test if the level of charm item/cap is >= entity.level; else return
		
		// test if there is enough space to add
//		Treasure.LOGGER.debug("adding type -> {} charm -> {}", type, entity.getCharm());
		if (charmEntities[type.value].size() < getMaxSize(type)) {
			charmEntities[type.value].add(entity);
		}
//		Treasure.LOGGER.debug("ther are {}  type -> {} charms", charmEntities[type.value].size(), type);
	}
	
	/**
	 * Convenience method
	 * @param type
	 * @return
	 */
	public int getMaxSize(InventoryType type) {
		// check against SOCKET first as this will be the most common
		return (type == InventoryType.SOCKET ? getMaxSocketsSize() : type == InventoryType.IMBUE ? getMaxImbueSize() : getMaxInnateSize());
	}
	
	@Override
	public boolean isCharmed() {
		int size = 0;
		for (List<ICharmEntity> list : charmEntities) {
			if (list != null) {
				size += list.size();
			}
		}
		if (size > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 */
	@Override
	public int getMaxCharmLevel() {
		Optional<CharmableMaterial> base = TreasureCharms.getBaseMaterial(baseMaterial);
		Optional<CharmableMaterial> source = TreasureCharms.getSourceItem(sourceItem);
		CharmableMaterial effectiveBase = base.isPresent() ? base.get() : TreasureCharms.COPPER;
		return effectiveBase.getMaxLevel() + (int) Math.floor(effectiveBase.getLevelMultiplier() * (source.isPresent() ? source.get().getMaxLevel() : 0));
	}
	
	@Override
	public void appendHoverText(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
	    tooltip.add(new TranslationTextComponent("tooltip.label.charmed").withStyle(TextFormatting.GOLD, TextFormatting.ITALIC));
		tooltip.add(new TranslationTextComponent("tooltip.label.charms").withStyle(TextFormatting.YELLOW, TextFormatting.BOLD));

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
	private void appendHoverText(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag, InventoryType inventoryType, boolean titleFlag) {
		List<ICharmEntity> entityList = getCharmEntities()[inventoryType.value];
		if (entityList != null && !entityList.isEmpty()) {
			// add title
			if (titleFlag) {
				TextFormatting color = inventoryType == InventoryType.SOCKET ? TextFormatting.BLUE : TextFormatting.DARK_RED;
				tooltip.add(
						new TranslationTextComponent("tooltip.label.charm.type." + inventoryType.name().toLowerCase()).withStyle(color)
							.append(getCapacityHoverText(stack, world, entityList).withStyle(TextFormatting.WHITE))
						);
			}
			// add charms
			for (ICharmEntity entity : entityList) {
				entity.getCharm().appendHoverText(stack, world, tooltip, flag, entity);
			}
		}		
	}

    @SuppressWarnings("deprecation")
	public void appendCapacityHoverText(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag, List<ICharmEntity> entities) {
	
//    	tooltip.add(new TranslationTextComponent("tooltip.label.charmable.slots").withStyle(TextFormatting.GRAY));
		tooltip.add(new TranslationTextComponent("tooltip.label.charmable.slots", 
				String.valueOf(Math.toIntExact(Math.round(entities.size()))), // used
				String.valueOf(Math.toIntExact(Math.round(this.maxSocketsSize)))) // max
				.withStyle(TextFormatting.WHITE)); 
    }
    
    @SuppressWarnings("deprecation")
	public TranslationTextComponent getCapacityHoverText(ItemStack stack, World world, List<ICharmEntity> entities) {	
    	return new TranslationTextComponent("tooltip.label.charmable.slots", 
				String.valueOf(Math.toIntExact(Math.round(entities.size()))), // used
				String.valueOf(Math.toIntExact(Math.round(this.maxSocketsSize)))); // max				
    }
    
	@Override
	public List<ICharmEntity>[] getCharmEntities() {
		if (charmEntities == null) {
			this.charmEntities = (ArrayList<ICharmEntity>[])new ArrayList[3];
		}
		return charmEntities;
	}

	@Override
	public void setCharmEntities(List<ICharmEntity>[] entities) {
		this.charmEntities = (ArrayList<ICharmEntity>[]) entities;
	}

	public boolean isSource() {
		return source;
	}

	public void setSource(boolean source) {
		this.source = source;
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
	
	@Override
	public int getMaxInnateSize() {
		return maxInnateSize;
	}
	
	@Override
	public int getMaxSocketsSize() {
		return maxSocketsSize;
	}

	@Override
	public int getMaxImbueSize() {
		return maxImbueSize;
	}	

	@Override
	public void setMaxSocketsSize(int maxSocketsSize) {
		this.maxSocketsSize = maxSocketsSize;
	}

	@Override
	public void setMaxImbueSize(int maxImbueSize) {
		this.maxImbueSize = maxImbueSize;
	}

	@Override
	public void setMaxInnateSize(int maxInnateSize) {
		this.maxInnateSize = maxInnateSize;
	}
	
	@Override
	public ResourceLocation getBaseMaterial() {
		return baseMaterial;
	}

	@Override
	public void setBaseMaterial(ResourceLocation baseMaterial) {
		this.baseMaterial = baseMaterial;
	}
	
	@Override
	public ResourceLocation getSourceItem() {
		return sourceItem;
	}
	@Override
	public void setSourceItem(ResourceLocation sourceItem) {
		this.sourceItem = sourceItem;
	}
	
	/**
	 * 
	 * @author Mark Gottschling on Aug 15, 2021
	 *
	 */
	public enum InventoryType {
		INNATE(0),
		IMBUE(1),
		SOCKET(2);
		
		private static final Map<Integer, InventoryType> values = new HashMap<Integer, InventoryType>();
		Integer value;
		
		// setup reverse lookup
		static {
			for (InventoryType x : EnumSet.allOf(InventoryType.class)) {
				values.put(x.getValue(), x);
			}
		}
		
		InventoryType(Integer value) {
			this.value = value;
		}

		public Integer getValue() {
			return value;
		}
		
		/**
		 * 
		 * @param value
		 * @return
		 */
		public static InventoryType getByValue(Integer value) {
			return (InventoryType) values.get(value);
		}
	}
		
	/**
	 * 
	 * @author Mark Gottschling on Aug 16, 2021
	 *
	 */
	public static class Builder {
	    private boolean source;
	    public boolean bindable;
	    public boolean socketable;
	    public boolean imbuing;
	    public boolean imbuable;
	    public boolean innate;	    	    

	    public ResourceLocation baseMaterial = TreasureCharms.COPPER.getName();
	    
	    // required property
	    public ResourceLocation sourceItem;
	    
	    public int maxSocketsSize;
	    public int maxImbueSize;
	    public int maxInnateSize;
	    
	    // required to pass the source Item here
	    public Builder(ResourceLocation sourceItem) {
	    	this.sourceItem = sourceItem;
	    }
	    
	    public Builder with(Consumer<Builder> builder) {
	    	builder.accept(this);
	    	return this;
	    }
	    
	    public Builder source(boolean source) {
	    	this.source = source;
	    	return this;
	    }
	    
		public Builder bindable(boolean bindable) {
			this.bindable = bindable;
			return this;
		}
		
	    public Builder socketable(boolean socketable, int size) {
	    	this.socketable = socketable;
	    	this.maxSocketsSize = size;
	    	return this;
	    }
	    
	    public Builder innate(boolean innate, int size) {
	    	this.innate = innate;
	    	this.maxInnateSize = size;
	    	return this;
	    }
	    
	    public Builder imbue(boolean imbue, int size) {
	    	this.imbuable = imbue;
	    	this.maxImbueSize = size;
	    	return this;
	    }
	    
	    public Builder imbuing(boolean imbuing) {
	    	this.imbuing = imbuing;
	    	return this;
	    }
	    
	    public Builder baseMaterial(ResourceLocation material) {
	    	this.baseMaterial = material;
	    	return this;
	    }
	    
	    public ICharmableCapability build() {
	    	// calculate the max charm level based on baseMaterial and the source item.
//	    	Treasure.LOGGER.debug("charm source item -> {}", sourceItem);
//	    	Optional<Integer> level = TreasureCharms.getCharmLevel(sourceItem);
//	    	this.maxCharmLevel = baseMaterial.getMaxLevel() + (level.isPresent() ? level.get() : 0) ;
	    	return new CharmableCapability(this);
	    }
	}
}