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

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.charm.cost.CostEvaluator;
import com.someguyssoftware.treasure2.charm.cost.ICostEvaluator;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.util.ResourceLocationUtil;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author Mark Gottschling on Apr 25, 2020
 */
public abstract class Charm implements ICharm {
	
	protected static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.#");
	public static final int TICKS_PER_SECOND = 20;
	public static final TextFormatting CHARM_COLOR = TextFormatting.AQUA;
	public static final TextFormatting CHARM_DESC_COLOR = TextFormatting.GRAY;

	private ResourceLocation name;
	private String type;
	// the level of the charm
	private int level;
	// the rarity of the charm
	private Rarity rarity;
	// the priority of the charm. lower number execute before higher numbers
	private int priority;

	// usually refers to the number of uses the charm has
	private double mana;
	// the length of time the charm is in effect
	private int duration;
	// time interval that the effect occurs at
	private double frequency;	
	// the effective distance of the charm ex. radius
	private double range;
	 // amount of time before charm can execute again
	private double cooldown;
	// the size of effect ex. damage, amount of healing, percent extra damage
	private double amount;
	
	// calculates the actual cost given an input
	private ICostEvaluator costEvaluator;
	
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
		this.mana = builder.mana;
		this.duration = builder.duration.intValue();
		this.frequency = builder.frequency;
		this.amount = builder.amount;
		this.cooldown = builder.cooldown;
		this.range = builder.range;
		this.effectStackable = builder.effectStackable;
		this.costEvaluator = builder.costEvaluator;
	}

	abstract public Class<?> getRegisteredEvent();
	
	/**
	 * 
	 */
	@Override
	public ICharmEntity createEntity() {
		ICharmEntity entity = new CharmEntity();
		entity.setCharm(this);
		entity.setAmount(amount);
		entity.setCooldown(cooldown);
		entity.setDuration(duration);
		entity.setFrequency(frequency);
		entity.setMana(mana);
		entity.setRange(range);
		entity.setMaxMana(mana);
		return entity;
	}

	@Override
	public ICharmEntity createEntity(ICharmEntity entity) {
		ICharmEntity newEntity = new CharmEntity(entity);
		return newEntity;
	}
	
	@Override
	public boolean isCurse() {
		return false;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag, ICharmEntity entity) {
		tooltip.add(getLabel(entity));
//		tooltip.add(TextFormatting.GRAY +  "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.indent3", 
//				I18n.translateToLocalFormatted("tooltip.charm.rate.healing", 
//						DECIMAL_FORMAT.format(getAmount()/2),
//						(int)(entity.getFrequency()/TICKS_PER_SECOND))));
		tooltip.add(getDesc(entity));
	}
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String getLabel(ICharmEntity entity) {
		return getCharmLabelColor() + "" + I18n.translateToLocalFormatted("tooltip.indent2", I18n.translateToLocalFormatted("tooltip.charm.type." + getType().toLowerCase()) + " " + String.valueOf(getLevel()) + " "  + getUsesGauge(entity) + " " + (this.effectStackable ? "+" : "-"));
	}

	public String getDesc(ICharmEntity entity) {
		return getCharmDescColor() +  "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.indent4", getCharmDesc(entity));
	}
	
	/**
	 * Implemented by concrete Charm.
	 * @param entity
	 * @return
	 */
	public String getCharmDesc(ICharmEntity entity) { return "";};
	
	/**
	 * 
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String getUsesGauge(ICharmEntity entity) {
		return I18n.translateToLocalFormatted("tooltip.charm.uses_gauge",
        		String.valueOf(Math.toIntExact(Math.round(entity.getMana()))), 
//				String.valueOf(Math.toIntExact(Math.round(getMana()))));
        		String.valueOf(Math.toIntExact((long)Math.ceil(entity.getMaxMana()))));
	}
	
	
	/**
	 * This method reads only this Charm's properties from an NBT tag
	 * 
	 * @param tag
	 */
	public static Optional<ICharm> load(NBTTagCompound tag) {
		Optional<ICharm> charm = Optional.empty();
		// read the name of the charm and fetch from the registry
		try {
			String charmName = tag.getString("name");			
			ResourceLocation resource = ResourceLocationUtil.create(charmName);
			charm = TreasureCharmRegistry.get(resource);
			if (!charm.isPresent()) {
				throw new Exception(String.format("Unable to locate charm %s in registry.", resource.toString()));
			}
		}
		catch(Exception e) {
			Treasure.logger.error("Unable to read state to NBT:", e);
		}	

		return charm;
	}

	/**
	 * 
	 * @param tag
	 * @return
	 */
	@Override
	public NBTTagCompound save(NBTTagCompound nbt) {
		try {
			nbt.setString("name", this.name.toString());
		}
		catch(Exception e) {
			Treasure.logger.error("Unable to write state to NBT:", e);
		}
		return nbt;
	}

	/**
	 * wrapper method that checks for the existence of a ICostEvaluator else uses cost property
	 * @param amount
	 * @return
	 */
	public double applyCost(World world, Random random, ICoords coords, EntityPlayer player, Event event, final ICharmEntity entity, double amount) {
		// TODO needs to check the entities cost evaluator - not the Charm's.
		if (entity.getCostEvaluator() != null) {
//			Treasure.logger.debug("entity -> {} has a cost eval -> {}", entity.getClass().getSimpleName(), entity.getCostEvaluator().getClass().getSimpleName());
			return entity.getCostEvaluator().apply(world, random, coords, player, event, entity, amount);
		}
		else {
			Treasure.logger.debug("Charm does not have a cost eval.");
			entity.setMana(MathHelper.clamp(entity.getMana() - 1.0,  0D, entity.getMana()));
		}
		return amount;
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
	public double getMana() {
		return mana;
	}

	@Override
	public int getDuration() {
		return duration;
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
	public double getFrequency() {
		return frequency;
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
		public final String type;
		public final Integer level;
		public Double mana = 0.0;
		public Double duration = 0.0;
		public Double frequency = 0.0;
		public Double amount = 1.0;
		public Double cooldown = 0.0;
		public Double range = 0.0;
		public Rarity rarity = Rarity.COMMON;
		public int priority = 10;
		public boolean effectStackable = false;
		
		public ICostEvaluator costEvaluator;

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
			this.costEvaluator = new CostEvaluator();
		}

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
		
		public Builder withMana(Double mana) {
			this.mana = mana;
			return Charm.Builder.this;
		}

		public Builder withDuration(Double duration) {
			this.duration = duration;
			return Charm.Builder.this;
		}
		
		public Builder withEffectStackable(boolean stackable) {
			this.effectStackable = stackable;
			return Charm.Builder.this;
		}
		
		public Builder withFrequency(Double frequency) {
			this.frequency= frequency;
			return Charm.Builder.this;
		}

		@Override
		public String toString() {
			return "Builder [name=" + name + ", type=" + type + ", level=" + level + ", mana=" + mana + ", duration="
					+ duration + ", frequency=" + frequency + ", amount=" + amount + ", cooldown=" + cooldown
					+ ", range=" + range + ", rarity=" + rarity + ", priority=" + priority + ", effectStackable="
					+ effectStackable + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((amount == null) ? 0 : amount.hashCode());
			result = prime * result + ((cooldown == null) ? 0 : cooldown.hashCode());
			result = prime * result + ((costEvaluator == null) ? 0 : costEvaluator.hashCode());
			result = prime * result + ((duration == null) ? 0 : duration.hashCode());
			result = prime * result + (effectStackable ? 1231 : 1237);
			result = prime * result + ((frequency == null) ? 0 : frequency.hashCode());
			result = prime * result + ((level == null) ? 0 : level.hashCode());
			result = prime * result + ((mana == null) ? 0 : mana.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + priority;
			result = prime * result + ((range == null) ? 0 : range.hashCode());
			result = prime * result + ((rarity == null) ? 0 : rarity.hashCode());
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Builder other = (Builder) obj;
			if (amount == null) {
				if (other.amount != null)
					return false;
			} else if (!amount.equals(other.amount))
				return false;
			if (cooldown == null) {
				if (other.cooldown != null)
					return false;
			} else if (!cooldown.equals(other.cooldown))
				return false;
			if (costEvaluator == null) {
				if (other.costEvaluator != null)
					return false;
			} else if (!costEvaluator.equals(other.costEvaluator))
				return false;
			if (duration == null) {
				if (other.duration != null)
					return false;
			} else if (!duration.equals(other.duration))
				return false;
			if (effectStackable != other.effectStackable)
				return false;
			if (frequency == null) {
				if (other.frequency != null)
					return false;
			} else if (!frequency.equals(other.frequency))
				return false;
			if (level == null) {
				if (other.level != null)
					return false;
			} else if (!level.equals(other.level))
				return false;
			if (mana == null) {
				if (other.mana != null)
					return false;
			} else if (!mana.equals(other.mana))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (priority != other.priority)
				return false;
			if (range == null) {
				if (other.range != null)
					return false;
			} else if (!range.equals(other.range))
				return false;
			if (rarity != other.rarity)
				return false;
			if (type == null) {
				if (other.type != null)
					return false;
			} else if (!type.equals(other.type))
				return false;
			return true;
		}
	}

	@Override
	public String toString() {
		return "Charm [name=" + name + ", type=" + type + ", level=" + level + ", rarity=" + rarity + ", priority="
				+ priority + ", mana=" + mana + ", duration=" + duration + ", frequency=" + frequency + ", range="
				+ range + ", cooldown=" + cooldown + ", amount=" + amount + ", effectStackable=" + effectStackable + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(cooldown);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((costEvaluator == null) ? 0 : costEvaluator.hashCode());
		result = prime * result + duration;
		result = prime * result + (effectStackable ? 1231 : 1237);
		temp = Double.doubleToLongBits(frequency);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + level;
		temp = Double.doubleToLongBits(mana);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + priority;
		temp = Double.doubleToLongBits(range);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((rarity == null) ? 0 : rarity.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Charm other = (Charm) obj;
		if (Double.doubleToLongBits(amount) != Double.doubleToLongBits(other.amount))
			return false;
		if (Double.doubleToLongBits(cooldown) != Double.doubleToLongBits(other.cooldown))
			return false;
		if (costEvaluator == null) {
			if (other.costEvaluator != null)
				return false;
		} else if (!costEvaluator.equals(other.costEvaluator))
			return false;
		if (duration != other.duration)
			return false;
		if (effectStackable != other.effectStackable)
			return false;
		if (Double.doubleToLongBits(frequency) != Double.doubleToLongBits(other.frequency))
			return false;
		if (level != other.level)
			return false;
		if (Double.doubleToLongBits(mana) != Double.doubleToLongBits(other.mana))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (priority != other.priority)
			return false;
		if (Double.doubleToLongBits(range) != Double.doubleToLongBits(other.range))
			return false;
		if (rarity != other.rarity)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public double getRange() {
		return range;
	}

	@Override
	public double getCooldown() {
		return cooldown;
	}

	@Override
	public double getAmount() {
		return amount;
	}

	@Override
	public ICostEvaluator getCostEvaluator() {
		return costEvaluator;
	}

	public static TextFormatting getCharmLabelColor() {
		return CHARM_COLOR;
	}
	
	public static TextFormatting getCharmDescColor() {
		return CHARM_DESC_COLOR;
	}
}
