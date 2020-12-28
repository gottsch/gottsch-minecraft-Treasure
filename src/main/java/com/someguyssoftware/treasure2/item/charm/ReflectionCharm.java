/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * 
 * @author Mark Gottschling on Apr 30, 2020
 *
 */
public class ReflectionCharm extends Charm {

	/**
	 * 
	 * @param builder
	 */
	ReflectionCharm(Builder builder) {
		super(builder);
	}

	@Override
	public boolean update(World world, Random random, ICoords coords, EntityPlayer player, Event event, final ICharmData data) {
		boolean result = false;
		if (event instanceof LivingDamageEvent) {
			if (data.getValue() > 0 && !player.isDead) {
				// get player position
				double px = player.posX;
				double py = player.posY;
				double pz = player.posZ;
				
				// get the source and amount
				double amount = ((LivingDamageEvent)event).getAmount();
				// calculate the new amount
				double reflectedAmount = amount * data.getPercent();
				int range = data.getDuration();
				
				List<EntityMob> mobs = world.getEntitiesWithinAABB(EntityMob.class, new AxisAlignedBB(px - range, py - range, pz - range, px + range, py + range, pz + range));
				mobs.forEach(mob -> {
					boolean flag = mob.attackEntityFrom(DamageSource.causePlayerDamage(player), (float) reflectedAmount);
					Treasure.logger.debug("reflected damage {} onto mob -> {} was successful -> {}", reflectedAmount, mob.getName(), flag);
				});
				
				// get all the mob within a radius
				data.setValue(data.getValue() - 1.0);
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
        TextFormatting color = TextFormatting.BLUE;
		tooltip.add("  " + color + getLabel(data));
		tooltip.add(" " + TextFormatting.GRAY +  "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.charm.reflection_rate", 
				Math.toIntExact((long) (data.getPercent()*100)), data.getDuration()));
	}
}
