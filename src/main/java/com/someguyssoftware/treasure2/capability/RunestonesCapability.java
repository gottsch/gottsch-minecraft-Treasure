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

import static com.someguyssoftware.treasure2.capability.TreasureCapabilities.RUNESTONES;

import java.util.List;
import java.util.function.Consumer;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.runestone.IRunestone;
import com.someguyssoftware.treasure2.runestone.IRunestoneEntity;
import com.someguyssoftware.treasure2.runestone.RunestoneEntity;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Jan 14, 2022
 *
 */
public class RunestonesCapability implements IRunestonesCapability {
	/*
	 * Properties that refer to the Item that has this capability
	 */
	Multimap<InventoryType, IRunestoneEntity> runestoneEntities = ArrayListMultimap.create();

	// is this item bindable to a target item that is socketable
	private boolean bindable;
	// does this item have sockets - accepts bindable items
	private boolean socketable;
	
	// max inventory sizes
	private int maxSocketSize;
	private int maxImbueSize;
	private int maxInnateSize;
			
	public RunestonesCapability(int maxInnateSize, int maxImbueSize, int maxSocketSize) {
		this.maxInnateSize = maxInnateSize;
		this.maxImbueSize = maxImbueSize;
		this.maxSocketSize = maxSocketSize;
	}
	
	/**
	 * 
	 * @param builder
	 */
	public RunestonesCapability(Builder builder) {
//		this.magicsCap = builder.magicsCap;
		this.bindable = builder.bindable;
		this.socketable = builder.socketable;
		this.maxInnateSize = builder.maxInnateSize;
		this.maxImbueSize = builder.maxImbueSize;
		this.maxSocketSize = builder.maxSocketSize;
	}
	
	/**
	 * this method is added to allow method referencing to create capability in the registration event for capabilities.
	 * @return
	 */
	public static RunestonesCapability create() {
		return new RunestonesCapability(0, 0, 0);
	}

	@Override
	public void appendHoverText(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		tooltip.add(TextFormatting.DARK_PURPLE + I18n.translateToLocal("tooltip.runestones.title"));
		
		// create header text for inventory type
		appendHoverText(stack, world, tooltip, flag, InventoryType.SOCKET, true);
	}
	
	
	public void appendHoverText(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag, InventoryType inventoryType, boolean titleFlag) {		
		List<IRunestoneEntity> entityList = (List<IRunestoneEntity>) getEntities(inventoryType);
		// test if the cap has the inventory type ability
		switch (inventoryType) {
			case SOCKET: if (!isSocketable()) { return;}; break;
		default:
			return;
		}

		// add title
		if (titleFlag) {
			tooltip.add(TextFormatting.GOLD + 
					I18n.translateToLocalFormatted("tooltip.indent1", I18n.translateToLocal("tooltip.runestones.inventory." + inventoryType.name().toLowerCase()))
					+ TextFormatting.WHITE + getCapacityHoverText(stack, world, inventoryType));
		}
		// add runestones
		for (IRunestoneEntity entity : entityList) {
			entity.getRunestone().addInformation(stack, world, tooltip, flag, entity);
		}	
	}
	
	@SuppressWarnings("deprecation")
	public String getCapacityHoverText(ItemStack stack, World world, InventoryType type) {	
		return I18n.translateToLocalFormatted("tooltip.runestones.slots", 				
				String.valueOf(Math.toIntExact(Math.round(getCurrentSize(type)))), // used
				String.valueOf(Math.toIntExact(Math.round(getMaxSize(type))))); // max				
	}
	
	@Override
	public boolean hasRunestone() {
//		Treasure.logger.debug("runestones size - > {}", runestoneEntities.values().size());
		if (runestoneEntities.values().size() > 0) {
			return true;
		}
		return false;
	}
	
	@Override
	public void clear() {
//		for (InventoryType type : InventoryType.values()) {
//			setCurrentSize(type, 0);
//		}
		runestoneEntities.clear();
	}
	
	@Override
	public void copyTo(ItemStack stack) {
		if (stack.hasCapability(RUNESTONES, null)) {
			IRunestonesCapability cap = stack.getCapability(RUNESTONES, null);
			
			// copy properties
			cap.setBindable(isBindable());
			cap.setSocketable(isSocketable());
			
			runestoneEntities.forEach((type, runestone) -> {
				Treasure.logger.debug("copyinging runestone -> {} to type -> {}", runestone, type);
				cap.add(type, runestone);
				Treasure.logger.debug("output runestones size -> {}", cap.getEntities(type).size());
			});
		}
	}
	
	@Override
	public void transferTo(ItemStack dest, InventoryType sourceType, InventoryType destType) {
		if (dest.hasCapability(RUNESTONES, null)) {
			IRunestonesCapability cap = dest.getCapability(RUNESTONES, null);
//			for (InventoryType type : InventoryType.values()) {
				List<IRunestoneEntity> runestones = (List<IRunestoneEntity>) getEntities(sourceType);
				Treasure.logger.debug("runestones size -> {}", runestones.size());
				// process each charm entity (only innate as charmItems can have innate)
				for (IRunestoneEntity entity : runestones) {
					if (cap.getCurrentSize(destType) < cap.getMaxSize(destType)) {
						Treasure.logger.debug("adding runestone -> {} to type -> {}", entity, destType);
						cap.add(destType, entity);
						Treasure.logger.debug("output runestones size -> {}", cap.getEntities(destType).size());
					}
				}
//			}
		}
	}
	
	@Override
	public boolean contains(IRunestone runestone) {
		// TODO
		return false;
	}

	@Override
	public List<IRunestoneEntity> getEntities(InventoryType type) {
		return (List<IRunestoneEntity>) runestoneEntities.get(type);
	}
	
	/**
	 * Copy of all runestone entities. Not attached to underlying map.
	 * @return
	 */
	@Override
	public Multimap<InventoryType, IRunestoneEntity> getEntitiesCopy() {
		Multimap<InventoryType, IRunestoneEntity> map = ArrayListMultimap.create();
		runestoneEntities.forEach((key, value) -> {
			IRunestoneEntity entity = new RunestoneEntity(value);
			map.put(key, entity);
		});
		return map;
	}
	
	@Override
	public int getCurrentSize(InventoryType type) {
		// check against SOCKET first as this will be the most common
//		return (type == InventoryType.SOCKET ? getMagicsCap().getSocketSize() : type == InventoryType.IMBUE ? getMagicsCap().getImbueSize() : getMagicsCap().getInnateSize());	
		return getEntities(type).size();
	}

	@Deprecated
	@Override
	public void setCurrentSize(InventoryType type, int size) {
//		switch(type) {
//		case INNATE:
//			getMagicsCap().setInnateSize(size);
//			break;
//		case IMBUE:
//			getMagicsCap().setImbueSize(size);
//		break;
//		case SOCKET:
//			getMagicsCap().setSocketSize(size);
//			break;		
//		}

	}

	@Deprecated
	@Override
	public void addCurrentSize(InventoryType type, int amount) {
		setCurrentSize(type, getCurrentSize(type) + amount);
	}
	
	/**
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
	 * @param type
	 * @param entity
	 */
	@Override
	public void add(InventoryType type, IRunestoneEntity entity) {

		// TODO ensure only one of the same type can be added.
		Treasure.logger.debug("attempt add: current {} size -> {}, max size -> {}", type, getCurrentSize(type), getMaxSize(type));
		// test if there is enough space to add
		if (getCurrentSize(type) < getMaxSize(type)) {
			runestoneEntities.get(type).add(entity);
			Treasure.logger.debug("add: current {} size -> {}, max size -> {}", type, getCurrentSize(type), getMaxSize(type));
			// update the current size
//			addCurrentSize(type, 1);
		}
	}
	
	public static class Builder {
		private IMagicsInventoryCapability magicsCap;
		public boolean bindable;
		public boolean socketable;
		
		// max inventory sizes
		private int maxSocketSize;
		private int maxImbueSize;
		private int maxInnateSize;
		
		public Builder(int maxInnateSize, int maxImbueSize, int maxSocketSize) {
			this.maxInnateSize = maxInnateSize;
			this.maxImbueSize = maxImbueSize;
			this.maxSocketSize = maxSocketSize;
		}

		@Deprecated
		public Builder(IMagicsInventoryCapability magicsCap) {
			this.magicsCap = magicsCap;
		}
		
		public Builder with(Consumer<Builder> builder) {
			builder.accept(this);
			return this;
		}
		
		public IRunestonesCapability build() {
			return new RunestonesCapability(this);
		}
	}

//	@Deprecated
//	@Override
//	public IMagicsInventoryCapability getMagicsCap() {
//		return magicsCap;
//	}

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

	public int getMaxSocketSize() {
		return maxSocketSize;
	}

	public int getMaxImbueSize() {
		return maxImbueSize;
	}

	public int getMaxInnateSize() {
		return maxInnateSize;
	}
}
