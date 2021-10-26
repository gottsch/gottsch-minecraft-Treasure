/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.charm.Charm;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.Event;


public class DirtWalkCharm extends Charm {

	/**
	 * 
	 * @param builder
	 */
	DirtWalkCharm(Builder builder) {
		super(builder);
	}

	/**
	 * 
	 */
	@Override
	public boolean update(World world, Random random, ICoords coords, EntityPlayer player, Event event, final ICharmData data) {
		boolean result = false;

		// update every 10 seconds
		if (world.getTotalWorldTime() % 200 == 0) {
			if (event instanceof LivingUpdateEvent) {
				if (!player.isDead && data.getValue() > 0) {
                    // if the current position where standing isn't already dirt, change it to dirt
                    IBlockState state = world.getBlockState(coords.down(1).toPos());
					if (state.getBlock() != Blocks.DIRT) {
                        world.setBlockState(coords.down(1).toPos(), Blocks.DIRT.getDefaultState());
                        data.setValue(MathHelper.clamp(data.getValue() - 1.0,  0D, data.getValue()));
						result = true;
					}
				}
			}
		}
		return result;
	}

	/**
	 * 
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag, ICharmData data) {
        TextFormatting color = TextFormatting.DARK_RED;
		tooltip.add("  " + color + getLabel(data));
	}
}
