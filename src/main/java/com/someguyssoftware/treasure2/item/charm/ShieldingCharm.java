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
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * 
 * @author Mark Gottschling on Apr 30, 2020
 *
 */
public class ShieldingCharm extends Charm {

	/**
	 * 
	 * @param builder
	 */
	ShieldingCharm(Builder builder) {
		super(builder);
	}

	@Override
	public boolean update(World world, Random random, ICoords coords, EntityPlayer player, Event event, final ICharmData data) {
		boolean result = false;
		if (event instanceof LivingDamageEvent) {
			if (data.getValue() > 0 && !player.isDead) {
				// get the source and amount
				double amount = ((LivingDamageEvent)event).getAmount();
				// calculate the new amount
				double newAmount = 0;
				double amountToCharm = amount * data.getPercent();
				double amountToPlayer = amount - amountToCharm;
				//    			Treasure.logger.debug("amount to charm -> {}); amount to player -> {}", amountToCharm, amountToPlayer);
				if (data.getValue() >= amountToCharm) {
					data.setValue(data.getValue() - amountToCharm);
					newAmount = amountToPlayer;
				}
				else {
					newAmount = amount - data.getValue();
					data.setValue(0);
				}
				((LivingDamageEvent)event).setAmount((float) newAmount);
				result = true;
			}    		
		}
		return result;
	}

	/**
	 * 
	 */
	@SuppressWarnings("deprecation")
//	@Override
//	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag, ICharmData data) {
//		TextFormatting color = TextFormatting.AQUA;
//		tooltip.add("  " + color + I18n.translateToLocalFormatted("tooltip.charm." + getName().toString().toLowerCase(), 
//				String.valueOf(Math.toIntExact(Math.round(data.getValue()))), 
//				String.valueOf(Math.toIntExact(Math.round(getMaxValue())))));
//		tooltip.add(" " + TextFormatting.GRAY +  "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.charm.shield_rate", getMaxPercent()*100F));
//	}
	/**
	 * 
	 */
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag, ICharmData data) {
        TextFormatting color = TextFormatting.RED;
        // check for specific name prefix (levels 1-10),  
        // tooltip.charm.shielding.prefix.level[x], else look for tooltip.charm.prefix.level[x] + tooltip.charm.[type]

        // first check for mod specific label
        String label = I18n.translateToLocalFormatted("tooltip.charm." + getName().toString().toLowerCase(),
				String.valueOf(Math.toIntExact(Math.round(data.getValue()))), 
				String.valueOf(Math.toIntExact(Math.round(getMaxValue()))));
        String prefix = "";
        String suffix = "";
        String type = "";
        
        if (label.isEmpty()) {
            type = I18n.translateToLocalFormatted("tooltip.charm." + getType(), 
            		String.valueOf(Math.toIntExact(Math.round(data.getValue()))), 
    				String.valueOf(Math.toIntExact(Math.round(getMaxValue()))));
            if (this.getLevel() <= 10) {
                prefix = I18n.translateToLocalFormatted("tooltip.charm." + getType() + ".prefix.level" + String.valueOf(this.getLevel()));
                if (prefix.isEmpty()) {
                    I18n.translateToLocalFormatted("tooltip.charm.prefix.level" + String.valueOf(this.getLevel()));
                }
                label = prefix + " " + type;
            }
            else {
                suffix = I18n.translateToLocalFormatted("tooltip.charm." + getType() + ".suffix.level" + String.valueOf(this.getLevel()));
                if (suffix.isEmpty()) {
                    I18n.translateToLocalFormatted("tooltip.charm.suffix.level" + String.valueOf(this.getLevel()));
                }
            }
            label = type + " " + suffix;
        }
        
		tooltip.add("  " + color + label);
		tooltip.add(" " + TextFormatting.GRAY +  "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.charm.shielding_rate"));
	}
}
