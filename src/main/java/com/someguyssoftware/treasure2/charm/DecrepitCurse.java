/**
 * 
 */
package com.someguyssoftware.treasure2.charm;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.treasure2.util.ModUtils;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.Event;

/**
 * @author Mark Gottschling on Aug 15, 2021
 *
 */
public class DecrepitCurse extends Charm {
	public static String DECREPIT_TYPE = "decrepit";

	private static final Class<?> REGISTERED_EVENT = LivingDamageEvent.class;
	
	DecrepitCurse(Builder builder) {
		super(builder);
	}

	@Override
	public Class<?> getRegisteredEvent() {
		return REGISTERED_EVENT;
	}

	@Override
	public boolean isCurse() {
		return true;
	}
	
	/**
	 * NOTE: it is assumed that only the allowable events are calling this action.
	 */
	@Override
	public boolean update(World world, Random random, ICoords coords, PlayerEntity player, Event event, final ICharmEntity entity) {
		boolean result = false;
		if (world.getGameTime() % 20 == 0) {
			if (player.isAlive() && entity.getValue() > 0 && player.getHealth() > 0.0) {
				double amount = ((LivingDamageEvent)event).getAmount();
				((LivingDamageEvent)event).setAmount((float) (amount * entity.getPercent()));
				entity.setValue(MathHelper.clamp(entity.getValue() - 1.0,  0D, entity.getValue()));
				result = true;
			}
		}
		return result;
	}

	/**
	 * 
	 */
	@Override
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn, ICharmEntity entity) {
		TextFormatting color = TextFormatting.DARK_RED;       
		tooltip.add(new StringTextComponent(" ")
				.append(new TranslationTextComponent(getLabel(entity)).withStyle(color)));
		tooltip.add(new StringTextComponent(" ")
				.append(new TranslationTextComponent("tooltip.charm.rate.decrepit", Math.round((this.getMaxPercent()-1)*100)).withStyle(TextFormatting.GRAY, TextFormatting.ITALIC)));
	}

	public static class Builder extends Charm.Builder {

		public Builder(Integer level) {
			super(ModUtils.asLocation(makeName(DECREPIT_TYPE, level)), DECREPIT_TYPE, level);
		}

		@Override
		public ICharm build() {
			return  new DecrepitCurse(this);
		}
	}
}
