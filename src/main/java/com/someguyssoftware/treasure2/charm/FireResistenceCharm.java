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

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.util.ResourceLocationUtil;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * 
 * @author Mark Gottschling on Dec 27, 2020
 *
 */
public class FireResistenceCharm extends Charm {
	public static final String FIRE_RESISTENCE_TYPE = "fire_resistence";
	private static final Class<?> REGISTERED_EVENT = LivingDamageEvent.class;

	/**
	 * 
	 * @param builder
	 */
	FireResistenceCharm(Builder builder) {
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
	public boolean update(World world, Random random, ICoords coords, EntityPlayer player, Event event, final ICharmEntity entity) {
		boolean result = false;

		// exit if not fire damage
		if (!((LivingDamageEvent)event).getSource().isFireDamage()) {
			return result;
		}
		if (entity.getValue() > 0 && !player.isDead) {
			// get the source and amount
			double amount = ((LivingDamageEvent)event).getAmount();
			// calculate the new amount
			double newAmount = 0;
			double amountToCharm = amount * entity.getPercent();
			double amountToPlayer = amount - amountToCharm;
			// Treasure.logger.debug("amount to charm -> {}); amount to player -> {}", amountToCharm, amountToPlayer);
			if (entity.getValue() >= amountToCharm) {
				entity.setValue(entity.getValue() - amountToCharm);
				newAmount = amountToPlayer;
			}
			else {
				newAmount = amount - entity.getValue();
				entity.setValue(0);
			}
			((LivingDamageEvent)event).setAmount((float) newAmount);
			result = true;
		}    		
		return result;
	}

	/**
	 * 
	 */
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag, ICharmEntity entity) {
		TextFormatting color = TextFormatting.RED;
		tooltip.add("  " + color + getLabel(entity));
		tooltip.add(" " + TextFormatting.GRAY +  "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.charm.fire_resistence_rate", Math.toIntExact(Math.round(entity.getPercent() * 100))));
	}
	
	public static class Builder extends Charm.Builder {

		public Builder(String name, Integer level) {
			super(ResourceLocationUtil.create(name), FIRE_RESISTENCE_TYPE, level);
		}

		@Override
		public ICharm build() {
			return  new FireResistenceCharm(this);
		}
	}
}
