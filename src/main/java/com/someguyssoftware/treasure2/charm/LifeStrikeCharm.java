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

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.Component;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.Event;

/**
 * 
 * @author Mark Gottschling on Aug 23, 2021
 *
 */
public class LifeStrikeCharm extends Charm implements ILifeStrike {
	public static String LIFE_STRIKE_TYPE = "life_strike";
	private static float LIFE_AMOUNT = 2F;
	private static final Class<?> REGISTERED_EVENT = LivingHurtEvent.class;
	
	LifeStrikeCharm(Builder builder) {
		super(builder);
	}

	@Override
	public Class<?> getRegisteredEvent() {
		return REGISTERED_EVENT;
	}
	
	/**
	 * 
	 */
	@Override
	public float getLifeAmount() {
		return LIFE_AMOUNT;
	}

	/**
	 * NOTE: it is assumed that only the allowable events are calling this action.
	 */
	@Override
	public boolean update(Level world, Random random, ICoords coords, Player player, Event event, final ICharmEntity entity) {
		boolean result = false;
		if (entity.getValue() > 0 && player.isAlive()) {
			DamageSource source = ((LivingHurtEvent) event).getSource();
			if (source.getEntity() instanceof Player) {
				if (player.getHealth() > 5.0F) {
					// get the source and amount
					double amount = ((LivingHurtEvent)event).getAmount();
					// increase damage amount
					((LivingHurtEvent)event).setAmount((float) (Math.max(2F, amount * entity.getPercent())));
					// reduce players health
					player.setHealth(Mth.clamp(player.getHealth() - LIFE_AMOUNT, 0.0F, player.getMaxHealth()));		
					entity.setValue(Mth.clamp(entity.getValue() - 1,  0D, entity.getValue()));
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
		ChatFormatting color = ChatFormatting.RED;       
		tooltip.add(new TranslatableComponent("tooltip.indent2", new TranslatableComponent(getLabel(entity)).withStyle(color)));
		tooltip.add(new TranslatableComponent("tooltip.indent2", new TranslatableComponent("tooltip.charm.rate.life_strike", Math.round((this.getMaxPercent()-1)*100)).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC)));
	}

	public static class Builder extends Charm.Builder {

		public Builder(Integer level) {
			super(ModUtils.asLocation(makeName(LIFE_STRIKE_TYPE, level)), LIFE_STRIKE_TYPE, level);
		}

		@Override
		public ICharm build() {
			return  new LifeStrikeCharm(this);
		}
	}
}
