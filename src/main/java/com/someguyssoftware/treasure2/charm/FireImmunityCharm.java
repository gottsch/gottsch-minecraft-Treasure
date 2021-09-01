/**
 * 
 */
package com.someguyssoftware.treasure2.charm;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.util.ModUtils;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.Event;

/**
 * 
 * @author Mark Gottschling on Aug 23, 2021
 *
 */
public class FireImmunityCharm extends Charm {
	public static final String FIRE_IMMUNITY_TYPE = "fire_immunity";
	private static final Class<?> REGISTERED_EVENT = LivingDamageEvent.class;

	FireImmunityCharm(Builder builder) {
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
	public boolean update(World world, Random random, ICoords coords, PlayerEntity player, Event event, final ICharmEntity entity) {
		boolean result = false;

			if (entity.getValue() > 0 && player.isAlive() && ((LivingDamageEvent)event).getSource().isFire()) {
				// get the fire damage amount
				double amount = ((LivingDamageEvent)event).getAmount();
				
				// if damage is fire damage
				if (entity.getValue() >= amount) {
					entity.setValue(entity.getValue() - amount);
				}
				else {
					entity.setValue(0);
				}
				((LivingDamageEvent)event).setAmount(0F);
				result = true;
			}

		return result;
	}

	/**
	 * 
	 */
	@Override
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn, ICharmEntity entity) {
		TextFormatting color = TextFormatting.RED;       
		tooltip.add(new StringTextComponent(" ")
				.append(new TranslationTextComponent(getLabel(entity)).withStyle(color)));
		tooltip.add(new StringTextComponent(" ")
				.append(new TranslationTextComponent("tooltip.charm.rate.fire_immunity").withStyle(TextFormatting.GRAY, TextFormatting.ITALIC)));
	}

	public static class Builder extends Charm.Builder {

		public Builder(Integer level) {
			super(ModUtils.asLocation(makeName(FIRE_IMMUNITY_TYPE, level)), FIRE_IMMUNITY_TYPE, level);
		}

		@Override
		public ICharm build() {
			return  new FireImmunityCharm(this);
		}
	}
}
