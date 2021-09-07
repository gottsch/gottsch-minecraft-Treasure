/**
 * 
 */
package com.someguyssoftware.treasure2.charm;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.block.BlockContext;
import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.util.ModUtils;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.Event;

/**
 * @author Mark Gottschling on Aug 15, 2021
 *
 */
public class IlluminationCharm extends Charm {
	public static String ILLUMINATION_TYPE = "illumination";
	private static final Class<?> REGISTERED_EVENT = LivingUpdateEvent.class;
	
	/**
	 * 
	 * @param builder
	 */
	IlluminationCharm(Builder builder) {
		super(builder);
	}
	
	public Class<?> getRegisteredEvent() {
		return REGISTERED_EVENT;
	}
	
	@Override
	public ICharmEntity createEntity() {
		ICharmEntity entity = new IlluminationCharmEntity(this, this.getMaxValue(),this.getMaxDuration(), this.getMaxPercent());
		return entity;
	}
	
	/**
	 * NOTE: it is assumed that only the allowable events are calling this action.
	 */
	@Override
	public boolean update(World world, Random random, ICoords coords, PlayerEntity player, Event event, final ICharmEntity entity) {
		boolean result = false;
		if (world.getGameTime() % 100 == 0) {
			if (entity.getValue() > 0 && player.isAlive()) {
				ICoords currentCoords = new Coords((int)Math.floor(player.position().x), (int)Math.floor(player.position().y), (int)Math.floor(player.position().z));

				/*
				 * validation checks
				 */
				// check that the block at current position is air or replaceable
				BlockContext cube = new BlockContext(world, currentCoords);
				if (!cube.isAir() && !cube.isReplaceable()) {
					return false;
				}
				// check that the block underneath is solid
				cube = new BlockContext(world, currentCoords.down(1));
				if (!cube.isSolid()) {
					Treasure.LOGGER.debug("not solid at -> {}", currentCoords.down(1));
					return false;
				}
				if (!(entity instanceof IlluminationCharmEntity)) {
					Treasure.LOGGER.debug("entity are not instance of IlluminationCharmData -> {}.{}", this.getClass().getSimpleName(), entity.getClass().getSimpleName());
					return false;
				}

				IlluminationCharmEntity charmEntity = (IlluminationCharmEntity)entity;
				// cast as linked list
				List<ICoords> list = (List<ICoords>)charmEntity.getCoordsList();
				Treasure.LOGGER.debug("charm coords list size -> {}", list.size());
				double value = entity.getValue();

				boolean isUpdated = false;
				// check if the coordsList is empty or not
				if (list.isEmpty()) {
					// add current position
					list.add(0, currentCoords);
					isUpdated = true;
				}
				else {
					// determine if new position is different than last position - ie first element in entity.coordsList
					ICoords firstCoords = list.get(0);
					if (!currentCoords.equals(firstCoords) && firstCoords.getDistanceSq(currentCoords) >= 25) {
						// add current coords to coords list
						list.add(0, currentCoords);
						// check if coords list is greater than max (entity.value)
						if (list.size() > (int)charmEntity.getValue()) {
							// get difference in size
							int diff = (int) (list.size() - charmEntity.getValue());
							//															Treasure.logger.debug("diff -> {}", diff);
							for (int index = 0; index < diff; index++) {
								ICoords lastCoords = list.get(list.size()-1);
								Block block = world.getBlockState(lastCoords.toPos()).getBlock();
								if (block == Blocks.TORCH) {
									//	Treasure.logger.debug("set torch to air at -> {}", lastCoords.toShortString());
//									world.setBlockToAir(lastCoords.toPos());
									Block.updateOrDestroy(world.getBlockState(lastCoords.toPos()), Blocks.AIR.defaultBlockState(), world, lastCoords.toPos(), 0);
								}
								else {
									//	Treasure.logger.debug("torch no longer found at -> {}", currentCoords.toShortString());
									// decrement value since torch was harvested
									value -= 1;
								}
								list.remove(lastCoords);
								//	Treasure.logger.debug("remove torch from list at -> {}; new size ->{}", lastCoords.toShortString(), list.size());								
							}	
						}
						isUpdated = true;
					}
				}
				if (isUpdated == true ) {
					world.setBlockAndUpdate(currentCoords.toPos(), Blocks.TORCH.defaultBlockState());
					//	Treasure.logger.debug("set torch at -> {}", currentCoords.toShortString());
					if (value < 0) value = 0;
					entity.setValue(value);
					//	Treasure.logger.debug("new entity -> {}", entity);
					result = true;
				}
			}
		}
		return result;
	}

	/**
	 * 
	 */
	@Override
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn, ICharmEntity entity) {
		TextFormatting color = TextFormatting.RED;       
		tooltip.add(new StringTextComponent(" ")
				.append(new TranslationTextComponent(getLabel(entity)).withStyle(color)));
		tooltip.add(new StringTextComponent(" ")
				.append(new TranslationTextComponent("tooltip.charm.rate.illumination").withStyle(TextFormatting.GRAY, TextFormatting.ITALIC)));
	}

	public static class Builder extends Charm.Builder {

		public Builder(Integer level) {
			super(ModUtils.asLocation(makeName(ILLUMINATION_TYPE, level)), ILLUMINATION_TYPE, level);
		}

		@Override
		public ICharm build() {
			return  new IlluminationCharm(this);
		}
	}
}
