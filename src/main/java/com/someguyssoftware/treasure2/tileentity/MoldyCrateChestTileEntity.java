package com.someguyssoftware.treasure2.tileentity;

import net.minecraft.network.chat.TranslatableComponent;

/**
 * 
 * @author Mark Gottschling on Jan 19, 2018
 *
 */
public class MoldyCrateChestTileEntity extends CrateChestTileEntity {

	@SuppressWarnings("deprecation")
	public MoldyCrateChestTileEntity() {
		super(TreasureTileEntities.MOLDY_CRATE_CHEST_TILE_ENTITY_TYPE);
		setCustomName(new TranslatableComponent("display.crate_chest_moldy.name"));
	}
}
