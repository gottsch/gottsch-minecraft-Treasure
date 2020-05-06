/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

import java.util.Random;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.BlockEvent;

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
	FullnessCharm(ICharmBuilder builder) {
		super(builder);
	}

	/**
	 * 
	 */
	@Override
	public ICharmVitals doCharm(World world, Random random, ICoords coords, EntityPlayer player, LivingUpdateEvent event, final ICharmVitals vitals) {
		if (!player.isDead && vitals.getValue() > 0 && player.getFoodStats().getFoodLevel() < MAX_FOOD_LEVEL) {
			if (world.getTotalWorldTime() % SECOND_IN_TICKS == 0) {			
				player.getFoodStats().addStats(1, 1);
				ICharmVitals nv = new CharmVitals(vitals.getValue() - 1, vitals.getDuration(), vitals.getPercent());
				Treasure.logger.debug("new vitals -> {}", nv);
				return nv;
			}
		}
		return vitals;
	}

	@Override
	public ICharmVitals doCharm(World world, Random random, ICoords coords, EntityPlayer player, LivingDamageEvent event, final ICharmVitals vitals) {
		return vitals;
	}
	
	@Override
	public ICharmVitals doCharm(World world, Random random, ICoords coords, EntityPlayer player, BlockEvent.HarvestDropsEvent event, final ICharmVitals vitals) {
		return vitals;
	}	
}
