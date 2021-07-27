package com.someguyssoftware.treasure2.gui.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.someguyssoftware.treasure2.block.StandardChestBlock;
import com.someguyssoftware.treasure2.gui.model.ITreasureChestModel;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

public interface ITreasureChestTileEntityRenderer {

	void renderLocks(AbstractTreasureChestTileEntity tileEntity, MatrixStack matrixStack,
			IRenderTypeBuffer renderBuffer, int combinedLight, int combinedOverlay);

	/**
	 * 
	 * @param tileEntity
	 * @return
	 */
	default public Direction getDirection(TileEntity tileEntity) {
		BlockState state = tileEntity.getBlockState();
		Direction direction = Direction.NORTH;
		if (state != null) {
			direction = state.getValue(StandardChestBlock.FACING);
		}
		return direction;
	}
	
	/**
	 * Helper method since all my models face the opposite direction of vanilla models
	 * @param meta
	 * @return
	 */
	default public int getHorizontalAngle(Direction facing) {
		switch (facing) {
		default:
		case NORTH:
			return 0;
		case SOUTH:
			return 180;
		case WEST:
			return 90;
		case EAST:
			return -90;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	ResourceLocation getTexture();

	/**
	 * 
	 * @param texture
	 */
	void setTexture(ResourceLocation texture);

	/**
	 * 
	 * @return
	 */
	ITreasureChestModel getModel();

	/**
	 * 
	 * @param model
	 */
	void setModel(ITreasureChestModel model);

}
