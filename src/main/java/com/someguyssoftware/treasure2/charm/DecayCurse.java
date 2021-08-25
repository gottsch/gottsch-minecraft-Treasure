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
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.Event;

/**
 * @author Mark Gottschling on Aug 15, 2021
 *
 */
public class DecayCurse extends Charm implements IDecay {
	public static String DECAY_TYPE = "decay";
	private static float DECAY_RATE = 2F;

	private static final Class<?> REGISTERED_EVENT = LivingUpdateEvent.class;
	
	DecayCurse(Builder builder) {
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
	 * 
	 */
	@Override
	public float getDecayRate() {
		return DECAY_RATE;
	}

	/**
	 * NOTE: it is assumed that only the allowable events are calling this action.
	 */
	@Override
	public boolean update(World world, Random random, ICoords coords, PlayerEntity player, Event event, final ICharmEntity entity) {
		boolean result = false;
		if (world.getGameTime() % 20 == 0) {
			if (player.isAlive() && entity.getValue() > 0 && player.getHealth() > 0.0) {
				if (world.getGameTime() % 100 == 0) {
					player.setHealth(MathHelper.clamp(player.getHealth() - getDecayRate(), 0.0F, player.getMaxHealth()));				
					entity.setValue(MathHelper.clamp(entity.getValue() - 1.0,  0D, entity.getValue()));
					//				Treasure.logger.debug("new data -> {}", data);
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
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn, ICharmEntity entity) {
		TextFormatting color = TextFormatting.DARK_RED;       
		tooltip.add(new TranslationTextComponent(getLabel(entity)).withStyle(color));
		tooltip.add(new TranslationTextComponent("tooltip.charm.decay_rate").withStyle(TextFormatting.GRAY, TextFormatting.ITALIC));
	}

	public static class Builder extends Charm.Builder {

		public Builder(Integer level) {
			super(ModUtils.asLocation(makeName(DECAY_TYPE, level)), DECAY_TYPE, level);
		}

		@Override
		public ICharm build() {
			return  new DecayCurse(this);
		}
	}
}
