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
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.util.ModUtils;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
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
	public boolean update(World world, Random random, ICoords coords, PlayerEntity player, Event event, final ICharmEntity entity) {
		boolean result = false;
		if (entity.getValue() > 0 && player.isAlive()) {
			DamageSource source = ((LivingHurtEvent) event).getSource();

			if (source.getEntity() instanceof PlayerEntity) {
				if (player.getHealth() > 5.0F) {
					// get the source and amount
					double amount = ((LivingHurtEvent)event).getAmount();
					// increase damage amount
					((LivingHurtEvent)event).setAmount((float) (Math.max(2F, amount * entity.getPercent())));
					// reduce players health
					player.setHealth(MathHelper.clamp(player.getHealth() - LIFE_AMOUNT, 0.0F, player.getMaxHealth()));		
					entity.setValue(MathHelper.clamp(entity.getValue() - 1,  0D, entity.getValue()));
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
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn, ICharmEntity entity) {
		TextFormatting color = TextFormatting.RED;       
		tooltip.add(new StringTextComponent(" ")
				.append(new TranslationTextComponent(getLabel(entity)).withStyle(color)));
		tooltip.add(new StringTextComponent(" ")
				.append(new TranslationTextComponent("tooltip.charm.rate.life_strike", Math.round((this.getMaxPercent()-1)*100)).withStyle(TextFormatting.GRAY, TextFormatting.ITALIC)));
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
