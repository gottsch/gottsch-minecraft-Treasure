/**
 * 
 */
package com.someguyssoftware.treasure2.chest;

import com.someguyssoftware.gottschcore.spatial.Heading;

/**
 * @author Mark Gottschling on Jan 9, 2018
 *
 */
public class TreasureChestTypes {
	// TODO change this class to enum
	public static final TreasureChestType CRATE;
	public static final TreasureChestType STANDARD;
	public static final TreasureChestType STANDARD2;
	public static final TreasureChestType SINGLE_STANDARD;
	public static final TreasureChestType STRONGBOX;
	public static final TreasureChestType SAFE;
	public static final TreasureChestType COMPRESSOR;
	public static final TreasureChestType ARMOIRE;
	public static final TreasureChestType SKULL;
	public static final TreasureChestType TOP_SPLIT;
	public static final TreasureChestType LOW_RISE;
	public static final TreasureChestType VIKING;

		
	static {
		STANDARD = new TreasureChestType(3).addSlots(
			new LockSlot(0, Heading.NORTH, 0.5F, 0.3F, 0.05F, 0F),
			new LockSlot(1, Heading.EAST, 0.95F, 0.3F, 0.5F, 90F),
			new LockSlot(2, Heading.WEST, 0.05F, 0.3F, 0.5F, -90F)
		);
		
		STANDARD2 = new TreasureChestType(3).addSlots(
				new LockSlot(0, Heading.NORTH, 0.5F, 0.2F, 0.05F, 0F),
				new LockSlot(1, Heading.EAST, 0.95F, 0.3F, 0.5F, 90F),
				new LockSlot(2, Heading.WEST, 0.05F, 0.3F, 0.5F, -90F)
			);
		
		SINGLE_STANDARD = new TreasureChestType(1).addSlots(
				new LockSlot(0, Heading.NORTH, 0.5F, 0.3F, 0.05F, 0F)
			);
		
		CRATE = new TreasureChestType(1).addSlots(
				new LockSlot(0, Heading.NORTH, 0.5F, 0.6F, 0.05F, 0F)
			);
		
		STRONGBOX = new TreasureChestType(3).addSlots(
			new LockSlot(0, Heading.NORTH, 0.5F, 0.2F, 0.20F, 0F),
			new LockSlot(1, Heading.EAST, 0.825F, 0.2F, 0.5F, 90F),
			new LockSlot(2, Heading.WEST, 0.175F, 0.2F, 0.5F, -90F)			
		);
		
		SAFE = new TreasureChestType(2).addSlots(
			new LockSlot(0, Heading.NORTH, 0.25F, 0.6F, 0.1125F, 0F),
			new LockSlot(1, Heading.NORTH, 0.25F, 0.3F, 0.1125F, 0F)
		);
		
		// starts in the NW bottom corner and moves E and S and up, so (0,0,0) is NW corner and (1,1,1) is the SE corner top
		COMPRESSOR = new TreasureChestType(4).addSlots(
				new LockSlot(0, Heading.NORTH, 0.61F, 0.18F, 0.27F, 0F),
				new LockSlot(1, Heading.EAST, 0.73F, 0.18F, 0.61F, 90F),
				new LockSlot(2, Heading.WEST, 0.27F, 0.18F, 0.39F, -90F),
				new LockSlot(3, Heading.SOUTH, 0.39F, 0.18F, 0.73F, 0F)
			);
		
		ARMOIRE = new TreasureChestType(2).addSlots(
				new LockSlot(0, Heading.NORTH, 0.5F, 0.95F, 0.05F, 0F),
				new LockSlot(1, Heading.NORTH, 0.5F, 0.4F, 0.05F, 0F)
			);
		
		SKULL = new TreasureChestType(1).addSlots(
				new LockSlot(0, Heading.EAST, 0.72F, 0.18F, 0.5F, 90F)
			);
		
		// NOTE the locks are meant to be on top of the chest, but they will not get rotated properly if Heading.UP.
		// Use the Heading that the locks should initially face and have the renderer implementation render the locks on the correct axis.
		TOP_SPLIT = new TreasureChestType(2).addSlots(
				new LockSlot(0, Heading.NORTH, 0.5F, 0.95F, 0.3F, 0F),
				new LockSlot(1, Heading.NORTH, 0.5F, 0.95F, 0.7F, 0F)
			);
		
		LOW_RISE = new TreasureChestType(3).addSlots(
				new LockSlot(0, Heading.NORTH, 0.5F, 0.2F, 0.05F, 0F),
				new LockSlot(1, Heading.EAST, 0.88F, 0.2F, 0.5F, 90F),
				new LockSlot(2, Heading.WEST, 0.12F, 0.2F, 0.5F, -90F)			
			);
		
		VIKING = new TreasureChestType(1).addSlots(
				new LockSlot(0, Heading.NORTH, 0.5F, 0.55F, 0.17F, 0F)
			);
	}
}
