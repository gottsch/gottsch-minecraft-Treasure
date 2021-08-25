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
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.Event;

/**
 * reflection: value = # of uses, duration = range, percent = % of damage reflected
 * @author Mark Gottschling on Aug 23, 2021
 *
 */
public class ReflectionCharm extends Charm {
	public static String REFLECTION_TYPE = "reflection";

	private static final Class<?> REGISTERED_EVENT = LivingDamageEvent.class;

	/**
	 * 
	 * @param builder
	 */
	public ReflectionCharm(Builder builder) {
		super(builder);
	}

	protected ReflectionCharm(Charm.Builder builder) {
		super(builder);
	}
	
	public Class<?> getRegisteredEvent() {
		return REGISTERED_EVENT;
	}

	@Override
	public boolean update(World world, Random random, ICoords coords, PlayerEntity player, Event event, final ICharmEntity entity) {
		boolean result = false;
		if (entity.getValue() > 0 && player.isAlive()) {
			// get player position
			double px = player.position().x;
			double py = player.position().y;
			double pz = player.position().z;
			
			// get the source and amount
			double amount = ((LivingDamageEvent)event).getAmount();
			// calculate the new amount
			double reflectedAmount = amount * entity.getPercent();
			int range = entity.getDuration();
			
			List<MobEntity> mobs = world.getEntitiesOfClass(MobEntity.class, new AxisAlignedBB(px - range, py - range, pz - range, px + range, py + range, pz + range));
			mobs.forEach(mob -> {
				boolean flag = mob.hurt(DamageSource.playerAttack(player), (float) reflectedAmount);
				Treasure.LOGGER.debug("reflected damage {} onto mob -> {} was successful -> {}", reflectedAmount, mob.getName(), flag);
			});
			
			// get all the mob within a radius
			entity.setValue(entity.getValue() - 1.0);
			result = true;
		}    		
		return result;
	}

	/**
	 * 
	 */
	@Override
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn, ICharmEntity entity) {
		TextFormatting color = TextFormatting.BLUE;
		tooltip.add(new StringTextComponent(" ")
				.append(new TranslationTextComponent(getLabel(entity)).withStyle(color)));
		tooltip.add(new StringTextComponent(" ")
				.append(new TranslationTextComponent("tooltip.charm.reflection_rate", Math.toIntExact((long)entity.getPercent()*100), entity.getDuration()).withStyle(TextFormatting.GRAY, TextFormatting.ITALIC)));

	}

	public static class Builder extends Charm.Builder {

		public Builder(Integer level) {
			super(ModUtils.asLocation(makeName(REFLECTION_TYPE, level)), REFLECTION_TYPE, level);
		}

		@Override
		public ICharm build() {
			return  new ReflectionCharm(this);
		}
	}
}
