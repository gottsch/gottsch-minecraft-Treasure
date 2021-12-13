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
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.Event;

/**
 * 
 * @author Mark Gottschling on Aug 23, 2021
 *
 */
public class FireResistenceCharm extends Charm {
	public static final String FIRE_RESISTENCE_TYPE = "fire_resistence";
	private static final Class<?> REGISTERED_EVENT = LivingDamageEvent.class;

	FireResistenceCharm(Builder builder) {
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
	public boolean update(Level world, Random random, ICoords coords, Player player, Event event, final ICharmEntity entity) {
		boolean result = false;

			if (((LivingDamageEvent)event).getSource().isFire() && entity.getValue() > 0 && player.isAlive()) {
				// get the fire damage amount
				double amount = ((LivingDamageEvent)event).getAmount();
				// calculate the new amount
				double newAmount = 0;
				double amountToCharm = amount * entity.getPercent();
				double amountToPlayer = amount - amountToCharm;
				
				if (entity.getValue() >= amountToCharm) {
					entity.setValue(entity.getValue() - amountToCharm);
					newAmount = amountToPlayer;
				}
				else {
					newAmount = amount - entity.getValue();
					entity.setValue(0);
				}
				((LivingDamageEvent)event).setAmount((float) newAmount);
				
				result = true;
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
		tooltip.add(new TranslatableComponent("tooltip.indent2", new TranslatableComponent("tooltip.charm.rate.fire_resistence", Math.toIntExact(Math.round(this.getMaxPercent() * 100))).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC)));
	}

	public static class Builder extends Charm.Builder {

		public Builder(Integer level) {
			super(ModUtils.asLocation(makeName(FIRE_RESISTENCE_TYPE, level)), FIRE_RESISTENCE_TYPE, level);
		}

		@Override
		public ICharm build() {
			return  new FireResistenceCharm(this);
		}
	}
}
