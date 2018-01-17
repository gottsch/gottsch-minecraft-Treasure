/**
 * 
 */
package com.someguyssoftware.treasure2.client.model;

import com.someguyssoftware.treasure2.tileentity.TreasureChestTileEntity;

import net.minecraft.client.model.ModelRenderer;

/**
 * @author Mark Gottschling onJan 9, 2018
 *
 */
public interface ITreasureChestModel {

	public void renderAll(TreasureChestTileEntity te);

	public ModelRenderer getLid();
}
