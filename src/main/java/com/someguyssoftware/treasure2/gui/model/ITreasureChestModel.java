/**
 * 
 */
package com.someguyssoftware.treasure2.gui.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

/**
 * @author Mark Gottschling onJan 9, 2018
 *
 */
public interface ITreasureChestModel {
	public ModelRenderer getLid();

	void renderAll(MatrixStack matrixStack, IVertexBuilder renderBuffer, int combinedLight, int combinedOverlay,
			AbstractTreasureChestTileEntity te);
	
	/**
	 * wrapper for vanilla Model.getRenderType()
	 * @param locationIn
	 * @return
	 */
	public RenderType getChestRenderType(ResourceLocation location);
}
