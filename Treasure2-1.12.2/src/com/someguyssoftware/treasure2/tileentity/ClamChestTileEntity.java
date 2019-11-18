package com.someguyssoftware.treasure2.tileentity;

import net.minecraft.util.text.translation.I18n;

/**
 * 
 * @author Mark Gottschling on Aug 26, 2019
 *
 */
public class ClamChestTileEntity extends AbstractTreasureChestTileEntity {
	
	/**
	 * 
	 * @param texture
	 */
	public ClamChestTileEntity() {
		super();
		setCustomName(I18n.translateToLocal("display.clam_chest.name"));
	}
	
	/**
	 * @return the numberOfSlots
	 */
	@Override
	public int getNumberOfSlots() {
		return 1;
	}
}
