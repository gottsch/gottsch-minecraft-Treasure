/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * 
 * @author Mark Gottschling on May 26, 2020
 *
 */
public class DecrepitCharm extends Charm {

	/**
	 * 
	 * @param builder
	 */
	DecrepitCharm(Builder builder) {
		super(builder);
	}

	/**
	 * 
	 */
	@Override
	public boolean update(World world, Random random, ICoords coords, EntityPlayer player, Event event, final ICharmData data) {
		boolean result = false;
		if (event instanceof LivingDamageEvent) {
			if (!player.isDead && data.getValue() > 0 && player.getHealth() > 0.0) {
				double amount = ((LivingDamageEvent)event).getAmount();
				((LivingDamageEvent)event).setAmount((float) (amount * data.getPercent()));
				data.setValue(MathHelper.clamp(data.getValue() - 1.0,  0D, data.getValue()));
				result = true;
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
		tooltip.add(" " + TextFormatting.GRAY +  "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.charm.decrepit_rate", (data.getPercent()-1)*100));
	}
}
