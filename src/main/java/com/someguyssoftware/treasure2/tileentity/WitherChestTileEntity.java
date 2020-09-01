package com.someguyssoftware.treasure2.tileentity;

import net.minecraft.util.text.translation.I18n;

/**
 * NOTE Wither ChestConfig does not have a "lid", but has doors, however, the use of the lidAngle from the super class
 * will still be used.  The rendered will use that value to determine how to render the doors.
 * 
 * @author Mark Gottschling on Jun 17, 2018
 *
 */
public class WitherChestTileEntity extends AbstractTreasureChestTileEntity {
    
	/**
	 * 
	 * @param texture
	 */
	public WitherChestTileEntity() {
		super();
		setCustomName(I18n.translateToLocal("display.wither_chest.name"));
	}
	
	/**
	 * @return the numberOfSlots
	 */
	@Override
	public int getNumberOfSlots() {
		return 42;
	}
}
