package com.someguyssoftware.treasure2.tileentity;

import net.minecraft.util.text.translation.I18n;

/**
 * 
 * @author Mark Gottschling on Jan 19, 2018
 *
 */
public class MoldyCrateChestTileEntity extends CrateChestTileEntity {

	@SuppressWarnings("deprecation")
	public MoldyCrateChestTileEntity() {
		super();
		setCustomName(I18n.translateToLocal("display.crate_chest_moldy.name"));
	}
}
