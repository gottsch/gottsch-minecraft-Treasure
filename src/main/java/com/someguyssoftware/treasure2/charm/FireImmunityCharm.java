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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.Event;

/**
 * 
 * @author Mark Gottschling on Aug 23, 2021
 *
 */
public class FireImmunityCharm extends Charm {
	public static final String FIRE_IMMUNITY_TYPE = "fire_immunity";
	private static final Class<?> REGISTERED_EVENT = LivingDamageEvent.class;

	FireImmunityCharm(Builder builder) {
		super(builder);
	}

	@Override
	public Class<?> getRegisteredEvent() {
		return REGISTERED_EVENT;
	}
	
	/**
	 * NOTE: it is assumed that only the allowable events are calling this action.
	 */
	@Override
	public boolean update(Level world, Random random, ICoords coords, Player player, Event event, final ICharmEntity entity) {
		boolean result = false;

			if (entity.getValue() > 0 && player.isAlive() && ((LivingDamageEvent)event).getSource().isFire()) {
				// get the fire damage amount
				double amount = ((LivingDamageEvent)event).getAmount();
				
				// if damage is fire damage
				if (entity.getValue() >= amount) {
					entity.setValue(entity.getValue() - amount);
				}
				else {
					entity.setValue(0);
				}
				((LivingDamageEvent)event).setAmount(0F);
				result = true;
			}

		return result;
	}

	/**
	 * 
	 */
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn, ICharmEntity entity) {
		ChatFormatting color = ChatFormatting.RED;       
		tooltip.add(new TranslatableComponent("tooltip.indent2", new TranslatableComponent(getLabel(entity)).withStyle(color)));
		tooltip.add(new TranslatableComponent("tooltip.indent2", new TranslatableComponent("tooltip.charm.rate.fire_immunity").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC)));
	}

	public static class Builder extends Charm.Builder {

		public Builder(Integer level) {
			super(ModUtils.asLocation(makeName(FIRE_IMMUNITY_TYPE, level)), FIRE_IMMUNITY_TYPE, level);
		}

		@Override
		public ICharm build() {
			return  new FireImmunityCharm(this);
		}
	}
}
