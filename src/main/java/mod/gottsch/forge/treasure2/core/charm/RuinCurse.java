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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.collect.FluentIterable;
import com.someguyssoftware.gottschcore.spatial.ICoords;

import mod.gottsch.forge.treasure2.core.Treasure;
import mod.gottsch.forge.treasure2.core.util.ModUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.Event;

/**
 * Damage's the player equipment every x seconds.
 * @author Mark Gottschling on May 26, 2020
 *
 */
public class RuinCurse extends Charm {
	public static final String RUIN_TYPE = "ruin";

	private static final Class<?> REGISTERED_EVENT = LivingUpdateEvent.class;

	/**
	 * 
	 * @param builder
	 */
	RuinCurse(Builder builder) {
		super(builder);
	}

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
	public boolean update(World world, Random random, ICoords coords, PlayerEntity player, Event event, final ICharmEntity entity) {
		boolean result = false;
		if (player.isAlive() && entity.getMana() > 0 && player.getHealth() > 0.0) {
			if (world.getGameTime() % entity.getFrequency() == 0) {
				FluentIterable<ItemStack> inventoryEquipment = (FluentIterable<ItemStack>) player.getAllSlots();
				List<ItemStack> actualEquipment = new ArrayList<>(5);
				inventoryEquipment.forEach(itemStack -> {
					if (itemStack.getItem() != Items.AIR) {
						actualEquipment.add(itemStack);
					}
				});
				if (actualEquipment != null && actualEquipment.size() > 0) {
					// randomly pick an item
					ItemStack selectedItemStack  = actualEquipment.get(random.nextInt(actualEquipment.size()));
					Treasure.LOGGER.debug("damaging item -> {}, current damage -> {} of {}", selectedItemStack.getDisplayName(), selectedItemStack.getDamageValue(), selectedItemStack.getMaxDamage());
					// damage the item
					if (selectedItemStack.isDamageableItem()) {
						selectedItemStack.hurt((int)getAmount(), random, null);
						Treasure.LOGGER.debug("damaged item -> {}, now at damaged -> {} of {}", selectedItemStack.getDisplayName(), selectedItemStack.getDamageValue(), selectedItemStack.getMaxDamage());
//						entity.setMana(MathHelper.clamp(entity.getMana() - 1.0,  0D, entity.getMana()));
						applyCost(world, random, coords, player, event, entity, getAmount());
					}
				}			
				Treasure.LOGGER.debug("charm {} new data -> {}", this.getName(), entity);
				result = true;
			}
		}
		return result;
	}
	
	@Override
	public TextFormatting getCharmLabelColor() {
		return TextFormatting.DARK_RED;
	}
	
	@Override
	public ITextComponent getCharmDesc(ICharmEntity entity) {
		return new TranslationTextComponent("tooltip.charm.rate.ruin", DECIMAL_FORMAT.format(getAmount()/2), DECIMAL_FORMAT.format(entity.getFrequency()/TICKS_PER_SECOND));
	}
	
	public static class Builder extends Charm.Builder {

		public Builder(Integer level) {
			super(ModUtils.asLocation(makeName(RUIN_TYPE, level)), RUIN_TYPE, level);
		}

		@Override
		public ICharm build() {
			return  new RuinCurse(this);
		}
	}
}
