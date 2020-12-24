/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

import java.util.List;
import java.util.Optional;

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
	public static final int SECOND_IN_TICKS = 20;

	private ResourceLocation name;
	private String type;
	private int level;
	private double maxValue;
	private double maxPercent;
	private int maxDuration;

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
	}

	/**
	 * 
	 */
	@Override
	public ICharmInstance createInstance() {
		ICharmData data = new CharmData();
		data.setValue(this.getMaxValue());
		data.setPercent(this.getMaxPercent());
		data.setDuration(this.getMaxDuration());
		ICharmInstance instance = new CharmInstance(this, data);
		return instance;
	}

	/**
	 * Default method. Concrete classes should override.
	 * @param stack
	 * @param world
	 * @param tooltip
	 * @param flag
	 * @param data
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag, ICharmData data) {
		TextFormatting color = TextFormatting.RED;
		tooltip.add("  " + color + I18n.translateToLocalFormatted("tooltip.charm." + getName().toString().toLowerCase()));
	}

	/**
	 * This method reads only this Charm's properties from an NBT tag
	 * 
	 * @param tag
	 */
	public static Optional<ICharm> readFromNBT(NBTTagCompound tag) {
		Optional<ICharm> charm = Optional.empty();
		// read the name of the charm and fetch from the registry
		try {
			String charmName = tag.getString("name");			
			ResourceLocation resource = ResourceLocationUtil.create(charmName);
//			charm = TreasureCharms.REGISTRY.get(charmName);
			charm = TreasureCharmRegistry.get(resource);
			if (!charm.isPresent()) {
				throw new Exception(String.format("Unable to locate charm %s in registry.", resource.toString()));
			}
		}
		catch(Exception e) {
			Treasure.logger.error("Unable to read state to NBT:", e);
		}	
		/*
		 * This code would only be needed if a modifying system AFTER a Charm has been create was in effect.
		CharmBuilder builder = new CharmBuilder(tag.getString("name"), CharmType.valueOf(typeStr), CharmLevel.valueOf(levelStr));
		builder.valueModifier(tag.getDouble("valueModifier")).percentModifier(tag.getDouble("percentModifier"))
		.percentModifier(tag.getDouble("durationModifier"));
		return builder.build();
		 */
		return charm;
	}

	/**
	 * 
	 * @param tag
	 * @return
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
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

	public double getMaxPercent() {
		return maxPercent;
	}

	@Override
	public int getMaxDuration() {
		return maxDuration;
	}

	/**
	 * 
	 * @author Mark Gottschling on Dec 18, 2020
	 *
	 */
	public static class Builder {
		private ResourceLocation name;
		private final String type;
		private final Integer level;
		private Double value = 0.0;
		private Double duration = 0.0;
		private Double percent = 0.0;

		private Class<? extends ICharm> charmClass;

		/**
		 * 
		 * @param name
		 * @param type
		 * @param level
		 * @param charmClass
		 */
		public Builder(ResourceLocation name, String type, Integer level, Class<? extends ICharm> charmClass) {
			this.name = name;
			this.type = type;
			this.level = level;
			this.charmClass = charmClass;
		}

		public ICharm build() {
			ICharm charm = null;
			try {
				charm = charmClass.getDeclaredConstructor(Builder.class).newInstance(Charm.Builder.this);
				Treasure.logger.debug("building charm from -> {} to -> {}", Charm.Builder.this.toString(), charm.toString());
			} catch (Exception e) {
				Treasure.logger.error("Couldn't create charm", e);
			}			

			return charm;
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

		@Override
		public String toString() {
			return "Builder [name=" + name + ", type=" + type + ", level=" + level + ", value="
					+ value + ", duration=" + duration + ", percent=" + percent + ", charmClass=" + charmClass + "]";
		}
	}

	@Override
	public String toString() {
		return "Charm [name=" + name + ", type=" + type + ", level=" + level + ", maxValue=" + maxValue
				+ ", maxPercent=" + maxPercent + ", maxDuration=" + maxDuration + "]";
	}

}
