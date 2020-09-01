package com.someguyssoftware.treasure2.tileentity;

import net.minecraft.util.text.translation.I18n;

/**
 * 
 * @author Mark Gottschling on Aug 26, 2019
 *
 */
public class SpiderChestTileEntity extends AbstractTreasureChestTileEntity {
	
	/**
	 * 
	 * @param texture
	 */
	public SpiderChestTileEntity() {
		super();
		setCustomName(I18n.translateToLocal("display.spider_chest.name"));
	}
}
