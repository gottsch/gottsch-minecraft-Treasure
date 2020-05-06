/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.BlockEvent;

/**
 * Adds light level to a blocks as the player moves every y seconds for a simutaneous maximum x blocks.
 * Ex. level 1, would a light level to a single block every 5 seconds for a max. of 3 block simutaneously lit.
 * After another 5 seconds, the first block would unlight and the newest block would light.
 * @author Mark Gottschling on May 4, 2020
 *
 */
public class IlluminationCharm extends Charm {

	/**
	 * 
	 * @param builder
	 */
	IlluminationCharm(ICharmBuilder builder) {
		super(builder);
	}

	/**
	 * 
	 */
	@Override
	public ICharmVitals doCharm(World world, Random random, ICoords coords, EntityPlayer player, LivingUpdateEvent event, final ICharmVitals vitals) {
		if ( !player.isDead && vitals.getDuration() > 0) {
			if (world.getTotalWorldTime() % 60 == 0) {
				ICoords currentCoords = new Coords((int)player.posX, (int)player.posY - 1, (int)player.posZ);
				IlluminationCharmVitals charmVitals = (IlluminationCharmVitals)vitals;
				// cast as linked list
				List<ICoords> list = (List<ICoords>)charmVitals.getCoordsList();
				
				// check if the coordsList is empty or not
				if (list.isEmpty()) {
					// add current position
					list.add(0, currentCoords);
//					linkedCoords.addFirst(currentCoords);
				}
				else {
					// determine if new position is different than last position - ie first element in vitals.coordsList
//					ICoords firstCoords =list.getFirst();
					ICoords firstCoords = list.get(0);
					if (!currentCoords.equals(firstCoords)) {
						// set the light level of the current coords
						Block block = world.getBlockState(currentCoords.toPos()).getBlock();

						block.setLightLevel(0.95F);
Treasure.logger.debug("set block light level at -> {}", currentCoords.toShortString());
						// add current coords to coords list
						list.add(0, currentCoords);
//						list.addFirst(currentCoords);
						
						// check if coords list is greater than max (vitals.value)
						if (list.size() > (int)charmVitals.getValue()) {
							// reset light level of last
							block = world.getBlockState(list.get(list.size()-1).toPos()).getBlock();
//							block = world.getBlockState(list.getLast().toPos()).getBlock();
							block.setLightLevel(0);
							list.remove(list.get(list.size()-1));
//							list.removeLast();							
						}
					}
				}
				// TODO
				IlluminationCharmVitals nv = new IlluminationCharmVitals(charmVitals.getValue(), charmVitals.getDuration() - 1, charmVitals.getPercent());
				nv.getCoordsList().addAll(list);
				list.clear();
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
