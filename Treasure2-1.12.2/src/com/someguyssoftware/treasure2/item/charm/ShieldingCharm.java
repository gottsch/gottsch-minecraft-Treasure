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
	ShieldingCharm(ICharmBuilder builder) {
		super(builder);
	}

	@Override
	public boolean doCharm(World world, Random random, ICoords coords, EntityPlayer player, LivingUpdateEvent event, final ICharmVitals vitals) {
		return false;
	}
	
	/**
	 * 
	 */
	@Override
	public boolean doCharm(World world, Random random, ICoords coords, EntityPlayer player, LivingDamageEvent event, ICharmVitals vitals) {
		boolean result = false;
		if (vitals.getValue() > 0 && !player.isDead) {
			// get the source and amount
			double amount = event.getAmount();
			// calculate the new amount
			// TODO add percents into the mix - no all is absorbed
			double newAmount = 0;
			// TODO rethink how charms value is.... not an int ???? because health is float
			// TODO these 2 need to be ints, thus need rounding on the calcs
			double amountToCharm = amount * vitals.getPercent(); //Math.toIntExact(Math.round(amount * vitals.getPercent()));
			double amountToPlayer = amount - amountToCharm;
			Treasure.logger.debug("amount to charm -> {}); amount to player -> {}", amountToCharm, amountToPlayer);
			if (vitals.getValue() >= amountToCharm) {
				vitals.setValue(vitals.getValue() - amountToCharm);
				newAmount = amountToPlayer;
			}
			else {
				newAmount = amount - vitals.getValue();
				vitals.setValue(0);
			}
			event.setAmount((float) newAmount);
			Treasure.logger.debug("new vitals -> {}", vitals);
			result = true;
		}
		return result;
	}

	@Override
	public boolean doCharm(World world, Random random, ICoords coords, EntityPlayer player, BlockEvent.HarvestDropsEvent event, final ICharmVitals vitals) {
		return false;
	}
}
