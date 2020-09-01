package com.someguyssoftware.treasure2.tileentity;

import net.minecraft.util.text.translation.I18n;

/**
 * 
 * @author Mark Gottschling on Jan 19, 2018
 *
 */
public class GoldStrongboxTileEntity extends AbstractTreasureChestTileEntity {
	
	/**
	 * 
	 * @param texture
	 */
	public GoldStrongboxTileEntity() {
		super();
		setCustomName(I18n.translateToLocal("display.gold_strongbox.name"));
	}
	
	/**
	 * @return the numberOfSlots
	 */
	@Override
	public int getNumberOfSlots() {
		return 15;
	}
}
