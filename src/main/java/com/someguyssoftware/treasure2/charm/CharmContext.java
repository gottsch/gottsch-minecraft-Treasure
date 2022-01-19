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
package com.someguyssoftware.treasure2.charm;

import java.util.Comparator;
import java.util.function.Consumer;

import com.someguyssoftware.treasure2.capability.ICharmableCapability;
import com.someguyssoftware.treasure2.capability.InventoryType;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

/**
 * 
 * @author Mark Gottschling on Dec 19, 2021
 *
 */
public class CharmContext {
	private EnumHand hand;
	private String slotProviderId;
	private Integer slot;
	private ItemStack itemStack;
	private ICharmableCapability capability;
	private InventoryType type;
	private int index;
	private ICharmEntity entity;
	
	/**
	 * 
	 * @param builder
	 */
	CharmContext(Builder builder) {
		this.hand = builder.hand;
		this.slotProviderId = builder.slotProviderId;
		this.slot = builder.slot;
		this.itemStack = builder.itemStack;
		this.capability = builder.capability;
		this.type = builder.type;
		this.index = builder.index;
		this.entity = builder.entity;
	}
	
	public static Comparator<CharmContext> priorityComparator = new Comparator<CharmContext>() {
		@Override
		public int compare(CharmContext p1, CharmContext p2) {
			// use p1 < p2 because the sort should be ascending
			if (p1.getEntity().getCharm().getPriority() < p2.getEntity().getCharm().getPriority()) {
				// greater than
				return 1;
			}
			else {
				// less than
				return -1;
			}
		}
	};
	
	/*
	 * 
	 */
	public static class Builder {
		public EnumHand hand;
		public String slotProviderId;
		public Integer slot;
		public ItemStack itemStack;
		public ICharmableCapability capability;
		public InventoryType type;
		public int index;
		public ICharmEntity entity;
		
		public Builder with(Consumer<Builder> builder)  {
			builder.accept(this);
			return this;
		}

		public CharmContext build() {
			return new CharmContext(this);
		}		
	}

	public EnumHand getHand() {
		return hand;
	}
	
	public String getSlotProviderId() {
		return slotProviderId;
	}

	public Integer getSlot() {
		return slot;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public ICharmableCapability getCapability() {
		return capability;
	}

	public InventoryType getType() {
		return type;
	}

	public int getIndex() {
		return index;
	}

	public ICharmEntity getEntity() {
		return entity;
	}

	@Override
	public String toString() {
		return "CharmContext [hand=" + hand + ", slotProviderId=" + slotProviderId + ", slot=" + slot
				+ ", itemStack=" + itemStack == null ? "N/A" : itemStack.getDisplayName()
				+ ", capability=" + capability + ", type=" + type + ", index=" + index + ", entity="
				+ entity + "]";
	}

}
