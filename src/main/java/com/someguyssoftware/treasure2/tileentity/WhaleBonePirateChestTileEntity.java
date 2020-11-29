/**
 * 
 */
package com.someguyssoftware.treasure2.tileentity;

import net.minecraft.util.text.translation.I18n;

/**
 * @author Mark Gottschling on Aug 20, 2019
 *
 */
public class WhaleBonePirateChestTileEntity extends AbstractTreasureChestTileEntity {
	/**
	 * 
	 * @param texture
	 */
	public WhaleBonePirateChestTileEntity() {
		super();
		setCustomName(I18n.translateToLocal("display.whale_bone_pirate_chest.name"));
	}
}
