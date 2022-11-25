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
import net.minecraft.nbt.CompoundTag;

/**
 * @author Mark Gottschling onJan 10, 2018
 *
 */
public interface ILockSlot {
	/**
	 * 
	 * @param parentNBT
	 */
	public ILockSlot load(CompoundTag nbt);
	
	/**
	 * 
	 * @param nbt
	 * @return
	 */
	public CompoundTag save(CompoundTag nbt);

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