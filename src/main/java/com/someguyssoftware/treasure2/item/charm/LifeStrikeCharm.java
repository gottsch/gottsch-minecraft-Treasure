/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author Mark Gottschling on Apr 26, 2020
 *
 */
public class LifeStrikeCharm extends Charm {
	private static final float LIFE_AMOUNT = 2.0F;

	/**
	 * 
	 * @param builder
	 */
	LifeStrikeCharm(Builder builder) {
		super(builder);
	}

	@Override
	public boolean update(World world, Random random, ICoords coords, EntityPlayer player, Event event, final ICharmData data) {
		boolean result = false;

		if (event instanceof LivingHurtEvent) {
			if (data.getValue() > 0 && !player.isDead) {
				DamageSource source = ((LivingHurtEvent) event).getSource();

				if (source.getTrueSource() instanceof EntityPlayer) {
					if (player.getHealth() > 5.0F) {
						// get the source and amount
						double amount = ((LivingHurtEvent)event).getAmount();
						// increase damage amount
						((LivingHurtEvent)event).setAmount((float) (amount * data.getPercent()));
						// reduce players health
						player.setHealth(MathHelper.clamp(player.getHealth() - LIFE_AMOUNT, 0.0F, player.getMaxHealth()));		
						data.setValue(MathHelper.clamp(data.getValue() - 1,  0D, data.getValue()));
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
		tooltip.add("  " + color + getLabel(data));
		tooltip.add(" " + TextFormatting.GRAY +  "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.charm.life_strike_rate", Math.round((data.getPercent()-1)*100)));
	}
}
