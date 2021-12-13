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
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.treasure2.util.ModUtils;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.Event;

/**
 * 
 * @author Mark Gottschling on Aug 24, 2021
 *
 */
public class DirtFillCurse extends Charm {
	public static final String DIRT_FILL_TYPE = "dirt_fill";

	private static final Class<?> REGISTERED_EVENT = LivingUpdateEvent.class;

	/**
	 * 
	 * @param builder
	 */
	DirtFillCurse(Builder builder) {
		super(builder);
	}

	public Class<?> getRegisteredEvent() {
		return REGISTERED_EVENT;
	}

	@Override
	public boolean isCurse() {
		return true;
	}
	
	@Override
	public boolean update(Level world, Random random, ICoords coords, Player player, Event event, final ICharmEntity entity) {
		boolean result = false;
		
		// update every 10 seconds
		if (world.getGameTime() % 200 == 0) {
			if (player.isAlive() && entity.getValue() > 0) {
				// randomly select an empty inventory slot and fill it with dirt
				List<Integer> emptySlots = getEmptySlotsRandomized(player.getInventory(), random);
				if (emptySlots != null && !emptySlots.isEmpty()) {
					player.getInventory().setItem(((Integer)emptySlots.get(emptySlots.size() - 1)).intValue(), new ItemStack(Blocks.DIRT, 1));	
					entity.setValue(Mth.clamp(entity.getValue() - 1.0,  0D, entity.getValue()));
					result = true;
				}
			}
		}
		return result;
	}

	/**
	 * 
	 */
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn, ICharmEntity entity) {
		ChatFormatting color = ChatFormatting.DARK_RED;
		tooltip.add(new TranslatableComponent("tooltip.indent2", new TranslatableComponent(getLabel(entity)).withStyle(color)));
	}
	
	/**
	 * 
	 * @param inventory
	 * @param rand
	 * @return
	 */
    private List<Integer> getEmptySlotsRandomized(Inventory inventory, Random rand) {
        List<Integer> list = Lists.<Integer>newArrayList();
        for (int i = 0; i < Math.min(36, inventory.getContainerSize()); ++i) {
            if (inventory.getItem(i).isEmpty()) {
                list.add(Integer.valueOf(i));
            }
        }
        Collections.shuffle(list, rand);
        return list;
    }
    
	/**
	 * 
	 */
	public static class Builder extends Charm.Builder {

		public Builder(Integer level) {
			super(ModUtils.asLocation(makeName(DIRT_FILL_TYPE, level)), DIRT_FILL_TYPE, level);
		}

		@Override
		public ICharm build() {
			return  new DirtFillCurse(this);
		}
	}
}
