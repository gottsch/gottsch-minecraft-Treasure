package com.someguyssoftware.treasure2.tileentity;

import net.minecraft.util.text.TranslationTextComponent;

/**
 * 
 * @author Mark Gottschling on Jan 19, 2018
 *
 */
public class MoldyCrateChestTileEntity extends CrateChestTileEntity {

	@SuppressWarnings("deprecation")
	public MoldyCrateChestTileEntity() {
		super(TreasureTileEntities.moldyCrateChestTileEntityType);
		setCustomName(new TranslationTextComponent("display.crate_chest_moldy.name"));
	}
}
