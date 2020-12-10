package com.someguyssoftware.treasure2.tileentity;

import net.minecraft.util.text.translation.I18n;

/**
 * 
 * @author Mark Gottschling on Dec 9, 2020
 *
 */
public class CrystalSkullChestTileEntity extends AbstractTreasureChestTileEntity {
	
	/**
	 * 
	 * @param texture
	 */
	public CrystalSkullChestTileEntity() {
		super();
		setCustomName(I18n.translateToLocal("display.crystal_skull_chest.name"));
	}
	
	/**
	 * @return the numberOfSlots
	 */
	@Override
	public int getNumberOfSlots() {
		return 9;
	}
}
