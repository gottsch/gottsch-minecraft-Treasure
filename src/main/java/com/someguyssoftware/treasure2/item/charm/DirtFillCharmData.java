/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.charm.ICharm;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * @author Mark Gottschling on Dec 23, 2020
 *
 */
@Deprecated
public class DirtFillCharmData extends CharmData {
	private ICoords lastCoords;

	/**
	 * 
	 */
	public DirtFillCharmData() {
		super();
	}

	/**
	 * 
	 * @param charm
	 */
	public DirtFillCharmData(ICharm charm) {
		this();
		setValue(charm.getMaxValue());
		setDuration(charm.getMaxDuration());
		setPercent(charm.getMaxPercent());
	}

	/**
	 * 
	 * @param value
	 * @param duration
	 * @param percent
	 */
	public DirtFillCharmData(double value, int duration, double percent) {
		this();
		setValue(value);
		setDuration(duration);
		setPercent(percent);
	}
	
	/**
	 * 
	 * @param value
	 * @param duration
	 * @param percent
	 * @param coords
	 */
	public DirtFillCharmData(double value, int duration, double percent, ICoords coords) {
		this(value, duration, percent);
		setLastCoords(coords);
	}

	/**
	 * 
	 */
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);		
		
		if (!nbt.hasKey("lastCoords")) {
			return;
		}		
		NBTTagCompound coordsTag = (NBTTagCompound) nbt.getTag("lastCoords");
		ICoords lastCoords = new Coords(coordsTag.getInteger("x"), coordsTag.getInteger("y"), coordsTag.getInteger("z"));
		this.setLastCoords(lastCoords);
	}

	/**
	 * 
	 * @param tag
	 * @return
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt = super.writeToNBT(nbt);
		try {
			// create a new nbt
			NBTTagCompound coordsTag = new NBTTagCompound();
			if (this.getLastCoords() != null) {
				coordsTag.setInteger("x", this.getLastCoords().getX());
				coordsTag.setInteger("y", this.getLastCoords().getY());
				coordsTag.setInteger("z", this.getLastCoords().getZ());
				nbt.removeTag("lastCoords");
				nbt.setTag("lastCoords", coordsTag);
			}
		}
		catch(Exception e) {
			Treasure.logger.error("Unable to write state to NBT:", e);
		}
		return nbt;
	}

	/**
	 * 
	 * @return
	 */
	public ICoords getLastCoords() {
		return lastCoords;
	}

	/**
	 * 
	 * @param blockList
	 */
	public void setLastCoords(ICoords lastCoords) {
		this.lastCoords = lastCoords;
	}

	@Override
	public String toString() {
		return "DirtFillCharmData [lastCoords=" + lastCoords + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((lastCoords == null) ? 0 : lastCoords.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		DirtFillCharmData other = (DirtFillCharmData) obj;
		if (lastCoords == null) {
			if (other.lastCoords != null)
				return false;
		} else if (!lastCoords.equals(other.lastCoords))
			return false;
		return true;
	}
}
