/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.nbt.NBTTagCompound;

/**
 * @author Mark Gottschling on Apr 27, 2020
 *
 */
public class CharmVitals implements ICharmVitals {
	private double value;
	private int duration;
	private double percent;
	
	/**
	 * 
	 */
	public CharmVitals() {};
	
	/**
	 * 
	 * @param value
	 * @param duration
	 * @param percent
	 */
	public CharmVitals(double value, int duration, double percent) {
		this.value = value;
		this.duration = duration;
		this.percent = percent;
	}
	
	/**
	 * 
	 * @param vitals
	 */
	public CharmVitals(ICharmVitals vitals) {
		this.value = vitals.getValue();
		this.duration = vitals.getDuration();
		this.percent = vitals.getPercent();
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("duration")) {
			this.duration = nbt.getInteger("duration");
		}
		if (nbt.hasKey("value")) {
			this.value = nbt.getInteger("value");
		}
		if (nbt.hasKey("percent")) {
			this.percent = nbt.getDouble("percent");
		}
	}
	
	/**
	 * 
	 * @param tag
	 * @return
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		try {
			nbt.setInteger("duration", this.getDuration());
			nbt.setDouble("value",this.getValue());
			nbt.setDouble("percent", this.getPercent());
		}
		catch(Exception e) {
			Treasure.logger.error("Unable to write state to NBT:", e);
		}
		return nbt;
	}
	
	@Override
	public double getValue() {
		return value;
	}

	@Override
	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public int getDuration() {
		return duration;
	}

	@Override
	public void setDuration(int duration) {
		this.duration = duration;
	}

	@Override
	public double getPercent() {
		return percent;
	}

	@Override
	public void setPercent(double percent) {
		this.percent = percent;
	}

	@Override
	public String toString() {
		return "CharmVitals [value=" + value + ", duration=" + duration + ", percent=" + percent + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + duration;
		long temp;
		temp = Double.doubleToLongBits(percent);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(value);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		CharmVitals other = (CharmVitals) obj;
		if (duration != other.duration)
			return false;
		if (Double.doubleToLongBits(percent) != Double.doubleToLongBits(other.percent))
			return false;
		if (Double.doubleToLongBits(value) != Double.doubleToLongBits(other.value))
			return false;
		return true;
	}

}
