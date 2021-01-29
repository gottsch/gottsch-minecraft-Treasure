/**
 * 
 */
package com.someguyssoftware.treasure2.tileentity;

import com.someguyssoftware.gottschcore.tileentity.ProximitySpawnerTileEntity;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

/**
 * Wrapper on PromixitySpawnerTileEntity, providing a no-arg constructor that provides the tile entity type to the super.
 * Researching why I couldn't pass the TET with reflection....
 * @author Mark Gottschling on Jan 29, 2021
 *
 */
public class TreasureProximitySpawnerTileEntity extends ProximitySpawnerTileEntity {

	public TreasureProximitySpawnerTileEntity() {
		super(TreasureTileEntities.proximityTileEntityType);
	}
}
