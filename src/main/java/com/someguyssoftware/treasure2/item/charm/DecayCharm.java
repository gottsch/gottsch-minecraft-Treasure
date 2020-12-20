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
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * 
 * @author Mark Gottschling on May 4, 2020
 *
 */
public class DecayCharm extends Charm {

	/**
	 * 
	 * @param builder
	 */
    @Deprecated
	DecayCharm(ICharmBuilder builder) {
		super(builder);
	}

    /**
	 * 
	 * @param builder
	 */
	DecayCharm(Builder builder) {
		super(builder);
    }
    
	/**
	 * 
	 */
	@Override
	public boolean update(World world, Random random, ICoords coords, EntityPlayer player, Event event, final ICharmData data) {
		boolean result = false;
		//		Treasure.logger.debug("in decay");
		if (event instanceof LivingUpdateEvent) {
			if (!player.isDead && data.getValue() > 0 && player.getHealth() > 0.0) {
				//			Treasure.logger.debug("player is alive and charm is good still...");
				if (world.getTotalWorldTime() % 100 == 0) {
					player.setHealth(MathHelper.clamp(player.getHealth() - 2.0F, 0.0F, player.getMaxHealth()));				
					data.setValue(MathHelper.clamp(data.getValue() - 1.0,  0D, data.getValue()));
					//				Treasure.logger.debug("new data -> {}", data);
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
        TextFormatting color = TextFormatting.DARK_RED;
        tooltip.add("  " + color + I18n.translateToLocalFormatted("tooltip.charm." + getName().toLowerCase(), 
						String.valueOf(Math.toIntExact(Math.round(data.getValue()))), 
                        String.valueOf(Math.toIntExact(Math.round(getMaxValue())))));
        tooltip.add(" " + TextFormatting.GRAY +  "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.charm.decay_rate"));
    }

	/**
	 * 
	 */
	@Override
	public boolean doCharm(World world, Random random, ICoords coords, EntityPlayer player, LivingUpdateEvent event, final ICharmData data) {
		boolean result = false;
		//		Treasure.logger.debug("in decay");
		if (!player.isDead && data.getValue() > 0 && player.getHealth() > 0.0) {
			//			Treasure.logger.debug("player is alive and charm is good still...");
			if (world.getTotalWorldTime() % 100 == 0) {
				player.setHealth(MathHelper.clamp(player.getHealth() - 2.0F, 0.0F, player.getMaxHealth()));				
				data.setValue(MathHelper.clamp(data.getValue() - 1.0,  0D, data.getValue()));
				//				Treasure.logger.debug("new data -> {}", data);
				result = true;
			}
		}
		return result;
	}

	@Override
	public boolean doCharm(World world, Random random, ICoords coords, EntityPlayer player, LivingDamageEvent event, final ICharmData data) {
		return false;
	}

	@Override
	public boolean doCharm(World world, Random random, ICoords coords, EntityPlayer player, BlockEvent.HarvestDropsEvent event, final ICharmData data) {
		return false;
	}	
}
