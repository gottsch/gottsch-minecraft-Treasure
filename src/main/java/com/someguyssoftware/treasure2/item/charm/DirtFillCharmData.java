/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * @author Mark Gottschling on Dec 23, 2020
 *
 */
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
        // setLastCoords();
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
	 */
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);		
		NBTTagList list = nbt.getTagList("lastCoords", 10);
//		Treasure.logger.debug("illumination tag list size -> {}", list.tagCount());
		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound tag = list.getCompoundTagAt(i);
			ICoords coords = ICoords.readFromNBT(tag);
			if (coords != null) {
				getCoordsList().add(coords);
			}
		}
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
					coords.writeToNBT(coordsTag);
			
			nbt.removeTag("lastCoords");
			nbt.setTag("lastCoords", list);
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
		return "IlluminationCharmData [" + /*coordsList=" + coordsList + ",*/ " toString()=" + super.toString() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((coordsList == null) ? 0 : coordsList.hashCode());
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
		IlluminationCharmData other = (IlluminationCharmData) obj;
		if (coordsList == null) {
			if (other.coordsList != null)
				return false;
		} else if (!coordsList.equals(other.coordsList))
			return false;
		return true;
	}

}
