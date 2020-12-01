package com.someguyssoftware.treasure2.tileentity;

import net.minecraft.util.text.translation.I18n;

/**
 * 
 * @author Mark Gottschling on Nov 30, 2020
 *
 */
public class MilkCrateTileEntity extends AbstractTreasureChestTileEntity {
	
	/**
	 * 
	 * @param texture
	 */
	public MilkCrateTileEntity() {
		super();
		setCustomName(I18n.translateToLocal("display.milk_crate.name"));
	}
}
