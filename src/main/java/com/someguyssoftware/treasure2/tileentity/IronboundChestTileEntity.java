package com.someguyssoftware.treasure2.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.TranslationTextComponent;

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
		super(TreasureTileEntities.ironboundChestTileEntityType);
		setCustomName(new TranslationTextComponent("display.ironbound_chest.name"));
	}

	@Override
	public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
		// TODO Auto-generated method stub
		return null;
	}
}
