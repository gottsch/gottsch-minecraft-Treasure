/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@FunctionalInterface
public interface CharmTooltipAppender {
    public void add(final ItemStack stack, final World world, final List<String> tooltip, final ITooltipFlag flag);
}
