/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.someguyssoftware.gottschcore.positional.ICoords;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.Event;


public class DirtFillCharm extends Charm {

	/**
	 * 
	 * @param builder
	 */
	DirtFillCharm(Builder builder) {
		super(builder);
	}

	@Override
	public ICharmInstance createInstance() {
		DirtFillCharmData data = new DirtFillCharmData();
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

		// update every 10 seconds
		if (world.getTotalWorldTime() % 200 == 0) {
			if (event instanceof LivingUpdateEvent) {
				if (!player.isDead && data.getValue() > 0) {
					// randomly select an empty inventory slot and fill it with dirt
					List<Integer> emptySlots = getEmptySlotsRandomized(player.inventory, random);
					if (emptySlots != null && !emptySlots.isEmpty()) {
						player.inventory.setInventorySlotContents(((Integer)emptySlots.get(emptySlots.size() - 1)).intValue(), new ItemStack(Blocks.DIRT, 1));		
						data.setValue(MathHelper.clamp(data.getValue() - 1.0,  0D, data.getValue()));
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
        TextFormatting color = TextFormatting.DARK_RED;
		tooltip.add("  " + color + getLabel(data));
	}
	
	/**
	 * 
	 * @param inventory
	 * @param rand
	 * @return
	 */
    private List<Integer> getEmptySlotsRandomized(IInventory inventory, Random rand) {
        List<Integer> list = Lists.<Integer>newArrayList();
        for (int i = 0; i < Math.min(36, inventory.getSizeInventory()); ++i) {
            if (inventory.getStackInSlot(i).isEmpty()) {
                list.add(Integer.valueOf(i));
            }
        }
        Collections.shuffle(list, rand);
        return list;
    }
}
