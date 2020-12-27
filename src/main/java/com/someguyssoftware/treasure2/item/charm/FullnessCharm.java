/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.positional.ICoords;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * 
 * @author Mark Gottschling on May 2, 2020
 *
 */
public class FullnessCharm extends Charm {
	public static final int MAX_FOOD_LEVEL = 20;
	

	/**
	 * 
	 * @param builder
	 */
	FullnessCharm(Builder builder) {
		super(builder);
	}

	@Override
	public boolean update(World world, Random random, ICoords coords, EntityPlayer player, Event event, final ICharmData data) {
		boolean result = false;
		if (event instanceof LivingUpdateEvent) {
			if (world.getTotalWorldTime() % SECOND_IN_TICKS == 0) {	
				if (!player.isDead && data.getValue() > 0 && player.getFoodStats().getFoodLevel() < MAX_FOOD_LEVEL) {
					player.getFoodStats().addStats(1, 1);
					data.setValue(data.getValue() - 1);
					result = true;
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
		TextFormatting color = TextFormatting.DARK_GREEN;
		tooltip.add("  " + color + getLabel(data));
		tooltip.add(" " + TextFormatting.GRAY +  "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.charm.fullness_rate"));
	}
}
