/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.nbt.NBTTagCompound;

/**
 * @author Mark Gottschling on Apr 25, 2020
 *
 */
public abstract class Charm implements ICharm {
	public static final int SECOND_IN_TICKS = 20;
	
	private CharmType type;
	private CharmLevel level;
	private String name;
	private double maxValue;
	private double maxPercent;
	private int maxDuration;
	private double valueModifier;
	private double percentModifier;
	private double durationModifier;
	
	
	/**
	 * 
	 * @param builder
	 */
	protected Charm(ICharmBuilder builder) {
		this.name = builder.getName();
		this.type = builder.getType();
		this.level = builder.getLevel();
		this.valueModifier = builder.getCharmValueModifier();
		this.percentModifier = builder.getCharmPercentModifier();
		this.durationModifier = builder.getCharmDurationModifier();
		
		this.maxValue = (int) (type.getBaseValues()[level.getValue()-1] * valueModifier);
		this.maxPercent = type.getBasePercents()[level.getValue()-1] * percentModifier;
		this.maxDuration = (int) (type.getBaseDurations()[level.getValue()-1] * durationModifier);
		
	}
	
	/**
	 * This method reads only this Charm's properties from an NBT tag
	 * 
	 * @param tag
	 */
	public static ICharm readFromNBT(NBTTagCompound tag) {
		String typeStr = tag.getString("type");
		String levelStr = tag.getString("level");
		CharmBuilder builder = new CharmBuilder(tag.getString("name"), CharmType.valueOf(typeStr), CharmLevel.valueOf(levelStr));
		builder.valueModifier(tag.getDouble("valueModifier")).percentModifier(tag.getDouble("percentModifier"))
		.percentModifier(tag.getDouble("durationModifier"));
		return builder.build();
	}
	
	/**
	 * New Way takes in a MultiConsumer 
	 */
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn, CharmInfoAdder infoAdder) {
		infoAdder.update(stack, worldIn, tooptip, flagIn);
	}
	
	/**
	 * Old Way
	 */
	public void addCharmedInfo(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
	
	}
	
	/**
	 * 
	 * @param tag
	 * @return
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		try {
			if (this.type != null) {
				nbt.setString("type", this.type.name());
			}
			if (this.level != null) {
				nbt.setString("level", this.level.name());
			}
			nbt.setString("name", this.name);
			nbt.setDouble("maxValue", maxValue);
			nbt.setDouble("maxPercent", maxPercent);
			nbt.setDouble("maxDuration", maxDuration);
			nbt.setDouble("valueModifier", valueModifier);
			nbt.setDouble("percentModifier", percentModifier);
			nbt.setDouble("durationModifier", durationModifier);
		}
		catch(Exception e) {
			Treasure.logger.error("Unable to write state to NBT:", e);
		}
		return nbt;
	}
	
	@Override
	public CharmType getCharmType() {
		return type;
	}

	@Override
	public CharmLevel getCharmLevel() {
		return level;
	}

	@Override
	public String getName() {
		return name;
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



	public CharmType getType() {
		return type;
	}

	public CharmLevel getLevel() {
		return level;
	}

	public double getValueModifier() {
		return valueModifier;
	}


	public double getPercentModifier() {
		return percentModifier;
	}

	public void setMaxPercent(double maxPercent) {
		this.maxPercent = maxPercent;
	}

	public double getDurationModifier() {
		return durationModifier;
	}

	@Override
	public String toString() {
		return "Charm [type=" + type + ", level=" + level + ", name=" + name + ", maxValue=" + maxValue
				+ ", maxPercent=" + maxPercent + ", maxDuration=" + maxDuration + ", valueModifier=" + valueModifier
				+ ", percentModifier=" + percentModifier + ", durationModifier=" + durationModifier + "]";
	}
}
