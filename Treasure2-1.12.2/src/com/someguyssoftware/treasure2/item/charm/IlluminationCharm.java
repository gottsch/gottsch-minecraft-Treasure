/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.cube.Cube;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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
		if ( !player.isDead && vitals.getValue() > 0) {
			if (world.getTotalWorldTime() % 100 == 0) {
				ICoords currentCoords = new Coords((int)player.posX, (int)player.posY, (int)player.posZ);
				// check that the block underneath is solid
				Cube cube = new Cube(world, currentCoords.down(1));
				if (!cube.isSolid()) {
					Treasure.logger.debug("not solid at -> {}", currentCoords.down(1));
					return null;
				}
				if (!(vitals instanceof IlluminationCharmVitals)) {
					Treasure.logger.debug("vitals are not instance of IlluminationCharmVitals -> {}.{}", this.getClass().getSimpleName(), vitals.getClass().getSimpleName());
					// TODO this is a hack until I find out why sometimes a IlluminationCharm doesn't have IlluminationCharmVitals?
					return null;
				}
				IlluminationCharmVitals charmVitals = (IlluminationCharmVitals)vitals;
				// cast as linked list
				List<ICoords> list = (List<ICoords>)charmVitals.getCoordsList();
				Treasure.logger.debug("coords list size -> {}", list.size());
				double value = vitals.getValue();
				
				boolean isUpdated = false;
				// check if the coordsList is empty or not
				if (list.isEmpty()) {
					// add current position
					list.add(0, currentCoords);
//					linkedCoords.addFirst(currentCoords);
					isUpdated = true;
				}
				else {
					// TODO check that current position is at least 5 blocks away from head position
					
					// determine if new position is different than last position - ie first element in vitals.coordsList
//					ICoords firstCoords =list.getFirst();
					ICoords firstCoords = list.get(0);
					if (!currentCoords.equals(firstCoords) && firstCoords.getDistanceSq(currentCoords) >= 25) {
						// set the light level of the current coords
//						Block block = world.getBlockState(currentCoords.toPos()).getBlock();
//						block.setLightLevel(0.95F);

						// add current coords to coords list
						list.add(0, currentCoords);
//						list.addFirst(currentCoords);
						
						// check if coords list is greater than max (vitals.value)
						if (list.size() > (int)charmVitals.getValue()) {
							// reset light level of last
							ICoords lastCoords = list.get(list.size()-1);
							Block block = world.getBlockState(lastCoords.toPos()).getBlock();
							if (block == Blocks.TORCH) {
								Treasure.logger.debug("set torch to air at -> {}", lastCoords.toShortString());
								world.setBlockToAir(lastCoords.toPos());
							}
							else {
								Treasure.logger.debug("torch no longer found at -> {}", currentCoords.toShortString());
								// decrement value since torch was harvested
								value -= 1;
							}
//							block = world.getBlockState(list.getLast().toPos()).getBlock();
//							block.setLightLevel(0);
							list.remove(lastCoords);
//							list.removeLast();							
						}
						isUpdated = true;
					}
				}
				if (isUpdated == true ) {
					world.setBlockState(currentCoords.toPos(), Blocks.TORCH.getDefaultState());
					Treasure.logger.debug("set torch at -> {}", currentCoords.toShortString());
					// TODO vitals has exposed setters - just use them. CAN'T need a new object to compare against in CharmState.
					// TODO else need to refactor doCharm() to return a result
					IlluminationCharmVitals nv = new IlluminationCharmVitals(value, charmVitals.getDuration(), charmVitals.getPercent());
					nv.getCoordsList().addAll(list);
					Treasure.logger.debug("new vitals -> {}", nv);
					return nv;
				}
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
