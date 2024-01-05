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

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.spatial.ICoords;

import mod.gottsch.forge.treasure2.core.Treasure;
import mod.gottsch.forge.treasure2.core.util.ModUtils;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.Event;

/**
 * Fired on LivingHurtEvent, so the original amount of damage INTENDED (ie not actual Damage) to be
 * inflicated on Player is reflected back on mob.
 * reflection: value = # of uses, duration = range, percent = % of damage reflected
 * @author Mark Gottschling on Apr 30, 2020
 *
 */
public class ReflectionCharm extends Charm {
	public static String REFLECTION_TYPE = "reflection";

	private static final Class<?> REGISTERED_EVENT = LivingHurtEvent.class;

	/**
	 * 
	 * @param builder
	 */
	ReflectionCharm(Builder builder) {
		super(builder);
	}

	public Class<?> getRegisteredEvent() {
		return REGISTERED_EVENT;
	}

	@Override
	public boolean update(World world, Random random, ICoords coords, PlayerEntity player, Event event, final ICharmEntity entity) {
		boolean result = false;
		if (entity.getMana() > 0 && player.isAlive()) {
			if (((LivingHurtEvent)event).getEntity() instanceof PlayerEntity) {
				// get player position
				double px = player.getX();
				double py = player.getY();
				double pz = player.getZ();

				// get the source and amount
				double amount = ((LivingHurtEvent)event).getAmount();
				// calculate the new amount
				double reflectedAmount = amount * entity.getAmount();
				double range = entity.getRange();
				List<MobEntity> mobs = world.getEntitiesOfClass(MobEntity.class, new AxisAlignedBB(px - range, py - range, pz - range, px + range, py + range, pz + range));
				mobs.forEach(mob -> {
					boolean flag = mob.hurt(DamageSource.GENERIC, (float) reflectedAmount);
					Treasure.LOGGER.debug("reflected damage {} onto mob -> {} was successful -> {}", reflectedAmount, mob.getName(), flag);
				});

				// get all the mob within a radius
//				entity.setMana(entity.getMana() - 1.0);
				applyCost(world, random, coords, player, event, entity, reflectedAmount);
				result = true;
			}    		
		}
		return result;
	}
	
	@Override
	public ITextComponent getCharmDesc(ICharmEntity entity) {
		return  new TranslationTextComponent("tooltip.charm.rate.reflection", Math.toIntExact((long) (entity.getAmount()*100)), entity.getRange());
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
