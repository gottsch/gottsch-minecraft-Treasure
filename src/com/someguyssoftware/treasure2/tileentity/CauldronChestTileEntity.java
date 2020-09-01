package com.someguyssoftware.treasure2.tileentity;

import net.minecraft.util.text.translation.I18n;

/**
 * 
 * @author Mark Gottschling on Jan 28, 2019
 *
 */
public class CauldronChestTileEntity extends AbstractTreasureChestTileEntity {
	
	/**
	 * 
	 * @param texture
	 */
	public CauldronChestTileEntity() {
		super();
		setCustomName(I18n.translateToLocal("display.cauldron_chest.name"));
	}
}