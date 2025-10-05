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
package mod.gottsch.forge.treasure2.core.capability;

import java.util.List;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/**
 * 
 * @author Mark Gottschling on Sep 6, 2020
 *
 */
public interface IDurabilityHandler {
    public static final int MAX_DURABILITY = 1000;

	int getDefaultDurability();

	void setDefaultDurability(int defaultDurability);

	int durability(Item item);

	void setDurability(int maxDamage);

	public int getMaxDurability();

	public void setMaxDurability(int maxDurability);

	public boolean isInfinite();

	public void setInfinite(boolean infinite);

	public void copyTo(ItemStack stack);

	public int getMaxRepairs();

	public void setMaxRepairs(int maxRepairs);

	public int getRepairs();

	public void setRepairs(int repairs);

	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag);

	CompoundTag save();

	void load(Tag tag);
}