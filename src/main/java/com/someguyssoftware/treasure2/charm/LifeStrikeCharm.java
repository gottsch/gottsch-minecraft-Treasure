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
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.util.ResourceLocationUtil;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author Mark Gottschling on Apr 26, 2020
 *
 */
public class LifeStrikeCharm extends Charm {
	private static final float LIFE_AMOUNT = 2.0F;
	public static String LIFE_STRIKE_TYPE = "life_strike";
	private static final Class<?> REGISTERED_EVENT = LivingHurtEvent.class;
	/**
	 * 
	 * @param builder
	 */
	LifeStrikeCharm(Builder builder) {
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
		if (entity.getValue() > 0 && !player.isDead) {
			DamageSource source = ((LivingHurtEvent) event).getSource();
			if (source.getTrueSource() instanceof EntityPlayer) {

				if (player.getHealth() > 5.0F) {
					// get the source and amount
					double amount = ((LivingHurtEvent)event).getAmount();
					// increase damage amount
					((LivingHurtEvent)event).setAmount((float) (amount * entity.getPercent()));
					// reduce players health
					player.setHealth(MathHelper.clamp(player.getHealth() - LIFE_AMOUNT, 0.0F, player.getMaxHealth()));		
					entity.setValue(MathHelper.clamp(entity.getValue() - 1,  0D, entity.getValue()));
					result = true;
					Treasure.logger.debug("life strike damage {} onto mob -> {}", (amount * entity.getPercent()), source.getTrueSource().getName());
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
		TextFormatting color = TextFormatting.WHITE;       
		tooltip.add("  " + color + getLabel(entity));
		tooltip.add(" " + TextFormatting.GRAY +  "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.charm.life_strike_rate", Math.round((entity.getPercent()-1)*100)));
	}
	
	public static class Builder extends Charm.Builder {

		public Builder(String name, Integer level) {
			super(ResourceLocationUtil.create(name), LIFE_STRIKE_TYPE, level);
		}

		@Override
		public ICharm build() {
			return  new LifeStrikeCharm(this);
		}
	}
}
