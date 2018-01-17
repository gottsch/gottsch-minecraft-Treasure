/**
 * 
 */
package com.someguyssoftware.treasure2.chest;

import com.someguyssoftware.gottschcore.enums.Direction;
import com.someguyssoftware.gottschcore.enums.Rotate;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;

import net.minecraft.nbt.NBTTagCompound;

/**
 * @author Mark Gottschling onJan 10, 2018
 *
 */
public interface ILockSlot {
	/**
	 * 
	 * @param parentNBT
	 */
	public static ILockSlot readFromNBT(NBTTagCompound nbt) {
		int index = -1;
		int face = -1;
		float x = -0F;
		float y = -0F;
		float z = 0F;
		float rotate = 0F;
		ILockSlot slot = null;
		
		if (nbt.hasKey("index")) {
			index = nbt.getInteger("index");
		}
		if (nbt.hasKey("face")) {
			face = nbt.getInteger("face");
		}
		if (nbt.hasKey("x")) {
			x = nbt.getFloat("x");
		}
		if (nbt.hasKey("y")) {
			y = nbt.getFloat("y");
		}
		if (nbt.hasKey("z")) {
			z = nbt.getFloat("z");
		}
		if (nbt.hasKey("rotation")) {
			rotate = nbt.getFloat("rotation");
		}
		// TODO add rotation
		slot = new LockSlot(index, Direction.getByCode(face), x, y, z, rotate);
		return slot;
	}
	
	/**
	 * 
	 * @param nbt
	 * @return
	 */
	public NBTTagCompound writeToNBT(NBTTagCompound nbt);

	Direction getFace();
	void setFace(Direction face);

	float getXOffset();
	void setXOffset(float xOffset);
	
	float getYOffset();
	void setYOffset(float xOffset);
	
	float getZOffset();
	void setZOffset(float xOffset);

	int getIndex();
	void setIndex(int index);

	float getRotation();
	void setRotation(float rotation);

	ILockSlot rotate(Rotate r);
	
}
