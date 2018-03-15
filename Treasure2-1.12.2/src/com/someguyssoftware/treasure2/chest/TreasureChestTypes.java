/**
 * 
 */
package com.someguyssoftware.treasure2.chest;

import com.someguyssoftware.gottschcore.enums.Direction;

/**
 * @author Mark Gottschling on Jan 9, 2018
 *
 */
public class TreasureChestTypes {
	public static final TreasureChestType CRATE;
	public static TreasureChestType STANDARD;
	public static TreasureChestType STANDARD2;
	public static TreasureChestType STRONGBOX;
	public static TreasureChestType SAFE;
	public static TreasureChestType SINGLE;
	public static TreasureChestType POT;

		
	static {
		STANDARD = new TreasureChestType(3).addSlots(
			new LockSlot(0, Direction.NORTH, 0.5F, 0.3F, 0.05F, 0F),
			new LockSlot(1, Direction.EAST, 0.95F, 0.3F, 0.5F, 90F),
			new LockSlot(2, Direction.WEST, 0.05F, 0.3F, 0.5F, -90F)
		);
		
		STANDARD2 = new TreasureChestType(3).addSlots(
				new LockSlot(0, Direction.NORTH, 0.5F, 0.2F, 0.05F, 0F),
				new LockSlot(1, Direction.EAST, 0.95F, 0.3F, 0.5F, 90F),
				new LockSlot(2, Direction.WEST, 0.05F, 0.3F, 0.5F, -90F)
			);
		
		CRATE = new TreasureChestType(1).addSlots(
				new LockSlot(0, Direction.NORTH, 0.5F, 0.6F, 0.05F, 0F)
			);
		
		STRONGBOX = new TreasureChestType(3).addSlots(
			new LockSlot(0, Direction.NORTH, 0.5F, 0.2F, 0.20F, 0F),
			new LockSlot(1, Direction.EAST, 0.825F, 0.2F, 0.5F, 90F),
			new LockSlot(2, Direction.WEST, 0.175F, 0.2F, 0.5F, -90F)			
		);
		
		SAFE = new TreasureChestType(2).addSlots(
			new LockSlot(0, Direction.NORTH, 0.25F, 0.6F, 0.1125F, 0F),
			new LockSlot(1, Direction.NORTH, 0.25F, 0.3F, 0.1125F, 0F)
		);		
	}
}
