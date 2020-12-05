package com.someguyssoftware.treasure2.tileentity;

import net.minecraft.util.text.translation.I18n;

/**
 * 
 * @author Mark Gottschling on Jan 19, 2018
 *
 */
public class IronboundChestTileEntity extends AbstractTreasureChestTileEntity {
	
	/**
	 * 
	 * @param texture
	 */
	public IronboundChestTileEntity() {
		super();
		setCustomName(I18n.translateToLocal("display.ironbound_chest.name"));
	}
}
