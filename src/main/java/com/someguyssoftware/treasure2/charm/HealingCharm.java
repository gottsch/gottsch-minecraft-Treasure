/**
 * 
 */
package com.someguyssoftware.treasure2.charm;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.item.charm.ICharmData;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author Mark Gottschling on Apr 26, 2020
 *
 */
public class HealingCharm extends Charm {
	public static String HEALING_TYPE = "healing";
	private static float HEAL_RATE = 1F;
	private static final Class<?> REGISTERED_EVENT = LivingUpdateEvent.class;
	
	/**
	 * 
	 * @param builder
	 */
	HealingCharm(Builder builder) {
		super(builder);
	}
	
	@Override
	public Class<?> getRegisteredEvent() {
		return REGISTERED_EVENT;
	}

	/**
	 * NOTE: it is assumed that only the allowable events are calling this action.
	 */
	@Override
	public boolean update(World world, Random random, ICoords coords, EntityPlayer player, Event event, final ICharmEntity entity) {
		boolean result = false;
		if (world.getTotalWorldTime() % 20 == 0) {
			if (entity.getValue() > 0 && player.getHealth() < player.getMaxHealth() && !player.isDead) {
				float amount = Math.min(1F, player.getMaxHealth() - player.getHealth());
				player.setHealth(MathHelper.clamp(player.getHealth() + amount, 0.0F, player.getMaxHealth()));		
				entity.setValue(MathHelper.clamp(entity.getValue() - amount,  0D, entity.getValue()));
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
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag, ICharmEntity entity) {
        TextFormatting color = TextFormatting.RED;       
		tooltip.add("  " + color + getLabel(entity));
		tooltip.add(" " + TextFormatting.GRAY +  "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.charm.healing_rate"));
	}
}
