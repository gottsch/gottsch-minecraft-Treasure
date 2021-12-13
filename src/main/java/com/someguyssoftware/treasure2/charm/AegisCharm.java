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

import com.someguyssoftware.treasure2.util.ModUtils;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;

/**
 * 
 * @author Mark Gottschling on Aug 23, 2021
 *
 */
public class AegisCharm extends ShieldingCharm {

	AegisCharm(Builder builder) {
		super(builder);
	}

	public static String AEGIS_TYPE = "aegis";

	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn, ICharmEntity entity) {
		ChatFormatting color = ChatFormatting.BLUE;
		tooltip.add(new TextComponent(" ")
				.append(new TranslatableComponent(getLabel(entity)).withStyle(color)));
		tooltip.add(new TextComponent(" ")
				.append(new TranslatableComponent("tooltip.charm.rate.aegis", 100).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC)));

	}

	public static class Builder extends Charm.Builder {

		public Builder(Integer level) {
			super(ModUtils.asLocation(makeName(AEGIS_TYPE, level)), AEGIS_TYPE, level);
			this.percent = 1D;
		}

		@Override
		public ICharm build() {
			return  new AegisCharm(this);
		}
	}

}
