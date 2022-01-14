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

import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.util.ResourceLocationUtil;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * 
 * @author Mark Gottschling on Dec 28, 2020
 *
 */
public class DirtFillCharm extends Charm {
	public static final String DIRT_FILL_TYPE = "dirt_fill";

	private static final Class<?> REGISTERED_EVENT = LivingUpdateEvent.class;

	/**
	 * 
	 * @param builder
	 */
	DirtFillCharm(Builder builder) {
		super(builder);
	}

	public Class<?> getRegisteredEvent() {
		return REGISTERED_EVENT;
	}

	@Override
	public boolean isCurse() {
		return true;
	}

	/**
	 * 
	 */
	@Override
	public boolean update(World world, Random random, ICoords coords, EntityPlayer player, Event event, final ICharmEntity entity) {
		boolean result = false;

		// update every 10 seconds
		if (world.getTotalWorldTime() % 200 == 0) {
			if (!player.isDead && entity.getValue() > 0) {
				// randomly select an empty inventory slot and fill it with dirt
				List<Integer> emptySlots = getEmptySlotsRandomized(player.inventory, random);
				if (emptySlots != null && !emptySlots.isEmpty()) {
					player.inventory.setInventorySlotContents(((Integer)emptySlots.get(emptySlots.size() - 1)).intValue(), new ItemStack(Blocks.DIRT, 1));		
					entity.setValue(MathHelper.clamp(entity.getValue() - 1.0,  0D, entity.getValue()));
					result = true;
				}
			}
		}
		return result;
	}

	/**
	 * 
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag, ICharmEntity entity) {
		TextFormatting color = TextFormatting.DARK_RED;
		tooltip.add("  " + color + getLabel(entity));
	}

	/**
	 * 
	 * @param inventory
	 * @param rand
	 * @return
	 */
	private List<Integer> getEmptySlotsRandomized(IInventory inventory, Random rand) {
		List<Integer> list = Lists.<Integer>newArrayList();
		for (int i = 0; i < Math.min(36, inventory.getSizeInventory()); ++i) {
			if (inventory.getStackInSlot(i).isEmpty()) {
				list.add(Integer.valueOf(i));
			}
		}
		Collections.shuffle(list, rand);
		return list;
	}
	
	public static class Builder extends Charm.Builder {

		public Builder(String name, Integer level) {
			super(ResourceLocationUtil.create(name), DIRT_FILL_TYPE, level);
		}

		@Override
		public ICharm build() {
			return  new DirtFillCharm(this);
		}
	}
}
