/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

import java.util.Random;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.BlockEvent;

/**
 * @author Mark Gottschling on Apr 26, 2020
 *
 */
public class HealingCharm extends Charm {

	/**
	 * 
	 * @param builder
	 */
	HealingCharm(ICharmBuilder builder) {
		super(builder);
	}

	/**
	 * 
	 */
	@Override
	public ICharmVitals doCharm(World world, Random random, ICoords coords, EntityPlayer player, LivingUpdateEvent event, final ICharmVitals vitals) {
		if (vitals.getValue() > 0 && player.getHealth() < player.getMaxHealth() && !player.isDead) {
			if (world.getTotalWorldTime() % 20 == 0) {
				float amount = Math.min(1F, player.getMaxHealth() - player.getHealth());
				player.setHealth(MathHelper.clamp(player.getHealth() + amount, 0.0F, player.getMaxHealth()));				
				ICharmVitals nv = new CharmVitals(MathHelper.clamp(vitals.getValue() - amount,  0D, vitals.getValue()), vitals.getDuration(), vitals.getPercent());
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
