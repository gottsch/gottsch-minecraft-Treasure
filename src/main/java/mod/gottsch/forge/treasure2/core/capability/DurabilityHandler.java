/*
 * This file is part of Treasure2.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.forge.treasure2.core.capability;

import java.util.List;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.event.WorldEventHandler;
import mod.gottsch.forge.treasure2.core.util.TreasureDataFixer;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/**
 * 
 * @author Mark Gottschling on Sep 6, 2020
 *
 */
public class DurabilityHandler implements IDurabilityHandler {
	private static final String MAX_DURABILITY_TAG = "maxDurability";
	private static final String DURABILITY_TAG = "durability";
	private static final String INFINITE_TAG = "infinite";
	
	/*
	 * the max value the durability can be set to.
	 * ex. if merging two keys of 25 durability each and maxDurability = 40
	 * then the durability of the merged key will be 40
	 * 	ie Math.min(key1.durability + key2.durability, maxDurability)
	 * ex. if creating a new itemstack and setting the durability, the
	 * max it will have is maxDurability.
	 */
	private int maxDurability = MAX_DURABILITY;

	/*
	 * the durability of newly created item stacks.
	 */
	private int defaultDurability;

	// the current durability of the capability.
	private int durability = Integer.MIN_VALUE;
	
	// is this durability infinite (as opposed to the finite value of the durability property)
	private boolean infinite;
	
	// the max number of repairs
	private int maxRepairs;
	
	// the remaining repairs available
	private int repairs;
	
	/**
	 * 
	 */
	public DurabilityHandler() {
	}
	
	public DurabilityHandler(int durability) {
		setDurability(durability);
	}
	
	public DurabilityHandler(int durability, int max) {
		// NOTE order is important here
		setMaxDurability(max);
		setDurability(durability);
	}

	@Override
	public CompoundTag save() {
		CompoundTag mainTag = new CompoundTag();
		try {
			mainTag.putInt("defaultDurability", getDefaultDurability());
			mainTag.putInt(DURABILITY_TAG, getDurability());
			mainTag.putInt(MAX_DURABILITY_TAG, getMaxDurability());
			mainTag.putBoolean(INFINITE_TAG, isInfinite());
			mainTag.putInt("repairs", getRepairs());
			mainTag.putInt("maxRepairs", getMaxRepairs());
		} catch (Exception e) {
			Treasure.LOGGER.error("Unable to write state to NBT:", e);
		}
		return mainTag;
	}
	
	@Override
	public void load(Tag tag) {
		if (tag instanceof CompoundTag compound) {

			if (compound.contains(MAX_DURABILITY_TAG)) {
				setMaxDurability(compound.getInt(MAX_DURABILITY_TAG));
			}

			if (compound.contains("defaultDurability")) {
				setDefaultDurability(compound.getInt("defaultDurability"));
			}
			if (compound.contains(DURABILITY_TAG)) {
				setDurability(compound.getInt(DURABILITY_TAG));
			}
			
			if (compound.contains(INFINITE_TAG)) {
				setInfinite(compound.getBoolean(INFINITE_TAG));
			}
			
			if (compound.contains("repairs")) {
				setRepairs(compound.getInt("repairs"));
			}
			if (compound.contains("maxRepairs")) {
				setMaxRepairs(compound.getInt("maxRepairs"));
			}
		}
	}
	
	@Override
	public void copyTo(ItemStack stack) {
		stack.getCapability(TreasureCapabilities.DURABILITY).ifPresent(handler -> {
			// note: set max first
			handler.setMaxDurability(getMaxDurability());
			((DurabilityHandler)handler).setDefaultDurability(getDefaultDurability());
			((DurabilityHandler)handler).setDurability(getDurability());
			handler.setInfinite(isInfinite());
			handler.setMaxRepairs(getMaxRepairs());
			handler.setRepairs(getRepairs());
		});
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
		if (!isInfinite()) {
			MutableComponent text = Component.translatable("tooltip.durability.amount", getDurability() - stack.getDamageValue(), getDurability()).withStyle(ChatFormatting.WHITE);
			if (getMaxRepairs() > 0) {
				text.append(" ").append(Component.translatable("tooltip.durability.repairs", getRepairs(), getMaxRepairs()));
			}
			tooltip.add(text);
		}
		else {
			tooltip.add(Component.translatable("tooltip.durability.amount.infinite").withStyle(ChatFormatting.WHITE));
		}
	}

	@Override
	public int getDefaultDurability() {
		return defaultDurability;
	}

	@Override
	public void setDefaultDurability(int defaultDurability) {
		this.defaultDurability = defaultDurability;
	}

	@Override
	public int durability(Item item) {
		if (durability == Integer.MIN_VALUE) {
			// check if config is loaded
			// NOTE both sides are loaded from the SERVER config values.
			if (WorldEventHandler.isServerLoaded() || WorldEventHandler.isClientLoaded()) {
				durability = TreasureDataFixer.KEYS_CONFIG_MAP.get(item);
				// update the default durability to the same value.
				// NOTE default durability does/should not change after being set.
				defaultDurability = durability;
			} else {
				return defaultDurability;
			}
		}
		return durability;
	}

	protected int getDurability() {
		return durability;
	}

	@Override
	public void setDurability(int maxDamage) {
		if (maxDamage > getMaxDurability()) {
            this.durability = getMaxDurability();
        }
        else {
            this.durability = maxDamage;
        }
	}

	@Override
	public int getMaxDurability() {
		return maxDurability;
	}

	@Override
	public void setMaxDurability(int maxDurability) {
		this.maxDurability = maxDurability;
	}

	@Override
	public boolean isInfinite() {
		return infinite;
	}

	@Override
	public void setInfinite(boolean infinite) {
		this.infinite = infinite;
	}

	@Override
	public int getMaxRepairs() {
		return maxRepairs;
	}

	@Override
	public void setMaxRepairs(int maxRepairs) {
		this.maxRepairs = maxRepairs;
	}

	@Override
	public int getRepairs() {
		return repairs;
	}

	@Override
	public void setRepairs(int repairs) {
		this.repairs = repairs;
	}

	@Override
	public String toString() {
		return "DurabilityHandler [maxDurability=" + maxDurability + ", durability=" + durability + ", infinite="
				+ infinite + ", maxRepairs=" + maxRepairs + ", repairs=" + repairs + "]";
	}
}
