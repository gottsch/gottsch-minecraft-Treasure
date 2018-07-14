/**
 * 
 */
package com.someguyssoftware.treasure2.loot;

import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;

/**
 * @author Mark Gottschling on Jun 30, 2018
 *
 */
public class TreasureLootTable extends LootTable {

	/**
	 * @param poolsIn
	 */
	public TreasureLootTable(LootPool[] poolsIn) {
		super(poolsIn);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TreasureLootTable [isFrozen()=" + isFrozen() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

}
