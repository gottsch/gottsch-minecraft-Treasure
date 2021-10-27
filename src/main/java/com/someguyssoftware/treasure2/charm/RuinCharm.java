/**
 * 
 */
package com.someguyssoftware.treasure2.charm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.collect.FluentIterable;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.util.ResourceLocationUtil;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * 
 * @author Mark Gottschling on May 26, 2020
 *
 */
public class RuinCharm extends Charm {
	public static final String RUIN_TYPE = "ruin";

	private static final Class<?> REGISTERED_EVENT = LivingUpdateEvent.class;

	/**
	 * 
	 * @param builder
	 */
	RuinCharm(Builder builder) {
		super(builder);
	}

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
	public boolean update(World world, Random random, ICoords coords, EntityPlayer player, Event event, final ICharmEntity entity) {
		boolean result = false;
		if (!player.isDead && entity.getValue() > 0 && player.getHealth() > 0.0) {
			if (world.getTotalWorldTime() % (entity.getDuration() * TICKS_PER_SECOND) == 0) {
				FluentIterable<ItemStack> inventoryEquipment = (FluentIterable<ItemStack>) player.getEquipmentAndArmor();
				List<ItemStack> actualEquipment = new ArrayList<>(5);
				inventoryEquipment.forEach(itemStack -> {
					if (itemStack.getItem() != Items.AIR) {
						actualEquipment.add(itemStack);
					}
				});
				if (actualEquipment != null && actualEquipment.size() > 0) {
					// randomly pick an item
					ItemStack selectedItemStack  = actualEquipment.get(random.nextInt(actualEquipment.size()));
					Treasure.logger.debug("damaging item -> {}, current damage -> {} of {}", selectedItemStack.getDisplayName(), selectedItemStack.getItemDamage(), selectedItemStack.getMaxDamage());
					// damage the item
					if (selectedItemStack.isItemStackDamageable()) {
						selectedItemStack.attemptDamageItem(1, random, null);
						Treasure.logger.debug("damaged item -> {}, now at damaged -> {} of {}", selectedItemStack.getDisplayName(), selectedItemStack.getItemDamage(), selectedItemStack.getMaxDamage());
						entity.setValue(MathHelper.clamp(entity.getValue() - 1.0,  0D, entity.getValue()));
					}
				}			
				Treasure.logger.debug("charm {} new data -> {}", this.getName(), entity);
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
		TextFormatting color = TextFormatting.DARK_RED;
		tooltip.add("  " + color + getLabel(entity));
		tooltip.add(" " + TextFormatting.GRAY +  "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.charm.ruin_rate", String.valueOf(Math.toIntExact(Math.round(entity.getDuration())))));
	}
	
	public static class Builder extends Charm.Builder {

		public Builder(String name, Integer level) {
			super(ResourceLocationUtil.create(name), RUIN_TYPE, level);
		}

		@Override
		public ICharm build() {
			return  new RuinCharm(this);
		}
	}
}
