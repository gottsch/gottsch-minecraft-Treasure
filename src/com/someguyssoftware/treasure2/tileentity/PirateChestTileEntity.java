package com.someguyssoftware.treasure2.tileentity;

import net.minecraft.util.text.translation.I18n;

/**
 * 
 * @author Mark Gottschling on Jan 19, 2018
 *
 */
public class PirateChestTileEntity extends AbstractTreasureChestTileEntity {
	
	/**
	 * 
	 * @param texture
	 */
	public PirateChestTileEntity() {
		super();
		setCustomName(I18n.translateToLocal("display.pirate_chest.name"));
	}
}
