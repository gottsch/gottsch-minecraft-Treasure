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

import mod.gottsch.forge.treasure2.core.capability.InventoryType;
import mod.gottsch.forge.treasure2.core.util.ModUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
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
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flagIn, ICharmEntity entity, InventoryType type) {
		ShieldingCharmEntity charmEntity = (ShieldingCharmEntity)entity;
		
		tooltip.add(getLabel(entity, type == InventoryType.SOCKET ? true : false));
		if (charmEntity.getCooldownEnd() > 0.0 && world.getGameTime() < charmEntity.getCooldownEnd()) {
			tooltip.add(
					new TranslationTextComponent("tooltip.indent4", 
							new TranslationTextComponent("tooltip.charm.rate.aegis")
							.append(" ")
							.append(new TranslationTextComponent("tooltip.charm.cooldown.meter", DECIMAL_FORMAT.format((charmEntity.getCooldownEnd() - world.getGameTime())/TICKS_PER_SECOND))
					).withStyle(TextFormatting.GRAY).withStyle(TextFormatting.ITALIC)
			));
		}
		else {
			tooltip.add(new TranslationTextComponent("tooltip.indent2", 
					new TranslationTextComponent("tooltip.charm.rate.aegis")).withStyle(TextFormatting.GRAY).withStyle(TextFormatting.ITALIC));
		}
	}
	
	public static class Builder extends Charm.Builder {

		public Builder(Integer level) {
			super(ModUtils.asLocation(makeName(AEGIS_TYPE, level)), AEGIS_TYPE, level);
		}
		
		@Override
		public ICharm build() {
			return new AegisCharm(this);
		}
	}
}
