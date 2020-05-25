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
 * @author Mark Gottschling on May 5, 2020
 *
 */
public class IlluminationCharmVitals extends CharmVitals {
	private List<ICoords> coordsList;

	/**
	 * 
	 */
	public IlluminationCharmVitals() {
		super();
		coordsList = Collections.synchronizedList(new LinkedList<>());
	}
	
	/**
	 * 
	 * @param charm
	 */
	public IlluminationCharmVitals(ICharm charm) {
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
	public IlluminationCharmVitals(double value, int duration, double percent) {
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
		NBTTagList list = nbt.getTagList("illuminationCoords", 10);
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
			NBTTagList list = new NBTTagList();
			synchronized (coordsList) {
				for (ICoords coords : coordsList) {
					// create a new nbt
					NBTTagCompound coordsTag = new NBTTagCompound();
					coords.writeToNBT(coordsTag);
					list.appendTag(coordsTag);
				}
			}
			nbt.removeTag("illuminationCoords");
			nbt.setTag("illuminationCoords", list);
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
	public List<ICoords> getCoordsList() {
		if (coordsList == null) {
			coordsList = new LinkedList<>();
		}
		return coordsList;
	}

	/**
	 * 
	 * @param blockList
	 */
	public void setCoordsList(List<ICoords> blockList) {
		this.coordsList = blockList;
	}

	@Override
	public String toString() {
		return "IlluminationCharmVitals [coordsList=" + coordsList + ", toString()=" + super.toString() + "]";
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
		IlluminationCharmVitals other = (IlluminationCharmVitals) obj;
		if (coordsList == null) {
			if (other.coordsList != null)
				return false;
		} else if (!coordsList.equals(other.coordsList))
			return false;
		return true;
	}

}
