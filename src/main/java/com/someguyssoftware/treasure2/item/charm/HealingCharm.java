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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author Mark Gottschling on Apr 26, 2020
 *
 */
public class HealingCharm extends Charm {
	
	/**
	 * 
	 * @param builder
	 */
	HealingCharm(Builder builder) {
		super(builder);
	}

	@Override
	public boolean update(World world, Random random, ICoords coords, EntityPlayer player, Event event, final ICharmData data) {
		boolean result = false;
		if (world.getTotalWorldTime() % 20 == 0) {
			if (event instanceof LivingUpdateEvent) {
				if (data.getValue() > 0 && player.getHealth() < player.getMaxHealth() && !player.isDead) {
					float amount = Math.min(1F, player.getMaxHealth() - player.getHealth());
					player.setHealth(MathHelper.clamp(player.getHealth() + amount, 0.0F, player.getMaxHealth()));		
					data.setValue(MathHelper.clamp(data.getValue() - amount,  0D, data.getValue()));
					//                    Treasure.logger.debug("new data -> {}", data);
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
        TextFormatting color = TextFormatting.RED;
        // check for specific name prefix (levels 1-10),  
        // tooltip.charm.shielding.prefix.level[x], else look for tooltip.charm.prefix.level[x] + tooltip.charm.[type]

        // first check for mod specific label
        String label = I18n.translateToLocalFormatted("tooltip.charm." + getName().toString().toLowerCase());
        String prefix = "";
        String suffix = "";
        String type = "";
        if (label.isBlank()) {
            type = I18n.translateToLocalFormatted("tooltip.charm." + getName().toString().toLowerCase());
            if (this.getLevel() <= 10) {
                prefix = I18n.translateToLocalFormatted("tooltip.charm." + getType() + ".prefix.level" + String.valueOf(this.getLevel()));
                if (prefix.isBlank()) {
                    I18n.translateToLocalFormatted("tooltip.charm.prefix.level" + String.valueOf(this.getLevel()));
                }
                label = prefix + " " + type;
            }
            else {
                suffix = I18n.translateToLocalFormatted("tooltip.charm." + getType() + ".suffix.level" + String.valueOf(this.getLevel()));
                if (suffix.isBlank()) {
                    I18n.translateToLocalFormatted("tooltip.charm.suffix.level" + String.valueOf(this.getLevel()));
                }
            }
            label = type + " " + suffix;
        }
        
		tooltip.add("  " + color + label,
				String.valueOf(Math.toIntExact(Math.round(data.getValue()))), 
				String.valueOf(Math.toIntExact(Math.round(getMaxValue()))));
		tooltip.add(" " + TextFormatting.GRAY +  "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.charm.healing_rate"));
	}
}
