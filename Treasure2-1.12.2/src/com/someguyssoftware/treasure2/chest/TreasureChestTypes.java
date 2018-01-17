/**
 * 
 */
package com.someguyssoftware.treasure2.chest;

import com.someguyssoftware.gottschcore.enums.Direction;
import com.someguyssoftware.treasure2.client.model.StandardChestModel;

/**
 * @author Mark Gottschling on Jan 9, 2018
 *
 */
public class TreasureChestTypes {
	public static TreasureChestType STANDARD;
	public static TreasureChestType SAFE;
	public static TreasureChestType SINGLE;
	public static TreasureChestType POT;
		
	static {
		STANDARD = new TreasureChestType(3).addSlots(
			new LockSlot(0, Direction.NORTH, 0.5F, 0.4F, 0.05F, 0F),
			new LockSlot(1, Direction.EAST, 0.95F, 0.4F, 0.5F, 90F),
			new LockSlot(2, Direction.WEST, 0.05F, 0.4F, 0.5F, -90F)
		);
		
		// TODO update the model
		SAFE = new TreasureChestType(2).addSlots(
			new LockSlot(0, Direction.NORTH, 0.5F, 0.4F, 0.05F, 0F),
			new LockSlot(1, Direction.WEST, 0.05F, 0.4F, 0.5F, -90F)
		);		
	}
}
