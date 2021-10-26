/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.charm.Charm;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * 
 * @author Mark Gottschling on Dec 27, 2020
 *
 */
public class FireImmunityCharm extends Charm {

	/**
	 * 
	 * @param builder
	 */
	FireImmunityCharm(Builder builder) {
		super(builder);
	}

	@Override
	public boolean update(World world, Random random, ICoords coords, EntityPlayer player, Event event, final ICharmData data) {
		boolean result = false;
		if (event instanceof LivingDamageEvent) {
			// exit if not fire damage
			if (!((LivingDamageEvent)event).getSource().isFireDamage()) {
				return result;
			}
			if (data.getValue() > 0 && !player.isDead) {
				// get the fire damage amount
				double amount = ((LivingDamageEvent)event).getAmount();

				if (data.getValue() >= amount) {
					data.setValue(data.getValue() - amount);
				}
				else {
					data.setValue(0);
				}
				((LivingDamageEvent)event).setAmount(0F);
				result = true;
			}    		
		}
		return result;
	}

	/**
	 * 
	 */
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag, ICharmData data) {
		TextFormatting color = TextFormatting.RED;
		tooltip.add("  " + color + getLabel(data));
		tooltip.add(" " + TextFormatting.GRAY +  "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.charm.fire_immunity_rate"));
	}
}
