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
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.Event;

/**
 * @author Mark Gottschling on Aug 15, 2021
 *
 */
public class DecayCurse extends Charm implements IDecay {
	public static String DECAY_TYPE = "decay";
	private static float DECAY_RATE = 2F;

	private static final Class<?> REGISTERED_EVENT = LivingUpdateEvent.class;
	
	DecayCurse(Builder builder) {
		super(builder);
	}

	@Override
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
	public float getDecayRate() {
		return DECAY_RATE;
	}

	/**
	 * NOTE: it is assumed that only the allowable events are calling this action.
	 */
	@Override
	public boolean update(Level world, Random random, ICoords coords, Player player, Event event, final ICharmEntity entity) {
		boolean result = false;
		if (world.getGameTime() % 20 == 0) {
			if (player.isAlive() && entity.getValue() > 0 && player.getHealth() > 0.0) {
				if (world.getGameTime() % 100 == 0) {
					player.setHealth(Mth.clamp(player.getHealth() - getDecayRate(), 0.0F, player.getMaxHealth()));				
					entity.setValue(Mth.clamp(entity.getValue() - 1.0,  0D, entity.getValue()));
					//				Treasure.logger.debug("new data -> {}", data);
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
		tooltip.add(new TranslatableComponent("tooltip.indent2", new TranslatableComponent("tooltip.charm.rate.decay").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC)));
	}

	public static class Builder extends Charm.Builder {

		public Builder(Integer level) {
			super(ModUtils.asLocation(makeName(DECAY_TYPE, level)), DECAY_TYPE, level);
		}

		@Override
		public ICharm build() {
			return  new DecayCurse(this);
		}
	}
}
