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
	public boolean doCharm(World world, Random random, ICoords coords, EntityPlayer player, LivingUpdateEvent event, final ICharmVitals vitals) {
		boolean result = false;
		if ( !player.isDead && vitals.getValue() > 0) {
			if (world.getTotalWorldTime() % 100 == 0) {
				ICoords currentCoords = new Coords((int)Math.floor(player.posX), (int)Math.floor(player.posY), (int)Math.floor(player.posZ));
				
				/*
				 * validation checks
				 */
				// check that the block at current position is air or replaceable
				Cube cube = new Cube(world, currentCoords);
				if (!cube.isAir() && !cube.isReplaceable()) {
					return false;
				}
				// check that the block underneath is solid
				cube = new Cube(world, currentCoords.down(1));
				if (!cube.isSolid()) {
					Treasure.logger.debug("not solid at -> {}", currentCoords.down(1));
					return false;
				}
				if (!(vitals instanceof IlluminationCharmVitals)) {
					Treasure.logger.debug("vitals are not instance of IlluminationCharmVitals -> {}.{}", this.getClass().getSimpleName(), vitals.getClass().getSimpleName());
						return false;
				}
				
				IlluminationCharmVitals charmVitals = (IlluminationCharmVitals)vitals;
				// cast as linked list
				List<ICoords> list = (List<ICoords>)charmVitals.getCoordsList();
//				Treasure.logger.debug("charm coords list size -> {}", list.size());
				double value = vitals.getValue();
				
				boolean isUpdated = false;
				// check if the coordsList is empty or not
				if (list.isEmpty()) {
					// add current position
					list.add(0, currentCoords);
					isUpdated = true;
				}
				else {
					// determine if new position is different than last position - ie first element in vitals.coordsList
					ICoords firstCoords = list.get(0);
					if (!currentCoords.equals(firstCoords) && firstCoords.getDistanceSq(currentCoords) >= 25) {
						// add current coords to coords list
						list.add(0, currentCoords);
						// check if coords list is greater than max (vitals.value)
						if (list.size() > (int)charmVitals.getValue()) {
							// get difference in size
							int diff = (int) (list.size() - charmVitals.getValue());
							Treasure.logger.debug("diff -> {}", diff);
							for (int index = 0; index < diff; index++) {
								ICoords lastCoords = list.get(list.size()-1);
								Block block = world.getBlockState(lastCoords.toPos()).getBlock();
								if (block == Blocks.TORCH) {
//									Treasure.logger.debug("set torch to air at -> {}", lastCoords.toShortString());
									world.setBlockToAir(lastCoords.toPos());
								}
								else {
//									Treasure.logger.debug("torch no longer found at -> {}", currentCoords.toShortString());
									// decrement value since torch was harvested
									value -= 1;
								}
								list.remove(lastCoords);
//								Treasure.logger.debug("remove torch from list at -> {}; new size ->{}", lastCoords.toShortString(), list.size());								
							}	
						}
						isUpdated = true;
					}
				}
				if (isUpdated == true ) {
					world.setBlockState(currentCoords.toPos(), Blocks.TORCH.getDefaultState());
//					Treasure.logger.debug("set torch at -> {}", currentCoords.toShortString());
					if (value < 0) value = 0;
					vitals.setValue(value);
//					Treasure.logger.debug("new vitals -> {}", vitals);
					result = true;
				}
			}
		}
		return result;
	}

	@Override
	public boolean doCharm(World world, Random random, ICoords coords, EntityPlayer player, LivingDamageEvent event, final ICharmVitals vitals) {
		return false;
	}
	
	@Override
	public boolean doCharm(World world, Random random, ICoords coords, EntityPlayer player, BlockEvent.HarvestDropsEvent event, final ICharmVitals vitals) {
		return false;
	}	
}
