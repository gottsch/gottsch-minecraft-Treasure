/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.nbt.NBTTagCompound;

/**
 * @author Mark Gottschling on Apr 25, 2020
 */
public abstract class Charm implements ICharm {
	public static final int SECOND_IN_TICKS = 20;
    // TODO revisit the name.  it should be a ResourceLocation so it can be expanded upon by modders.
    // TODO revisit how it is NBT read/write with legacy support
    // TODO then update TreasureCharmRegistry
	private String name;
	private ResourceLocation name2;
	private String type;
	private int level;
	private double maxValue;
	private double maxPercent;
	private int maxDuration;

    // [x]TODO a Charm should know how to create it's own data, like a Block knows how to create a tile entity ie CharmEntity/**CharmData
    // [x] rename CharmState to CharmInstance; CharmInstance -> (Charm, CharmData);
    // [x]rename CharmVitals to CharmData
    // [x]remove CharmStateFactory - move factory into Charm
	
	/**
	 * 
	 * @param builder
	 */
	@Deprecated
	protected Charm(ICharmBuilder builder) {
//		this.name = builder.getName();
//		this.type = builder.getType();
//		this.level = builder.getLevel();
//		this.valueModifier = builder.getCharmValueModifier();
//		this.percentModifier = builder.getCharmPercentModifier();
//		this.durationModifier = builder.getCharmDurationModifier();
//		
//		this.maxValue = (int) (type.getBaseValues()[level.getValue()-1] * valueModifier);
//		this.maxPercent = type.getBasePercents()[level.getValue()-1] * percentModifier;
//		this.maxDuration = (int) (type.getBaseDurations()[level.getValue()-1] * durationModifier);
//		
	}
	
	/**
	 * 
	 * @param builder
	 */
	protected Charm(Builder builder) {
		this.name = builder.name;
		this.namne2 = builder.name2;
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
    @Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag, ICharmDatat data) {
        TextFormatting color = TextFormatting.RED;
        tooltip.add("  " + color + I18n.translateToLocalFormatted("tooltip.charm." + getName().toLowerCase()));
    }

	/**
	 * This method reads only this Charm's properties from an NBT tag
	 * 
	 * @param tag
	 */
	public static ICharm readFromNBT(NBTTagCompound tag) {
		ICharm charm = null;
		// read the name of the charm and fetch from the registry
		try {
			String charmName = tag.getString("name");
			charm = TreasureCharms.REGISTRY.get(charmName);
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
			nbt.setString("name", this.name);
		}
		catch(Exception e) {
			Treasure.logger.error("Unable to write state to NBT:", e);
		}
		return nbt;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public ResourceLocation getName2() {
		return name2;
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



//	public CharmType getType() {
//		return type;
//	}
//
//	public CharmLevel getLevel() {
//		return level;
//	}
//
//	public double getValueModifier() {
//		return valueModifier;
//	}
//
//
//	public double getPercentModifier() {
//		return percentModifier;
//	}
//
//	public void setMaxPercent(double maxPercent) {
//		this.maxPercent = maxPercent;
//	}
//
//	public double getDurationModifier() {
//		return durationModifier;
//	}
//
//	@Override
//	public String toString() {
//		return "Charm [type=" + type + ", level=" + level + ", name=" + name + ", maxValue=" + maxValue
//				+ ", maxPercent=" + maxPercent + ", maxDuration=" + maxDuration + ", valueModifier=" + valueModifier
//				+ ", percentModifier=" + percentModifier + ", durationModifier=" + durationModifier + "]";
//	}
	
	/**
	 * 
	 * @author Mark Gottschling on Dec 18, 2020
	 *
	 */
	public static class Builder {
		private final String name;
		private final ResourceLocation name2;
		private final String type;
		private final Integer level;
		private Double value;
		private Double duration;
		private Double percent;
		
		private Class<? extends ICharm> charmClass;
		
		public Builder(String name, String type, Integer level, Class<? extends ICharm> charmClass) {
			this.name = name;
			this.name2 = new ResourceLocation(Treasure.MODID, name);
			this.type = type;
			this.level = level;
			this.charmClass = charmClass;
		}
		
		public Builder(ResourceLocation name, String type, Integer level, Class<? extends ICharm> charmClass) {
			this.name2 = name;
			this.type = type;
			this.level = level;
			this.charmClass = charmClass;
		}
		
		public ICharm build() {
			ICharm charm = null;
			try {
				charm = charmClass.getDeclaredConstructor(Builder.class).newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}			
			Treasure.logger.debug("building charm from -> {} to -> {}", this.toString(), charm.toString());
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
	}
}
