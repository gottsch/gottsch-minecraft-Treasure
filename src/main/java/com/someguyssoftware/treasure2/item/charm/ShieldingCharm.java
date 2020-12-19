/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

import java.util.Random;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.BlockEvent;
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
    @Deprecated
	ShieldingCharm(ICharmBuilder builder) {
		super(builder);
	}

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
    			Treasure.logger.debug("amount to charm -> {}); amount to player -> {}", amountToCharm, amountToPlayer);
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
    @Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag, ICharmDatat data) {
        TextFormatting color = TextFormatting.AQUA;
        tooltip.add("  " + color + I18n.translateToLocalFormatted("tooltip.charm." + getName().toLowerCase(), 
						String.valueOf(Math.toIntExact(Math.round(data.getValue()))), 
                        String.valueOf(Math.toIntExact(Math.round(getMaxValue())))));
        tooltip.add(" " + TextFormatting.GRAY +  "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.charm.shield_rate", state.getCharm().getMaxPercent()*100F));
    }

	@Deprecated
	@Override
	public boolean doCharm(World world, Random random, ICoords coords, EntityPlayer player, LivingUpdateEvent event, final ICharmData data) {
		return false;
	}
	
	/**
	 * 
	 */
	@Deprecated
	@Override
	public boolean doCharm(World world, Random random, ICoords coords, EntityPlayer player, LivingDamageEvent event, ICharmData data) {
		boolean result = false;
		if (data.getValue() > 0 && !player.isDead) {
			// get the source and amount
			double amount = event.getAmount();
			// calculate the new amount
			double newAmount = 0;
			double amountToCharm = amount * data.getPercent();
			double amountToPlayer = amount - amountToCharm;
//			Treasure.logger.debug("amount to charm -> {}); amount to player -> {}", amountToCharm, amountToPlayer);
			if (data.getValue() >= amountToCharm) {
				data.setValue(data.getValue() - amountToCharm);
				newAmount = amountToPlayer;
			}
			else {
				newAmount = amount - data.getValue();
				data.setValue(0);
			}
			event.setAmount((float) newAmount);
			result = true;
		}
		return result;
	}

	@Deprecated
	@Override
	public boolean doCharm(World world, Random random, ICoords coords, EntityPlayer player, BlockEvent.HarvestDropsEvent event, final ICharmData data) {
		return false;
	}
}
