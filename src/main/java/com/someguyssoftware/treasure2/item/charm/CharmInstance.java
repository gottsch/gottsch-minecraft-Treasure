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
 * Contains an instance of a Charm and its CharmData
 * @author Mark Gottschling on Apr 27, 2020
 *
 */
public class CharmInstance implements ICharmInstance {
	private ICharm charm;
	private ICharmData data;

	/**
	 * 
	 * @param charm
	 * @param data
	 */
	public CharmInstance(ICharm charm, ICharmData data) {
		this.charm = charm;
		this.data = data;
	}	

//	@Override
//	public boolean doCharm(World world, Random random, ICoords coords, EntityPlayer player, Event event) {
//		if (event instanceof LivingUpdateEvent) {
//			return charm.doCharm(world, random, coords, player, (LivingUpdateEvent)event, data);
//		}
//		else if (event instanceof LivingDamageEvent) {
//			return charm.doCharm(world, random, coords, player, (LivingDamageEvent)event, data);
//		}
//		else {
//			return charm.doCharm(world, random, coords, player, (BlockEvent.HarvestDropsEvent)event, data);			
//		}
//	}
	
	/**
	 * Provides the CharmVitals to the Charm.doCharm() method.
	 * @param world
	 * @param random
	 * @param coords
	 * @param player
	 * @param data
	 * @return boolean indication whether the call was successful (an update occurred) or not (no update).
	 */
//	@Override
//	public boolean doCharm(World world, Random random, ICoords coords, EntityPlayer player, LivingUpdateEvent event) {
//		return charm.doCharm(world, random, coords, player, event, data);
//	}
//	
//	@Override
//	public boolean doCharm(World world, Random random, ICoords coords, EntityPlayer player, LivingDamageEvent event) {
//		return charm.doCharm(world, random, coords, player, event, data);
//	}
//	
//	@Override
//	public boolean doCharm(World world, Random random, ICoords coords, EntityPlayer player, BlockEvent.HarvestDropsEvent event) {
//		return charm.doCharm(world, random, coords, player, event, data);
//	}
	
	/**
	 * 
	 * @param data
	 * @param newVitals
	 * @return
	 */
	@Deprecated
	private boolean checkVitals(ICharmData data, ICharmData newVitals) {
		// check if they are different
		if (data != null && newVitals != null && !data.equals(newVitals)) {
			this.data = newVitals;
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
	public ICharmData getData() {
		return data;
	}

	@Override
	public void setData(ICharmData data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "CharmInstance [charm=" + charm + ", data=" + data + "]";
	}
}
