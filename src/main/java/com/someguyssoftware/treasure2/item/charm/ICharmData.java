/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

import net.minecraft.nbt.NBTTagCompound;

/**
 * @author Mark Gottschling on Apr 27, 2020
 *
 */
@Deprecated
public interface ICharmData {
	public double getValue();
	public void setValue(double value);
	
	public int getDuration();
	public void setDuration(int duration);
	
	public double getPercent();
	public void setPercent(double percent);
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt);
	public void readFromNBT(NBTTagCompound tag);
}
