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
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.Event;

/**
 * @author Mark Gottschling on Aug 15, 2021
 *
 */
public class HealingCharm extends Charm implements IHealing {
	public static String HEALING_TYPE = "healing";
	private static float HEAL_RATE = 1F;
	
	HealingCharm(Builder builder) {
		super(builder);
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
	public boolean update(World world, Random random, ICoords coords, PlayerEntity player, Event event, final ICharmEntity entity) {
		boolean result = false;
		if (world.getGameTime() % 20 == 0) {
//			if (event instanceof LivingUpdateEvent) {
				if (entity.getValue() > 0 && player.getHealth() < player.getMaxHealth() && player.isAlive()) {
					float amount = Math.min(getHealRate(), player.getMaxHealth() - player.getHealth());
					player.setHealth(MathHelper.clamp(player.getHealth() + amount, 0.0F, player.getMaxHealth()));		
					entity.setValue(MathHelper.clamp(entity.getValue() - amount,  0D, entity.getValue()));
					//                    Treasure.logger.debug("new data -> {}", data);
					result = true;
				}
//			}
		}
		return result;
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn, ICharmEntity entity) {
        TextFormatting color = TextFormatting.RED;       
		tooltip.add(new TranslationTextComponent(getLabel(entity)).withStyle(color));
		tooltip.add(new TranslationTextComponent("tooltip.charm.healing_rate").withStyle(TextFormatting.GRAY, TextFormatting.ITALIC));
	}
	
	/**
	 * 
	 */
	@Override
	public CompoundNBT writeToNBT(CompoundNBT nbt) {
		try {
			nbt.putString("name", this.getName().toString());
		}
		catch(Exception e) {
			Treasure.LOGGER.error("Unable to write state to NBT:", e);
		}
		return nbt;
	}
	
	/**
	 * 
	 * @author Mark Gottschling on Aug 15, 2021
	 *
	 */
	public static class Builder extends Charm.Builder {
		
		public Builder(Integer level) {
			super(ModUtils.asLocation(HEALING_TYPE + level), HEALING_TYPE, level);
		}

		@Override
		public ICharm build() {
			return  new HealingCharm(this);
		}
	}
}
