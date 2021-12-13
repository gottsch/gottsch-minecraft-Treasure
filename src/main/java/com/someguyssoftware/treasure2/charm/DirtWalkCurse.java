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

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.treasure2.util.ModUtils;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.Event;

/**
 * 
 * @author Mark Gottschling on Aug 24, 2021
 *
 */
public class DirtWalkCurse extends Charm {
	public static final String DIRT_WALK_TYPE = "dirt_walk";

	private static final Class<?> REGISTERED_EVENT = LivingUpdateEvent.class;

	/**
	 * 
	 * @param builder
	 */
	DirtWalkCurse(Builder builder) {
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
				  // if the current position where standing isn't already dirt, change it to dirt
                BlockState state = world.getBlockState(coords.down(1).toPos());
				if (state.getBlock() != Blocks.DIRT) {
                    world.setBlockAndUpdate(coords.down(1).toPos(), Blocks.DIRT.defaultBlockState());
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
	 */
	public static class Builder extends Charm.Builder {

		public Builder(Integer level) {
			super(ModUtils.asLocation(makeName(DIRT_WALK_TYPE, level)), DIRT_WALK_TYPE, level);
		}

		@Override
		public ICharm build() {
			return  new DirtWalkCurse(this);
		}
	}
}
