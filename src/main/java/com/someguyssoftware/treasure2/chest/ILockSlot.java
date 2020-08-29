/**
 * 
 */
package com.someguyssoftware.treasure2.chest;

import com.someguyssoftware.gottschcore.spatial.Heading;
import com.someguyssoftware.gottschcore.spatial.Rotate;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.nbt.CompoundNBT;

/**
 * @author Mark Gottschling onJan 10, 2018
 *
 */
public interface ILockSlot {
	/**
	 * 
	 * @param parentNBT
	 */
	public static ILockSlot readFromNBT(CompoundNBT nbt) {
		int index = -1;
		int face = -1;
		float x = -0F;
		float y = -0F;
		float z = 0F;
		float rotate = 0F;
		ILockSlot slot = null;
		
		if (nbt.contains("index")) {
			index = nbt.getInt("index");
		}
		if (nbt.contains("face")) {
			face = nbt.getInt("face");
//			Treasure.LOGGER.info("ILockSlot | readFromNBT | read face -> {}", face);
		}
		if (nbt.contains("x")) {
			x = nbt.getFloat("x");
		}
		if (nbt.contains("y")) {
			y = nbt.getFloat("y");
		}
		if (nbt.contains("z")) {
			z = nbt.getFloat("z");
		}
		if (nbt.contains("rotation")) {
			rotate = nbt.getFloat("rotation");
		}
//		Treasure.LOGGER.info("LockSlot |readNBT | Heading.getByIndex({}) -> {}", face, Heading.getByIndex(face));
		slot = new LockSlot(index, Heading.getByIndex(face), x, y, z, rotate);
		return slot;
	}
	
	/**
	 * 
	 * @param nbt
	 * @return
	 */
	public CompoundNBT writeToNBT(CompoundNBT nbt);

	Heading getFace();
	void setFace(Heading face);

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