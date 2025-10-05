/*
 * This file is part of Treasure2.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.forge.treasure2.core.item.weapon;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * Extends AxeItem so it behaves like a vanilla axe elsewhere in the codebase. ex. breaks shields etc.
 * @author Mark Gottschling May 24, 2023
 *
 */
public class Axe extends AxeItem implements IWeapon {
	private float criticalChance;
	private float criticalDamage;
	
	public Axe(Tier tier, float damageModifier, float speedModifier, Item.Properties properties) {
		this(tier, damageModifier, speedModifier, 0f, 0f, properties);
	}

	/**
	 * 
	 * @param tier
	 * @param damageModifier
	 * @param speedModifier
	 * @param criticalChance
	 * @param criticalDamage
	 * @param properties
	 */
	public Axe(Tier tier, float damageModifier, float speedModifier, float criticalChance, float criticalDamage, Item.Properties properties) {
		super(tier, damageModifier, speedModifier, properties);
		this.criticalChance = criticalChance;
		this.criticalDamage = criticalDamage;
	}
	
	@Override
	public Component getName(ItemStack itemStack) {
		if (isUnique()) {
			return Component.translatable(this.getDescriptionId(itemStack)).withStyle(ChatFormatting.YELLOW);
		} else {
			return Component.translatable(this.getDescriptionId(itemStack));
		}
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flag) {
		appendStats(stack, worldIn, tooltip, flag);
		appendHoverExtras(stack, worldIn, tooltip, flag);
	}

	public float getCriticalChance() {
		return criticalChance;
	}

	public void setCriticalChance(float criticalChance) {
		this.criticalChance = criticalChance;
	}

	public float getCriticalDamage() {
		return criticalDamage;
	}

	public void setCriticalDamage(float criticalDamage) {
		this.criticalDamage = criticalDamage;
	}
}
