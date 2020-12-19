/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

import java.util.Random;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * 
 * @author Mark Gottschling on May 2, 2020
 *
 */
public class FullnessCharm extends Charm {
	public static final int MAX_FOOD_LEVEL = 20;
	/**
	 * 
	 * @param builder
	 */
    @Deprecated
	FullnessCharm(ICharmBuilder builder) {
		super(builder);
	}

    	/**
	 * 
	 * @param builder
	 */
	FullnessCharm(Builder builder) {
		super(builder);
    }
    
	@Override
	public boolean update(World world, Random random, ICoords coords, EntityPlayer player, Event event, final ICharmData data) {
		boolean result = false;
		if (event instanceof LivingUpdateEvent) {
			if (world.getTotalWorldTime() % SECOND_IN_TICKS == 0) {	
				if (!player.isDead && data.getValue() > 0 && player.getFoodStats().getFoodLevel() < MAX_FOOD_LEVEL) {
					player.getFoodStats().addStats(1, 1);
					data.setValue(data.getValue() - 1);
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
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag, ICharmDatat data) {
        TextFormatting color = TextFormatting.DARK_GREEN;
        tooltip.add("  " + color + I18n.translateToLocalFormatted("tooltip.charm." + getName().toLowerCase(), 
						String.valueOf(Math.toIntExact(Math.round(data.getValue()))), 
                        String.valueOf(Math.toIntExact(Math.round(getMaxValue())))));
        tooltip.add(" " + TextFormatting.GRAY +  "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.charm.fullness_rate"));
    }

	/**
	 * 
	 */
	@Override
	public boolean doCharm(World world, Random random, ICoords coords, EntityPlayer player, LivingUpdateEvent event, final ICharmData data) {
		boolean result = false;
		if (!player.isDead && data.getValue() > 0 && player.getFoodStats().getFoodLevel() < MAX_FOOD_LEVEL) {
			if (world.getTotalWorldTime() % SECOND_IN_TICKS == 0) {			
				player.getFoodStats().addStats(1, 1);
				data.setValue(data.getValue() - 1);
				result = true;
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
