package com.someguyssoftware.treasure2.tileentity;

import net.minecraft.util.text.translation.I18n;

/**
 * 
 * @author Mark Gottschling on Apr 19, 2020
 *
 */
public class VikingChestTileEntity extends AbstractTreasureChestTileEntity {
	
	/**
	 * 
	 * @param texture
	 */
	public VikingChestTileEntity() {
		super();
		setCustomName(I18n.translateToLocal("display.viking_chest.name"));
	}
}
