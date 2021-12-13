/**
 * 
 */
package com.someguyssoftware.treasure2.charm;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.util.ModUtils;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.Component;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.Event;

/**
 * @author Mark Gottschling on Aug 15, 2021
 *
 */
public class GreaterHealingCharm extends Charm implements IHealing {
	public static String HEALING_TYPE = "greater_healing";
	private static float HEAL_RATE = 2F;
	private static final Class<?> REGISTERED_EVENT = LivingUpdateEvent.class;
	
	GreaterHealingCharm(Builder builder) {
		super(builder);
	}

	@Override
	public Class<?> getRegisteredEvent() {
		return REGISTERED_EVENT;
	}
	
	/**
	 * 
	 */
	@Override
	public float getHealRate() {
		return HEAL_RATE;
	}

	/**
	 * NOTE: it is assumed that only the allowable events are calling this action.
	 */
	@Override
	public boolean update(Level world, Random random, ICoords coords, Player player, Event event, final ICharmEntity entity) {
		boolean result = false;
		if (world.getGameTime() % 20 == 0) {
			if (entity.getValue() > 0 && player.getHealth() < player.getMaxHealth() && player.isAlive()) {
				float amount = Math.min(getHealRate(), player.getMaxHealth() - player.getHealth());
				player.setHealth(Mth.clamp(player.getHealth() + amount, 0.0F, player.getMaxHealth()));		
				entity.setValue(Mth.clamp(entity.getValue() - amount,  0D, entity.getValue()));
				result = true;
			}
		}
		return result;
	}

	/**
	 * 
	 */
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn, ICharmEntity entity) {
		ChatFormatting color = ChatFormatting.RED;       
		tooltip.add(new TranslatableComponent("tooltip.indent2", new TranslatableComponent(getLabel(entity)).withStyle(color)));
		tooltip.add(new TranslatableComponent("tooltip.indent2", new TranslatableComponent("tooltip.charm.rate.greater_healing").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC)));
	}

	public static class Builder extends Charm.Builder {

		public Builder(Integer level) {
			super(ModUtils.asLocation(makeName(HEALING_TYPE, level)), HEALING_TYPE, level);
		}

		@Override
		public ICharm build() {
			return  new GreaterHealingCharm(this);
		}
	}
}
