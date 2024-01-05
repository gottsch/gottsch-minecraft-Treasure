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
package mod.gottsch.forge.treasure2.core.charm;

import java.util.Random;

import com.someguyssoftware.gottschcore.spatial.ICoords;

import mod.gottsch.forge.treasure2.core.util.ModUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.Event;

/**
 * They player takes an additional x amount of damage when hit.
 * @author Mark Gottschling on May 26, 2020
 *
 */
public class DecrepitCurse extends Charm {
	public static String DECREPIT_TYPE = "decrepit";

	private static final Class<?> REGISTERED_EVENT = LivingDamageEvent.class;

	/**
	 * 
	 * @param builder
	 */
	DecrepitCurse(Builder builder) {
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
	 * NOTE: it is assumed that only the allowable events are calling this action.
	 */
	@Override
	public boolean update(World world, Random random, ICoords coords, PlayerEntity player, Event event, final ICharmEntity entity) {
		boolean result = false;

		if (player.isAlive() && entity.getMana() > 0 && player.getHealth() > 0.0) {
			double amount = ((LivingDamageEvent)event).getAmount();
			double newAmount =  amount + (amount * entity.getAmount());
			((LivingDamageEvent)event).setAmount((float)newAmount);
//			entity.setMana(MathHelper.clamp(entity.getMana() - 1.0,  0D, entity.getMana()));
			applyCost(world, random, coords, player, event, entity, newAmount);
			result = true;
		}
		return result;
	}
	
	@Override
	public TextFormatting getCharmLabelColor() {
		return TextFormatting.DARK_RED;
	}
	
	@Override
	public ITextComponent getCharmDesc(ICharmEntity entity) {
		return new TranslationTextComponent("tooltip.charm.rate.decrepit", Math.round((entity.getAmount())*100));
	}
	
	public static class Builder extends Charm.Builder {

		public Builder(Integer level) {
			super(ModUtils.asLocation(makeName(DECREPIT_TYPE, level)), DECREPIT_TYPE, level);
		}

		@Override
		public ICharm build() {
			return  new DecrepitCurse(this);
		}
	}
}
