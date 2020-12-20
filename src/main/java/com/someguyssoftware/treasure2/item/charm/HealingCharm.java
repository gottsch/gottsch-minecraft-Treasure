/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author Mark Gottschling on Apr 26, 2020
 *
 */
public class HealingCharm extends Charm {

	/**
	 * 
	 * @param builder
	 */
    @Deprecated
	HealingCharm(ICharmBuilder builder) {
		super(builder);
	}
	
	/**
	 * 
	 * @param builder
	 */
	HealingCharm(Builder builder) {
		super(builder);
	}

    @Override
	public boolean update(World world, Random random, ICoords coords, EntityPlayer player, Event event, final ICharmData data) {
        boolean result = false;
        if (world.getTotalWorldTime() % 20 == 0) {
            if (event instanceof LivingUpdateEvent) {
                if (data.getValue() > 0 && player.getHealth() < player.getMaxHealth() && !player.isDead) {
                    float amount = Math.min(1F, player.getMaxHealth() - player.getHealth());
                    player.setHealth(MathHelper.clamp(player.getHealth() + amount, 0.0F, player.getMaxHealth()));		
                    data.setValue(MathHelper.clamp(data.getValue() - amount,  0D, data.getValue()));
                    Treasure.logger.debug("new data -> {}", data);
                    result = true;
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
        TextFormatting color = TextFormatting.RED;
        tooltip.add("  " + color + I18n.translateToLocalFormatted("tooltip.charm." + getName().toLowerCase(), 
						String.valueOf(Math.toIntExact(Math.round(data.getValue()))), 
                        String.valueOf(Math.toIntExact(Math.round(getMaxValue())))));
        tooltip.add(" " + TextFormatting.GRAY +  "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.charm.healing_rate"));
    }

	/**
	 * 
	 */
    @Deprecated
	@Override
	public boolean doCharm(World world, Random random, ICoords coords, EntityPlayer player, LivingUpdateEvent event, final ICharmData data) {
		boolean result = false;
		if (data.getValue() > 0 && player.getHealth() < player.getMaxHealth() && !player.isDead) {
			if (world.getTotalWorldTime() % 20 == 0) {
				float amount = Math.min(1F, player.getMaxHealth() - player.getHealth());
				player.setHealth(MathHelper.clamp(player.getHealth() + amount, 0.0F, player.getMaxHealth()));		
				data.setValue(MathHelper.clamp(data.getValue() - amount,  0D, data.getValue()));
				Treasure.logger.debug("new data -> {}", data);
				result = true;
			}
		}
		return result;
	}

    @Deprecated
	@Override
	public boolean doCharm(World world, Random random, ICoords coords, EntityPlayer player, LivingDamageEvent event, final ICharmData data) {
		return false;
	}
    
    @Deprecated
	@Override
	public boolean doCharm(World world, Random random, ICoords coords, EntityPlayer player, BlockEvent.HarvestDropsEvent event, final ICharmData data) {
		return false;
	}
}
