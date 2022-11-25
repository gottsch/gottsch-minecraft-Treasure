/*
 * This file is part of  Treasure2.
 * Copyright (c) 2018 Mark Gottschling (gottsch)
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

/**
 * @author Mark Gottschling on Jan 9, 2018
 *
 */
public class LockLayouts {
	public static final LockLayout NONE;
	public static final LockLayout CRATE;
	public static final LockLayout STANDARD;
	public static final LockLayout STANDARD2;
	public static final LockLayout SINGLE_STANDARD;
	public static final LockLayout STRONGBOX;
	public static final LockLayout SAFE;
	public static final LockLayout COMPRESSOR;
	public static final LockLayout ARMOIRE;
	public static final LockLayout SKULL;
	public static final LockLayout TOP_SPLIT;
	public static final LockLayout LOW_RISE;
	public static final LockLayout VIKING;
	public static final LockLayout MILK_CRATE;
		
	static {
		NONE = new LockLayout(0);
		
		STANDARD = new LockLayout(3).addSlots(
			new LockSlot(0, Heading.NORTH, 0.5F, 0.3F, 0.05F, 0F),
			new LockSlot(1, Heading.EAST, 0.95F, 0.3F, 0.5F, 90F),
			new LockSlot(2, Heading.WEST, 0.05F, 0.3F, 0.5F, -90F)
		);
		
		STANDARD2 = new LockLayout(3).addSlots(
				new LockSlot(0, Heading.NORTH, 0.5F, 0.2F, 0.05F, 0F),
				new LockSlot(1, Heading.EAST, 0.95F, 0.3F, 0.5F, 90F),
				new LockSlot(2, Heading.WEST, 0.05F, 0.3F, 0.5F, -90F)
			);
		
		SINGLE_STANDARD = new LockLayout(1).addSlots(
				new LockSlot(0, Heading.NORTH, 0.5F, 0.3F, 0.05F, 0F)
			);
		
		CRATE = new LockLayout(1).addSlots(
				new LockSlot(0, Heading.NORTH, 0.5F, 0.6F, 0.05F, 0F)
			);
		
		MILK_CRATE = new LockLayout(1).addSlots(
				new LockSlot(0, Heading.NORTH, 0.5F, 0.412F, 0.15F, 0F)		
				);
		
		STRONGBOX = new LockLayout(3).addSlots(
			new LockSlot(0, Heading.NORTH, 0.5F, 0.2F, 0.20F, 0F),
			new LockSlot(1, Heading.EAST, 0.825F, 0.2F, 0.5F, 90F),
			new LockSlot(2, Heading.WEST, 0.175F, 0.2F, 0.5F, -90F)			
		);
		
		SAFE = new LockLayout(2).addSlots(
			new LockSlot(0, Heading.NORTH, 0.25F, 0.6F, 0.1125F, 0F),
			new LockSlot(1, Heading.NORTH, 0.25F, 0.3F, 0.1125F, 0F)
		);
		
		// starts in the NW bottom corner and moves E and S and up, so (0,0,0) is NW corner and (1,1,1) is the SE corner top
		COMPRESSOR = new LockLayout(4).addSlots(
				new LockSlot(0, Heading.NORTH, 0.61F, 0.18F, 0.27F, 0F),
				new LockSlot(1, Heading.EAST, 0.73F, 0.18F, 0.61F, 90F),
				new LockSlot(2, Heading.WEST, 0.27F, 0.18F, 0.39F, -90F),
				new LockSlot(3, Heading.SOUTH, 0.39F, 0.18F, 0.73F, 0F)
			);
		
		ARMOIRE = new LockLayout(2).addSlots(
				new LockSlot(0, Heading.NORTH, 0.5F, 0.95F, 0.05F, 0F),
				new LockSlot(1, Heading.NORTH, 0.5F, 0.4F, 0.05F, 0F)
			);
		
		SKULL = new LockLayout(1).addSlots(
				new LockSlot(0, Heading.EAST, 0.72F, 0.18F, 0.5F, 90F)
			);
		
		// NOTE the locks are meant to be on top of the chest, but they will not get rotated properly if Heading.UP.
		// Use the Heading that the locks should initially face and have the renderer implementation render the locks on the correct axis.
		TOP_SPLIT = new LockLayout(2).addSlots(
				new LockSlot(0, Heading.NORTH, 0.5F, 0.95F, 0.3F, 0F),
				new LockSlot(1, Heading.NORTH, 0.5F, 0.95F, 0.7F, 0F)
			);
		
		LOW_RISE = new LockLayout(3).addSlots(
				new LockSlot(0, Heading.NORTH, 0.5F, 0.2F, 0.05F, 0F),
				new LockSlot(1, Heading.EAST, 0.88F, 0.2F, 0.5F, 90F),
				new LockSlot(2, Heading.WEST, 0.12F, 0.2F, 0.5F, -90F)			
			);
		
		VIKING = new LockLayout(1).addSlots(
				new LockSlot(0, Heading.NORTH, 0.5F, 0.55F, 0.17F, 0F)
			);
	}
}
