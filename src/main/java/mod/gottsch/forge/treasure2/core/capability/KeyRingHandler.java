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
package mod.gottsch.forge.treasure2.core.capability;

import mod.gottsch.forge.treasure2.Treasure;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

/**
 * 
 * @author Mark Gottschling on Nov 23, 2022
 *
 */
public class KeyRingHandler implements IKeyRingHandler {
	private static final String IS_OPEN_TAG = "isOpen";
	
	private boolean isOpen;
	
	@Override
	public boolean isOpen() {
		return isOpen;
	}
	@Override
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
	
	@Override
	public CompoundTag save() {
		CompoundTag tag = new CompoundTag();
		try {
			tag.putBoolean(IS_OPEN_TAG, isOpen());
		} catch (Exception e) {
			Treasure.LOGGER.error("unable to write state to NBT:", e);
		}
		return tag;
	}
	
	@Override
	public void load(Tag tag) {
		if (tag instanceof CompoundTag) {
			CompoundTag compound = (CompoundTag)tag;
			if (compound.contains(IS_OPEN_TAG)) {
				setOpen(compound.getBoolean(IS_OPEN_TAG));
			}
		}
	}
}
