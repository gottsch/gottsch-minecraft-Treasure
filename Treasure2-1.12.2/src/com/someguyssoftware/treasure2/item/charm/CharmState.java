/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

import java.util.Random;

import com.someguyssoftware.gottschcore.positional.ICoords;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.BlockEvent;

/**
 * Contains an instance of a Charm and its Vitals
 * @author Mark Gottschling on Apr 27, 2020
 *
 */
public class CharmState implements ICharmState {
	private ICharm charm;
	private ICharmVitals vitals;

	/**
	 * 
	 * @param charm
	 */
	public CharmState(ICharm charm) {
		this.charm = charm;
		this.vitals = new CharmVitals(charm.getMaxValue(), charm.getMaxDuration(), charm.getMaxPercent());
	}
	
	/**
	 * 
	 * @param charm
	 * @param vitals
	 */
	public CharmState(ICharm charm, ICharmVitals vitals) {
		this.charm = charm;
		this.vitals = vitals;
	}	

	/**
	 * Convenience method.
	 * @param world
	 * @param random
	 * @param coords
	 * @param player
	 * @param vitals
	 */
	@Override
	public boolean doCharm(World world, Random random, ICoords coords, EntityPlayer player, LivingUpdateEvent event) {
		ICharmVitals newVitals = charm.doCharm(world, random, coords, player, event, vitals);
		return checkVitals(vitals, newVitals);
	}
	
	@Override
	public boolean doCharm(World world, Random random, ICoords coords, EntityPlayer player, LivingDamageEvent event) {
		ICharmVitals newVitals = charm.doCharm(world, random, coords, player, event, vitals);
		return checkVitals(vitals, newVitals);
	}
	
	@Override
	public boolean doCharm(World world, Random random, ICoords coords, EntityPlayer player, BlockEvent.HarvestDropsEvent event) {
		ICharmVitals newVitals = charm.doCharm(world, random, coords, player, event, vitals);
		return checkVitals(vitals, newVitals);
	}
	
	/**
	 * 
	 * @param vitals
	 * @param newVitals
	 * @return
	 */
	private boolean checkVitals(ICharmVitals vitals, ICharmVitals newVitals) {
		// check if they are different
		if (vitals != null && !vitals.equals(newVitals)) {
			this.vitals = newVitals;
			return true;
		}
		return false;
	}
	
	@Override
	public ICharm getCharm() {
		return charm;
	}

	@Override
	public void setCharm(ICharm charm) {
		this.charm = charm;
	}

	@Override
	public ICharmVitals getVitals() {
		return vitals;
	}

	@Override
	public void setVitals(ICharmVitals vitals) {
		this.vitals = vitals;
	}

	@Override
	public String toString() {
		return "CharmState [charm=" + charm + ", vitals=" + vitals + "]";
	}
}
