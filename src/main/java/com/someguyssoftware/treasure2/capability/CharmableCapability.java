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
import java.util.List;
import java.util.function.Consumer;

import com.someguyssoftware.treasure2.charm.ICharmEntity;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

/**
 * The CharmableCapability provides any item with a Charm Inventory, which is a datatype of  Map<CharmType>, List<Charm>>.
 * The Charm Inventory can support three types: FINITE, IMBUE or SOCKET.
 * FINITE - Charms that are added on Item spawn and can not be renewed. Once expired, the charm is removed and the space is decremented.
 * IMBUE - Charms that can be added via Books, or any IMBUING item.
 * SOCKET - Charms that can be added to sockets via Coins or Gems, or any BINDING item. 
 * @author Mark Gottschling on Apr 27, 2020
 *
 */
public class CharmableCapability implements ICharmableCapability {
//    private LinkedList<List<ICharmEntity>> charmEntities = new LinkedList<>();
    @SuppressWarnings("unchecked")
	ArrayList<ICharmEntity>[] charmEntities = (ArrayList<ICharmEntity>[])new ArrayList[3];
    private boolean source;
    private boolean bindable;
    private boolean socketing;
    private boolean socketable;
    private boolean imbuing;
    private boolean imbuable;
    private boolean finite;
    
    private int socketSize;
    private int imbueSize;
    private int finiteSize;
    
	/**
	 * 
	 */
	public CharmableCapability() {
	}
	
	/**
	 * 
	 * @param builder
	 */
	public CharmableCapability(Builder builder) {
		init();
		this.finite = builder.finite;
		this.finiteSize = finite ? Math.max(1, builder.finiteSize) : 0;
		this.socketable = builder.socketable;
		this.socketSize = socketable ? Math.max(1, builder.socketSize) : 0;
	}
		
	protected void init() {
		charmEntities[CharmInventoryType.FINITE.value] = new ArrayList<>(1);
		charmEntities[CharmInventoryType.IMBUE.value] =  new ArrayList<>(1);
		charmEntities[CharmInventoryType.SOCKET.value] = new ArrayList<>(1);
	}
	
	/**
	 * Convenience method.
	 * @param type
	 * @param entity
	 */
	@Override
	public void add(CharmInventoryType type, ICharmEntity entity) {
		charmEntities[type.value].add(entity);
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
	
	@Override
	public void appendHoverText(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
	    tooltip.add(new TranslationTextComponent("tooltip.label.charmed").withStyle(TextFormatting.GOLD, TextFormatting.ITALIC));
		tooltip.add(new TranslationTextComponent("tooltip.label.charms").withStyle(TextFormatting.YELLOW, TextFormatting.BOLD));

		// create header text for inventory type
		appendHoverText(stack, world, tooltip, flag, CharmInventoryType.FINITE, false);
		appendHoverText(stack, world, tooltip, flag, CharmInventoryType.IMBUE, true);
		appendHoverText(stack, world, tooltip, flag, CharmInventoryType.SOCKET, true);
	}
	
	private void appendHoverText(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag, CharmInventoryType inventoryType, boolean titleFlag) {
		List<ICharmEntity> entityList = getCharmEntities()[inventoryType.value];
		if (entityList != null && !entityList.isEmpty()) {
			// add title
			if (titleFlag) {
				tooltip.add(new TranslationTextComponent("tooltip.label.charm.type.finite", inventoryType.name()).withStyle(TextFormatting.DARK_GRAY));
			}
			// add charms
			for (ICharmEntity entity : entityList) {
				entity.getCharm().appendHoverText(stack, world, tooltip, flag, entity);
			}
		}		
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

	public boolean isBindable() {
		return bindable;
	}

	public void setBindable(boolean bindable) {
		this.bindable = bindable;
	}

	public boolean isSocketing() {
		return socketing;
	}

	public void setSocketing(boolean socketing) {
		this.socketing = socketing;
	}

	public boolean isSocketable() {
		return socketable;
	}

	public void setSocketable(boolean socketable) {
		this.socketable = socketable;
	}

	public boolean isImbuing() {
		return imbuing;
	}

	public void setImbuing(boolean imbuing) {
		this.imbuing = imbuing;
	}

	public boolean isImbuable() {
		return imbuable;
	}

	public void setImbuable(boolean imbuable) {
		this.imbuable = imbuable;
	}

	public boolean isFinite() {
		return finite;
	}

	public void setFinite(boolean finite) {
		this.finite = finite;
	}
	
	/**
	 * 
	 * @author Mark Gottschling on Aug 15, 2021
	 *
	 */
	public enum CharmInventoryType {
		FINITE(0),
		IMBUE(1),
		SOCKET(2);
		
		int value;
		
		CharmInventoryType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
	
	public static class Builder {
	    private boolean source;
	    private boolean bindable;
	    private boolean socketing;
	    public boolean socketable;
	    private boolean imbuing;
	    private boolean imbuable;
	    public boolean finite;
	    	    
	    private int socketSize;
	    private int imbueSize;
	    public int finiteSize;
	    
	    public Builder() {}
	    
	    public Builder with(Consumer<Builder> builder) {
	    	builder.accept(this);
	    	return this;
	    }
	    
	    public Builder socketable(boolean socketable, int size) {
	    	this.socketable = socketable;
	    	this.socketSize = size;
	    	return this;
	    }
	    
	    public Builder socketing(boolean socketing) {
	    	this.socketing = socketing;
	    	return this;
	    }
	    
	    public Builder finite(boolean finite, int size) {
	    	this.finite = finite;
	    	this.finiteSize = size;
	    	return this;
	    }
	    
	    public Builder imbue(boolean imbue, int size) {
	    	this.imbuable = imbue;
	    	this.imbueSize = size;
	    	return this;
	    }
	    
	    public Builder imbuing(boolean imbuing) {
	    	this.imbuing = imbuing;
	    	return this;
	    }
	    public ICharmableCapability build() {
	    	return new CharmableCapability(this);
	    }
	}
}