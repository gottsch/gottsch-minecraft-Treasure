/**
 * 
 */
package com.someguyssoftware.treasure2.charm;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.charm.Charm.Builder;
import com.someguyssoftware.treasure2.util.ModUtils;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.Event;

/**
 * drains 1 health; value = uses, duration = range
 * @author Mark Gottschling on Aug 23, 2021
 *
 */
public class DrainCharm extends Charm {
	public static final String DRAIN_TYPE = "drain";
	private static final Class<?> REGISTERED_EVENT = LivingUpdateEvent.class;

	DrainCharm(Builder builder) {
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

		if (world.getGameTime() % (TICKS_PER_SECOND * 5) == 0) {
            if (entity.getValue() > 0 && player.getHealth() < player.getMaxHealth() && player.isAlive()) {
                // get player position
                double px = player.position().x;
                double py = player.position().y;
                double pz = player.position().z;
                
                // calculate the new amount
                int range = entity.getDuration();
                AtomicInteger drainedHealth = new AtomicInteger(0);
                List<MobEntity> mobs = world.getEntitiesOfClass(MobEntity.class, new AxisAlignedBB(px - range, py - range, pz - range, px + range, py + range, pz + range));
                if (mobs.isEmpty()) {
                    return result;
                }
                mobs.forEach(mob -> {
                	boolean flag = mob.hurt(DamageSource.playerAttack(player), (float) 1.0F);
                    Treasure.LOGGER.debug("health drained from mob -> {} was successful -> {}", mob.getName(), flag);
                    if (flag) {
                        drainedHealth.getAndAdd(1);
                    }
                });
                
                if (drainedHealth.get() > 0) {
                    player.setHealth(MathHelper.clamp(player.getHealth() + drainedHealth.get(), 0.0F, player.getMaxHealth()));		
                    entity.setValue(MathHelper.clamp(entity.getValue() - 1D,  0D, entity.getValue()));
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
		TextFormatting color = TextFormatting.RED;       
		tooltip.add(new TranslationTextComponent(getLabel(entity)).withStyle(color));
		tooltip.add(new TranslationTextComponent("tooltip.charm.drain_rate", Math.toIntExact(Math.round(entity.getPercent() * 100))).withStyle(TextFormatting.GRAY, TextFormatting.ITALIC));
	}

	public static class Builder extends Charm.Builder {

		public Builder(Integer level) {
			super(ModUtils.asLocation(makeName(DRAIN_TYPE, level)), DRAIN_TYPE, level);
		}
		
		@Override
		public ICharm build() {
			return  new DrainCharm(this);
		}
	}
}
