/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
 * 
 * All rights reserved.
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package com.someguyssoftware.treasure2.gui.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.someguyssoftware.treasure2.gui.model.ITreasureChestModel;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestBlockEntity;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

/**
 * 	// NEW FORMAT
 * @author Mark Gottschling on Jul 26, 2021
 *
 */
public class TreasureChestTileEntityRenderer extends TileEntityRenderer<AbstractTreasureChestTileEntity> implements ITreasureChestTileEntityRenderer {
	private ResourceLocation texture;
	private ITreasureChestModel model;
	
	public TreasureChestTileEntityRenderer(TileEntityRendererDispatcher dispatcher) {
		super(dispatcher);

	}

	@Override
	public void renderLocks(AbstractTreasureChestTileEntity tileEntity, MatrixStack matrixStack,
			IRenderTypeBuffer renderBuffer, int combinedLight, int combinedOverlay) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(AbstractTreasureChestTileEntity tileEntity, float partialTicks, MatrixStack matrixStack,
			IRenderTypeBuffer renderTypeBuffer, int combinedLight, int combinedOverlay) {
		
		Direction facing = getDirection(tileEntity);
		
		// push the current transformation matrix + normals matrix
		matrixStack.pushPose(); 
		
		// do translation
		
		// do scale
		
		// rotation
		float f = getHorizontalAngle(facing);
		
		matrixStack.mulPose(Vector3f.YP.rotationDegrees(-f));
		
		// update the lid rotation
		
		IVertexBuilder renderBuffer = renderTypeBuffer.getBuffer(model.getChestRenderType(getTexture()));
		model.renderAll(matrixStack, renderBuffer, combinedLight, combinedOverlay, tileEntity);
		matrixStack.popPose();		

		//	////////////// render the locks //////////////////////////////////////
		renderLocks(tileEntity, matrixStack, renderTypeBuffer, combinedLight, combinedOverlay);
		//	////////////// end of render the locks //////////////////////////////////////
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public ResourceLocation getTexture() {
		return texture;
	}

	/**
	 * 
	 * @param texture
	 */
	@Override
	public void setTexture(ResourceLocation texture) {
		this.texture = texture;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public ITreasureChestModel getModel() {
		return model;
	}

	/**
	 * 
	 * @param model
	 */
	@Override
	public void setModel(ITreasureChestModel model) {
		this.model = model;
	}

}
