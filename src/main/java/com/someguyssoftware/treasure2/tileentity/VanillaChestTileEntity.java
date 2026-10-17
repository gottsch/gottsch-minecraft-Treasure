package com.someguyssoftware.treasure2.tileentity;

import net.minecraft.util.text.translation.I18n;

/**
 * A treasure chest that looks like a vanilla chest.
 *
 * @author Mark Gottschling on 10/16/2026
 */
public class VanillaChestTileEntity extends AbstractTreasureChestTileEntity {

	public VanillaChestTileEntity() {
		super();
		setCustomName(I18n.translateToLocal("display.vanilla_chest.name"));
	}
}
