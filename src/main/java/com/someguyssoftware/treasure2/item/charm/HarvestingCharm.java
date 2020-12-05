/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

import java.util.Random;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.BlockEvent;

/**
 * 
 * @author Mark Gottschling on May 4, 2020
 *
 */
public class HarvestingCharm extends Charm {

	/**
	 * 
	 * @param builder
	 */
	HarvestingCharm(ICharmBuilder builder) {
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
		return false;
	}

	@Override
	public boolean doCharm(World world, Random random, ICoords coords, EntityPlayer player, BlockEvent.HarvestDropsEvent event, final ICharmVitals vitals) {
		boolean result = false;
		if (vitals.getValue() > 0 && !player.isDead) {
            // TODO exclude all Charms, Chests, ie any Treasure items
			// process all the drops
			for (ItemStack stack : event.getDrops()) {
				Treasure.logger.debug("current stack size is -> {}", stack.getCount());
				int size = (int)(stack.getCount() * vitals.getPercent());
				stack.setCount(size);
				Treasure.logger.debug("resulting stack size is -> {}", stack.getCount());
			}
			// all items drop
			event.setDropChance(1.0F);
			vitals.setValue(vitals.getValue() - 1);
			result = true;
		}
		return result;
	}
}
