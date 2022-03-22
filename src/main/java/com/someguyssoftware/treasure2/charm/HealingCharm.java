/**
 * 
 */
package com.someguyssoftware.treasure2.charm;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.charm.cost.ICostEvaluator;
import com.someguyssoftware.treasure2.util.ResourceLocationUtil;

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
	public static String TYPE = "healing";
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
		if (world.getTotalWorldTime() % entity.getFrequency() == 0) {
			Treasure.logger.debug("healing entity is using cost eval -> {}", getCostEvaluator().getClass().getSimpleName());
			if (entity.getMana() > 0 && player.getHealth() < player.getMaxHealth() && !player.isDead) {
				// determine the actual amount of health (0.0 -> getAmount())
				float amount = Math.min((float)getAmount(), player.getMaxHealth() - player.getHealth());
				player.setHealth(MathHelper.clamp(player.getHealth() + amount, 0.0F, player.getMaxHealth()));		
				applyCost(world, random, coords, player, event, entity, amount);
				result = true;
			}
		}
		return result;
	}
		
	/**
	 * 
	 */
//	@SuppressWarnings("deprecation")
//	@Override
//	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag, ICharmEntity entity) {
////        TextFormatting color = TextFormatting.RED;       
//		tooltip.add(getLabel(entity));
//		tooltip.add(TextFormatting.GRAY +  "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.indent3", 
//				I18n.translateToLocalFormatted("tooltip.charm.rate.healing", 
//						DECIMAL_FORMAT.format(getAmount()/2),
//						(int)(entity.getFrequency()/TICKS_PER_SECOND))));
//	}
	
	@SuppressWarnings("deprecation")
	@Override
	public String getCharmDesc(ICharmEntity entity) {
		return I18n.translateToLocalFormatted("tooltip.charm.rate.healing", DECIMAL_FORMAT.format(getAmount()/2), (int)(entity.getFrequency()/TICKS_PER_SECOND));
	}
	
	public static class Builder extends Charm.Builder {
		public Builder(Integer level) {
			super(ResourceLocationUtil.create(makeName(TYPE, level)), TYPE, level);
			this.costEvaluator = new Costinator();
		}

		@Override
		public ICharm build() {
			return  new HealingCharm(this);
		}
	}
	
	/**
	 * Example custom CostEvaluator.
	 * Accomplishes the same goal as Charm.CostEvaluator but coded differently.
	 * @author Mark Gottschling on Jan 13, 2022
	 *
	 */
	public static class Costinator implements ICostEvaluator {
		/**
		 * @param amount the cost amount requested
		 * @return the actual cost incurred
		 */
		@Override
		public double apply(World world, Random random, ICoords coords, EntityPlayer player, Event event, ICharmEntity entity, double amount) {

			double cost = amount;
			if (entity.getMana() < amount) {
				cost = entity.getMana();
			}
			double remaining = entity.getMana() - cost;
			entity.setMana(MathHelper.clamp(remaining,  0D, entity.getMana()));
			return cost;
		}
	}
}
