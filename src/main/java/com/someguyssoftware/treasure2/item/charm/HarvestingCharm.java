/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.item.IPouch;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * 
 * @author Mark Gottschling on May 4, 2020
 *
 */
public class HarvestingCharm extends Charm {
	
	/**
	 * 
	 * @param builder
	 */
	HarvestingCharm(Builder builder) {
		super(builder);
	}

	@Override
	public boolean update(World world, Random random, ICoords coords, EntityPlayer player, Event event, final ICharmData data) {
		boolean result = false;
		if (event instanceof BlockEvent.HarvestDropsEvent) {
			if (data.getValue() > 0 && !player.isDead) {           
				// process all the drops
				for (ItemStack stack : ((BlockEvent.HarvestDropsEvent)event).getDrops()) {

					// exclude all Charms, Pouches or Blocks with Tile Entities
					Block block = Block.getBlockFromItem(stack.getItem());
					if (block != Blocks.AIR) {
						if (block.hasTileEntity(block.getDefaultState())) {
							Treasure.logger.debug("skipped item because it has a tile entity.");
							continue;
						}
					} else {
						if (stack.getItem() instanceof ICharmed || stack.getItem() instanceof IPouch) {
							Treasure.logger.debug("skipped item because it is a charm or pouch.");
							continue;
						}
					}

					//					Treasure.logger.debug("current stack size is -> {}", stack.getCount());
					int size = (int)(stack.getCount() * data.getPercent());
					stack.setCount(size);
					//					Treasure.logger.debug("resulting stack size is -> {}", stack.getCount());
				}
				// all items drop
				((BlockEvent.HarvestDropsEvent)event).setDropChance(1.0F);
				data.setValue(data.getValue() - 1);
				result = true;
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
		TextFormatting color = TextFormatting.GREEN;
		tooltip.add("  " + color + I18n.translateToLocalFormatted("tooltip.charm." + getName().toString().toLowerCase(), 
				String.valueOf(Math.toIntExact(Math.round(data.getValue()))), 
				String.valueOf(Math.toIntExact(Math.round(getMaxValue())))));
		tooltip.add(" " + TextFormatting.GRAY + "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.charm.harvest_rate", getMaxPercent()));
	}

}
