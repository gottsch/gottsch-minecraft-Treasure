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
//	public CharmState(ICharm charm) {
//		this.charm = charm;
//		this.vitals = new CharmVitals(charm.getMaxValue(), charm.getMaxDuration(), charm.getMaxPercent());
//	}
	
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
	 * Provides the CharmVitals to the Charm.doCharm() method.
	 * @param world
	 * @param random
	 * @param coords
	 * @param player
	 * @param vitals
	 * @return boolean indication whether the call was successful (an update occurred) or not (no update).
	 */
	@Override
	public boolean doCharm(World world, Random random, ICoords coords, EntityPlayer player, LivingUpdateEvent event) {
		return charm.doCharm(world, random, coords, player, event, vitals);
	}
	
	@Override
	public boolean doCharm(World world, Random random, ICoords coords, EntityPlayer player, LivingDamageEvent event) {
		return charm.doCharm(world, random, coords, player, event, vitals);
	}
	
	@Override
	public boolean doCharm(World world, Random random, ICoords coords, EntityPlayer player, BlockEvent.HarvestDropsEvent event) {
		return charm.doCharm(world, random, coords, player, event, vitals);
	}
	
	/**
	 * 
	 * @param vitals
	 * @param newVitals
	 * @return
	 */
	@Deprecated
	private boolean checkVitals(ICharmVitals vitals, ICharmVitals newVitals) {
		// check if they are different
		if (vitals != null && newVitals != null && !vitals.equals(newVitals)) {
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
