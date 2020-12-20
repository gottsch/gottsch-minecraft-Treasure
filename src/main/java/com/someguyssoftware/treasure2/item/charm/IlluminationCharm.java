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
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

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
    @Deprecated
	IlluminationCharm(ICharmBuilder builder) {
		super(builder);
    }
    
    	/**
	 * 
	 * @param builder
	 */
	IlluminationCharm(Builder builder) {
		super(builder);
	}

	@Override
	public ICharmInstance createInstance() {
		Treasure.logger.debug("creating new illumination charm instance");
        IlluminationCharmData data = new IlluminationCharmData();
        data.setValue(this.getMaxValue());
        data.setPercent(this.getMaxPercent());
		data.setDuration(this.getMaxDuration());
        ICharmInstance instance = new CharmInstance(this, data);
        return instance;
	}
	
	/**
	 * 
	 */
	@Override
	public boolean update(World world, Random random, ICoords coords, EntityPlayer player, Event event, final ICharmData data) {
		boolean result = false;
		if (event instanceof LivingUpdateEvent) {
			if ( !player.isDead && data.getValue() > 0) {
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
					if (!(data instanceof IlluminationCharmData)) {
											Treasure.logger.debug("data are not instance of IlluminationCharmData -> {}.{}", this.getClass().getSimpleName(), data.getClass().getSimpleName());
						return false;
					}

					IlluminationCharmData charmData = (IlluminationCharmData)data;
					// cast as linked list
					List<ICoords> list = (List<ICoords>)charmData.getCoordsList();
									Treasure.logger.debug("charm coords list size -> {}", list.size());
					double value = data.getValue();

					boolean isUpdated = false;
					// check if the coordsList is empty or not
					if (list.isEmpty()) {
						// add current position
						list.add(0, currentCoords);
						isUpdated = true;
					}
					else {
						// determine if new position is different than last position - ie first element in data.coordsList
						ICoords firstCoords = list.get(0);
						if (!currentCoords.equals(firstCoords) && firstCoords.getDistanceSq(currentCoords) >= 25) {
							// add current coords to coords list
							list.add(0, currentCoords);
							// check if coords list is greater than max (data.value)
							if (list.size() > (int)charmData.getValue()) {
								// get difference in size
								int diff = (int) (list.size() - charmData.getValue());
								//							Treasure.logger.debug("diff -> {}", diff);
								for (int index = 0; index < diff; index++) {
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
									list.remove(lastCoords);
									//								Treasure.logger.debug("remove torch from list at -> {}; new size ->{}", lastCoords.toShortString(), list.size());								
								}	
							}
							isUpdated = true;
						}
					}
					if (isUpdated == true ) {
						world.setBlockState(currentCoords.toPos(), Blocks.TORCH.getDefaultState());
											Treasure.logger.debug("set torch at -> {}", currentCoords.toShortString());
						if (value < 0) value = 0;
						data.setValue(value);
											Treasure.logger.debug("new data -> {}", data);
						result = true;
					}
				}
			}
		}
		return result;
	}
    
    /**
     * 
     */
    @SuppressWarnings("deprecation")
	@Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag, ICharmData data) {
        TextFormatting color = TextFormatting.WHITE;
        tooltip.add("  " + color + I18n.translateToLocalFormatted("tooltip.charm." + getName().toLowerCase(), 
						String.valueOf(Math.toIntExact(Math.round(data.getValue()))), 
                        String.valueOf(Math.toIntExact(Math.round(getMaxValue())))));
        tooltip.add(" " + TextFormatting.GRAY +  "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.charm.illumination_rate"));
    }

	/**
	 * 
	 */
	@Override
	public boolean doCharm(World world, Random random, ICoords coords, EntityPlayer player, LivingUpdateEvent event, final ICharmData data) {
		boolean result = false;
		if ( !player.isDead && data.getValue() > 0) {
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
				if (!(data instanceof IlluminationCharmData)) {
					//					Treasure.logger.debug("data are not instance of IlluminationCharmData -> {}.{}", this.getClass().getSimpleName(), data.getClass().getSimpleName());
					return false;
				}

				IlluminationCharmData charmData = (IlluminationCharmData)data;
				// cast as linked list
				List<ICoords> list = (List<ICoords>)charmData.getCoordsList();
				//				Treasure.logger.debug("charm coords list size -> {}", list.size());
				double value = data.getValue();

				boolean isUpdated = false;
				// check if the coordsList is empty or not
				if (list.isEmpty()) {
					// add current position
					list.add(0, currentCoords);
					isUpdated = true;
				}
				else {
					// determine if new position is different than last position - ie first element in data.coordsList
					ICoords firstCoords = list.get(0);
					if (!currentCoords.equals(firstCoords) && firstCoords.getDistanceSq(currentCoords) >= 25) {
						// add current coords to coords list
						list.add(0, currentCoords);
						// check if coords list is greater than max (data.value)
						if (list.size() > (int)charmData.getValue()) {
							// get difference in size
							int diff = (int) (list.size() - charmData.getValue());
							//							Treasure.logger.debug("diff -> {}", diff);
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
					data.setValue(value);
					//					Treasure.logger.debug("new data -> {}", data);
					result = true;
				}
			}
		}
		return result;
	}

	@Override
	public boolean doCharm(World world, Random random, ICoords coords, EntityPlayer player, LivingDamageEvent event, final ICharmData data) {
		return false;
	}

	@Override
	public boolean doCharm(World world, Random random, ICoords coords, EntityPlayer player, BlockEvent.HarvestDropsEvent event, final ICharmData data) {
		return false;
	}	
}
