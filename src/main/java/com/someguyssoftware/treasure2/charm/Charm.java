/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
 * 
 * All rights reserved.
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package com.someguyssoftware.treasure2.charm;

import java.util.Optional;
import java.util.function.Consumer;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.util.ModUtils;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * @author Mark Gottschling on Apr 25, 2020
 */
public abstract class Charm implements ICharm {
	public static final int TICKS_PER_SECOND = 20;

	private ResourceLocation name;
	private String type;
	private int level;
	private double maxValue;
	private double maxPercent;
	private int maxDuration;
	private Rarity rarity;
	private int priority;

	/*
	 * if multiple charms of the same type are being processed, only 1 should be updated/executed.
	 * ex. if multiple harvesting charms are held, only one should update.
	 */
	private boolean effectStackable = false;

	/**
	 * 
	 * @param builder
	 */
	protected Charm(Builder builder) {
		this.name = builder.name;
		this.type = builder.type;
		this.level = builder.level;
		this.maxValue = builder.value;
		this.maxDuration = builder.duration.intValue();
		this.maxPercent = builder.percent;
		this.rarity = builder.rarity;
		this.priority = builder.priority;
		this.effectStackable = builder.effectStackable;
	}

	abstract public Class<?> getRegisteredEvent();
	
	
	/**
	 * 
	 */
	@Override
	public ICharmEntity createEntity() {
		ICharmEntity entity = new CharmEntity(this, this.getMaxValue(),this.getMaxDuration(), this.getMaxPercent());
		return entity;
	}

	@Override
	public boolean isCurse() {
		return false;
	}
	
	/**
	 * 
	 * @param nbt
	 * @return
	 */
	public static Optional<ICharm> load(CompoundNBT nbt) {
		Optional<ICharm> charm = Optional.empty();
		// read the name of the charm and fetch from the registry
		try {
			String charmName = nbt.getString("name");			
			ResourceLocation resource = ModUtils.asLocation(charmName);
			charm = TreasureCharmRegistry.get(resource);
			if (!charm.isPresent()) {
				throw new Exception(String.format("Unable to locate charm %s in registry.", resource.toString()));
			}
		}
		catch(Exception e) {
			Treasure.LOGGER.error("Unable to read state to NBT:", e);
		}	
		return charm;		
	}

	/**
	 * 
	 * @param nbt
	 * @return
	 */
	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		try {
			nbt.putString("name", this.name.toString());
		}
		catch(Exception e) {
			Treasure.LOGGER.error("Unable to write state to NBT:", e);
		}
		return nbt;
	}

	/**
	 * 
	 * @param data
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String getLabel(ICharmEntity entity) {
		return new TranslationTextComponent("tooltip.charm.type." + getType().toLowerCase()).getString() + " " + String.valueOf(getLevel()) + " "  + getUsesGauge(entity) + " " + (this.effectStackable ? "+" : "-");
	}

	/**
	 * 
	 * @param data
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String getUsesGauge(ICharmEntity entity) {
		return new TranslationTextComponent("tooltip.charm.uses_gauge",
				String.valueOf(Math.toIntExact(Math.round(entity.getValue()))), 
				String.valueOf(Math.toIntExact(Math.round(getMaxValue())))).getString();
	}

	@Override
	public ResourceLocation getName() {
		return name;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public double getMaxValue() {
		return maxValue;
	}

	public double getMaxPercent() {
		return maxPercent;
	}

	@Override
	public int getMaxDuration() {
		return maxDuration;
	}

	@Override
	public Rarity getRarity() {
		return rarity;
	}

	@Override
	public int getPriority() {
		return priority;
	}

	@Override
	public boolean isEffectStackable() {
		return effectStackable;
	}

	/**
	 * 
	 * @author Mark Gottschling on Dec 18, 2020
	 *
	 */
	abstract public static class Builder {
		public ResourceLocation name;
		public String type;
		public Integer level;
		public Double value = 0.0;
		public Double duration = 0.0;
		public Double percent = 0.0;
		public Rarity rarity = Rarity.COMMON;
		public int priority = 10;
		public boolean effectStackable = false;

		/**
		 * 
		 * @param name
		 * @param type
		 * @param level
		 * @param charmClass
		 */
		public Builder(ResourceLocation name, String type, Integer level) {
			this.name = name;
			this.type = type;
			this.level = level;
		}

		/**
		 * 
		 * @return
		 */
		abstract public ICharm build();

		/**
		 * 
		 * @param type
		 * @param level
		 * @return
		 */
		public static String makeName(String type, int level) {
			return type + "_" + level;
		}
		
		/**
		 * 
		 * @param builder
		 * @return
		 */
		public Builder with(Consumer<Builder> builder)  {
			builder.accept(this);
			return this;
		}

		public Builder withValue(Double value) {
			this.value = value;
			return Charm.Builder.this;
		}

		public Builder withDuration(Double duration) {
			this.duration = duration;
			return Charm.Builder.this;
		}

		public Builder withPercent(Double percent) {
			this.percent = percent;
			return Charm.Builder.this;
		}

		public Builder withRarity(Rarity rarity) {
			this.rarity = rarity;
			return Charm.Builder.this;
		}

		public Builder withPriority(int priority) {
			this.priority = priority;
			return Charm.Builder.this;
		}

		public Builder withAllowMultipleUpdates(boolean allow) {
			this.effectStackable = allow;
			return Charm.Builder.this;
		}
	}


	@Override
	public String toString() {
		return "Charm [name=" + name + ", type=" + type + ", level=" + level + ", maxValue=" + maxValue
				+ ", maxPercent=" + maxPercent + ", maxDuration=" + maxDuration + ", rarity=" + rarity + ", priority="
				+ priority + ", effectStackable=" + effectStackable + "]";
	}
}
