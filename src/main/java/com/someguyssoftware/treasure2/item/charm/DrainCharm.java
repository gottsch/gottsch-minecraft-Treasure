/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

import java.util.List;
import java.util.Random;

import javax.xml.ws.Holder;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * 
 * @author Mark Gottschling on Dec 28, 2020
 *
 */
public class DrainCharm extends Charm {

	/**
	 * 
	 * @param builder
	 */
	DrainCharm(Builder builder) {
		super(builder);
	}

	@Override
	public boolean update(World world, Random random, ICoords coords, EntityPlayer player, Event event, final ICharmData data) {
		boolean result = false;
		if (event instanceof LivingUpdateEvent) {
            if (world.getTotalWorldTime() % (TICKS_PER_SECOND * 5) == 0) {
                if (data.getValue() > 0 && player.getHealth() < player.getMaxHealth() && !player.isDead) {
                    // get player position
                    double px = player.posX;
                    double py = player.posY;
                    double pz = player.posZ;
                    
                    // calculate the new amount
                    int range = data.getDuration();
                    Holder<Integer> drainedHealth = new Holder<>(0);
                    List<EntityMob> mobs = world.getEntitiesWithinAABB(EntityMob.class, new AxisAlignedBB(px - range, py - range, pz - range, px + range, py + range, pz + range));
                    if (mobs.isEmpty()) {
                        return result;
                    }
                    mobs.forEach(mob -> {
                        boolean flag = mob.attackEntityFrom(DamageSource.causePlayerDamage(player), 1.0F);
                        Treasure.logger.debug("health drained from mob -> {} was successful -> {}", mob.getName(), flag);
                        if (flag) {
                            drainedHealth.value++;
                        }
                    });
                    
                    if (drainedHealth.value > 0) {
                        player.setHealth(MathHelper.clamp(player.getHealth() + drainedHealth.value, 0.0F, player.getMaxHealth()));		
                        data.setValue(MathHelper.clamp(data.getValue() - 1D,  0D, data.getValue()));
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
        TextFormatting color = TextFormatting.RED;
		tooltip.add("  " + color + getLabel(data));
		tooltip.add(" " + TextFormatting.GRAY +  "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.charm.drain_rate", data.getDuration()));
	}
}
