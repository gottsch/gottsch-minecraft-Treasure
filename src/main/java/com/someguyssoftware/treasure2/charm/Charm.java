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

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.util.ResourceLocationUtil;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Apr 25, 2020
 */
public abstract class Charm implements ICharm {
	public static final int TICKS_PER_SECOND = 20;

	private ResourceLocation name;
	private String type;
	private int level;
	// TODO these can be renamed, removing "max";
	private double maxValue;
	private double maxPercent;
	private int maxDuration;
	private double frequency;
	
	/*
	 * if multiple charms of the same type are being processed, only 1 should be updated/executed.
	 * ex. if multiple harvesting charms are held, only one should update.
	 */
	private boolean effetStackable = false;

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
		this.frequency = builder.frequency;
		this.effetStackable = builder.effectStackable;
	}

	abstract public Class<?> getRegisteredEvent();
	
	/**
	 * 
	 */
	@Override
	public ICharmEntity createEntity() {
		ICharmEntity entity = new CharmEntity(this, this.getMaxValue(),this.getMaxDuration(), this.getMaxPercent(), this.frequency);
		return entity;
	}

	@Override
	public boolean isCurse() {
		return false;
	}

	/**
	 * Default method. Concrete classes should override.
	 * @param stack
	 * @param world
	 * @param tooltip
	 * @param flag
	 * @param entity
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag, ICharmEntity entity) {
		TextFormatting color = TextFormatting.WHITE;
		tooltip.add("  " + color + getLabel(entity));
	}
	
	/**
	 * 
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String getLabel(ICharmEntity entity) {
        /*
         * 1. check for mod item specific label
         * 2. check for specific type prefix (levels 1-10)
         * 3. check for general prefix (levels 1-10)
         * OR
         * 4. check for specific type suffix (levels 11+)
         * 5. check for general suffix (levels 11+)
         * ex. tooltip.charm.shielding.prefix.level[x], else look for tooltip.charm.prefix.level[x] + tooltip.charm.[type]
         */
        String tooltipKey = "tooltip.charm." + getName().toString().toLowerCase();
        String label = I18n.translateToLocalFormatted(tooltipKey,
				String.valueOf(Math.toIntExact(Math.round(entity.getValue()))), 
				String.valueOf(Math.toIntExact(Math.round(getMaxValue()))));
        String prefix = "";
        String suffix = "";
        String type = "";
        if (label.equals(tooltipKey)) {
            type = I18n.translateToLocalFormatted("tooltip.charm." + getType(), 
            		String.valueOf(Math.toIntExact(Math.round(entity.getValue()))), 
    				String.valueOf(Math.toIntExact(Math.round(getMaxValue()))));
            if (this.getLevel() <= 10) {
            	String prefixKey = "tooltip.charm." + getType() + ".prefix.level" + String.valueOf(this.getLevel());
                prefix = I18n.translateToLocalFormatted(prefixKey);
                if (prefix.equals(prefixKey)) {
                    prefix = I18n.translateToLocalFormatted("tooltip.charm.prefix.level" + String.valueOf(this.getLevel()));
                }
                label = prefix + " " + type;
            }
            else {
            	String suffixKey = "tooltip.charm." + getType() + ".suffix.level" + String.valueOf(this.getLevel());
                suffix = I18n.translateToLocalFormatted(suffixKey);
                if (suffix.equals(suffixKey)) {
                    suffix = I18n.translateToLocalFormatted("tooltip.charm.suffix.level" + String.valueOf(this.getLevel()));
                }
                label = type + " " + suffix;
            }
        }
        // TODO redo this in future.
        return label + " " + getUsesGauge(entity) + " " + (this.isEffectStackable() ? (TextFormatting.DARK_PURPLE + "* combinable") : "");
	}

	/**
	 * 
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String getUsesGauge(ICharmEntity entity) {
		return I18n.translateToLocalFormatted("tooltip.charm.uses_gauge",
        		String.valueOf(Math.toIntExact(Math.round(entity.getValue()))), 
				String.valueOf(Math.toIntExact(Math.round(getMaxValue()))));
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

	@Override
	public double getMaxPercent() {
		return maxPercent;
	}

	@Override
	public int getMaxDuration() {
		return maxDuration;
	}

	@Override
	public double getFrequency() {
		return frequency;
	}

	@Override
	public boolean isEffectStackable() {
		return effetStackable;
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
		public Double value = 0.0;
		public Double duration = 0.0;
		public Double percent = 0.0;
		public Double frequency = 0.0;
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
			return "Builder [name=" + name + ", type=" + type + ", level=" + level + ", value=" + value + ", duration="
					+ duration + ", percent=" + percent	+ ", frequency=" + frequency + ", effectStackable=" + effectStackable + "]";
		}
	}

	@Override
	public String toString() {
		return "Charm [name=" + name + ", type=" + type + ", level=" + level + ", maxValue=" + maxValue
				+ ", maxPercent=" + maxPercent + ", maxDuration=" + maxDuration
				+ ", frequency=" + frequency + ", effectStackable=" + effetStackable + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (effetStackable ? 1231 : 1237);
		result = prime * result + level;
		result = prime * result + maxDuration;
		long temp;
		temp = Double.doubleToLongBits(maxPercent);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(maxValue);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(frequency);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		if (effetStackable != other.effetStackable)
			return false;
		if (level != other.level)
			return false;
		if (maxDuration != other.maxDuration)
			return false;
		if (Double.doubleToLongBits(maxPercent) != Double.doubleToLongBits(other.maxPercent))
			return false;
		if (Double.doubleToLongBits(maxValue) != Double.doubleToLongBits(other.maxValue))
			return false;
		if (Double.doubleToLongBits(frequency) != Double.doubleToLongBits(other.frequency))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
