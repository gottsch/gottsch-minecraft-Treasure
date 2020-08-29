package com.someguyssoftware.treasure2.gui.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.client.renderer.IRenderTypeBuffer;

public interface ITreasureChestTileEntityRenderer {

	void renderLocks(AbstractTreasureChestTileEntity tileEntity, MatrixStack matrixStack,
			IRenderTypeBuffer renderBuffer, int combinedLight, int combinedOverlay);

}
