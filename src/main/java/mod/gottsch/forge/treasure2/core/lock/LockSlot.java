/*
 * This file is part of  Treasure2.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.forge.treasure2.core.lock;

import mod.gottsch.forge.gottschcore.spatial.Heading;
import mod.gottsch.forge.gottschcore.spatial.Rotate;
import mod.gottsch.forge.treasure2.Treasure;
import net.minecraft.nbt.CompoundTag;

/**
 * Defines a slot on a chest where a lock is placed.
 * Used in TreasureChestTypes to define the slots on different chest types.
 * Used in TreasureChestTileEntity to save current orientation of occupied slots.
 * @author Mark Gottschling on Jan 9, 2018
 *
 */
public class LockSlot implements ILockSlot {
	private int index;
	private Heading face;
	private float xOffset;
	private float yOffset;
	private float zOffset;
	private float rotation;
	
	/**
	 * Empty constructor
	 */
	public LockSlot() {	
	}
	
	/**
	 * 
	 */
	public LockSlot(int index, Heading face, float x, float y, float z, float rotation) {
		setIndex(index);
		setFace(face);
		setXOffset(x);
		setYOffset(y);
		setZOffset(z);
		setRotation(rotation);
	}

	/**
	 * 
	 * @param r
	 * @return
	 */
	@Override
	public ILockSlot rotate(Rotate r) {
		Heading newFace = getFace().rotateY(r);
		float x = 0F;
		float z = 0F;
		float rotation = 0F;
	
		/*
		 *  NOTE this currently only works for a 1x1 standard cube size. See Treasure1710 for making generic
		 *  method to calculate rotation position/shapes on multicube/irregular shaped blocks.
		 */
		// switch on the rotation
		switch(r) {
		case ROTATE_90:
			// switch on the base
			switch (this.getFace()) {
			case NORTH:
				x = 1 - getZOffset();
				z = getXOffset();
				rotation = 90F;
				break;
			case EAST:
				x = getZOffset();
				z = getXOffset();
				rotation = 180F;
				break;
			case SOUTH:
				x = 1 - getZOffset();
				z = getXOffset();
				rotation = -90F;
				break;
			case WEST:
				x = getZOffset();
				z = getXOffset();
				rotation = 0F;
				break;
			default:
				break;			
			}
			break;
		case ROTATE_180:
			switch (this.getFace()) {
			case NORTH:
				x = 1 - getXOffset();
				z = 1 - getZOffset();
				rotation = 180F;				
			case SOUTH:
				x = 1 - getXOffset();
				z = 1 - getZOffset();
				rotation = 180F;
				break;
			case EAST:
				x = 1 - getXOffset();
				z = getZOffset();
				rotation = -90;
			case WEST:
				x = 1 - getXOffset();
				z = getZOffset();
				rotation = 90;
				break;
			default:
				break;			
			}
			break;		
		case ROTATE_270:
			switch (this.getFace()) {
			case NORTH:
				x = getZOffset();
				z = 1-getXOffset();
				rotation = -90;
				break;
			case EAST:
				x = getZOffset();
				z = 1 - getXOffset();
				rotation = 0;
				break;
			case SOUTH:
				x = getZOffset();
				z = 1 - getXOffset();
				rotation = 90;
				break;
			case WEST:
				x = getZOffset();
				z = 1- getXOffset();
				rotation = 180;
				break;
			default:
				break;			
			}
			break;
		default:
			break;	
		}		
		ILockSlot slot = new LockSlot(getIndex(), newFace, x, getYOffset(), z, rotation);
		return slot;
	}
	
	/**
	 * 
	 * @param nbt
	 * @return
	 */
	public ILockSlot load(CompoundTag nbt) {
		int index = -1;
		int face = -1;
		float x = -0F;
		float y = -0F;
		float z = 0F;
		float rotate = 0F;
		
		if (nbt.contains("index")) {
			index = nbt.getInt("index");
			setIndex(index);
		}
		if (nbt.contains("face")) {
			face = nbt.getInt("face");
			setFace(Heading.getByIndex(face));
//			Treasure.LOGGER.info("ILockSlot | readFromNBT | read face -> {}", face);
		}
		if (nbt.contains("x")) {
			x = nbt.getFloat("x");
			setXOffset(x);
		}
		if (nbt.contains("y")) {
			y = nbt.getFloat("y");
			setYOffset(y);
		}
		if (nbt.contains("z")) {
			z = nbt.getFloat("z");
			setZOffset(z);
		}
		if (nbt.contains("rotation")) {
			rotate = nbt.getFloat("rotation");
			setRotation(rotate);
		}
//		Treasure.LOGGER.info("LockSlot |readNBT | Heading.getByIndex({}) -> {}", face, Heading.getByIndex(face));
		return this;
	}
	
	/**
	 * 
	 * @param nbt
	 * @return
	 */
	@Override
	public CompoundTag save(CompoundTag nbt) {
		try {
			nbt.putInt("index", getIndex());
			nbt.putInt("face", getFace().getIndex());
//			Treasure.LOGGER.info("LockSlot | writeToNBT | wrote face -> {}", nbt.get("face"));
			nbt.putFloat("x", getXOffset());
			nbt.putFloat("y", getYOffset());
			nbt.putFloat("z", getZOffset());
			nbt.putFloat("rotation", getRotation());
		}
		catch(Exception e) {
			Treasure.LOGGER.error("Unable to write state to NBT:", e);
		}		
		return nbt;
	}
	
	/**
	 * @return the face
	 */
	@Override
	public Heading getFace() {
		return face;
	}

	/**
	 * @param face the face to set
	 */
	@Override
	public void setFace(Heading face) {
		this.face = face;
	}

	/**
	 * @return the xOffset
	 */
	@Override
	public float getXOffset() {
		return xOffset;
	}

	/**
	 * @param xOffset the xOffset to set
	 */
	@Override
	public void setXOffset(float xOffset) {
		this.xOffset = xOffset;
	}

	/**
	 * @return the yOffset
	 */
	@Override
	public float getYOffset() {
		return yOffset;
	}

	/**
	 * @param yOffset the yOffset to set
	 */
	@Override
	public void setYOffset(float yOffset) {
		this.yOffset = yOffset;
	}

	/**
	 * @return the zOffset
	 */
	@Override
	public float getZOffset() {
		return zOffset;
	}

	/**
	 * @param zOffset the zOffset to set
	 */
	@Override
	public void setZOffset(float zOffset) {
		this.zOffset = zOffset;
	}

	/**
	 * @return the index
	 */
	@Override
	public int getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	@Override
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return the rotation
	 */
	@Override
	public float getRotation() {
		return rotation;
	}

	/**
	 * @param rotation the rotation to set
	 */
	@Override
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LockSlot [index=" + index + ", face=" + face + ", xOffset=" + xOffset + ", yOffset=" + yOffset
				+ ", zOffset=" + zOffset + ", rotation=" + rotation + "]";
	}
	
}
