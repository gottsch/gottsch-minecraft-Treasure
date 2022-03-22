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

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.util.ResourceLocationUtil;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

/*
 *  TODO look at Illumination charm/entity. could add cooldown property
 * could make all charms more generic, each extending and only adding what they need.
 * the only common property would be "value" which is the number of "uses"
 */

public class AegisCharm extends ShieldingCharm {
	
	AegisCharm(Builder builder) {
		super((Charm.Builder)builder);
	}

	public static String AEGIS_TYPE = "aegis";

	@SuppressWarnings("deprecation")
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flagIn, ICharmEntity entity) {
		ShieldingCharmEntity charmEntity = (ShieldingCharmEntity)entity;
		TextFormatting color = TextFormatting.BLUE;
		tooltip.add(color + "" + I18n.translateToLocalFormatted("tooltip.indent2", getLabel(entity)));
		if (charmEntity.getCooldownEnd() > 0.0 && world.getTotalWorldTime() < charmEntity.getCooldownEnd()) {
			tooltip.add(TextFormatting.GRAY + "" + TextFormatting.ITALIC
					+ I18n.translateToLocalFormatted("tooltip.indent2", 
							I18n.translateToLocal("tooltip.charm.rate.aegis") + " " +I18n.translateToLocalFormatted("tooltip.charm.cooldown.meter", DECIMAL_FORMAT.format((charmEntity.getCooldownEnd() - world.getTotalWorldTime())/TICKS_PER_SECOND)))
			);
		}
		else {
		tooltip.add(TextFormatting.GRAY + "" + TextFormatting.ITALIC 
				+ I18n.translateToLocalFormatted("tooltip.indent2", I18n.translateToLocal("tooltip.charm.rate.aegis")));
		}
	}

	public static class Builder extends Charm.Builder {

		public Builder(Integer level) {
			super(ResourceLocationUtil.create(makeName(AEGIS_TYPE, level)), AEGIS_TYPE, level);
		}
		
		@Override
		public ICharm build() {
			return new AegisCharm(this);
		}
	}
}
